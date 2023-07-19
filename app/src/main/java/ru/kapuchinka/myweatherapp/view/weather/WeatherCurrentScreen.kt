package ru.kapuchinka.myweatherapp.view.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse
import ru.kapuchinka.myweatherapp.utils.receiver.NetworkChangeReceiver
import ru.kapuchinka.myweatherapp.view.permission.RequestPermission
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherCurrentLocationScreen(weatherViewModel: WeatherViewModel, context: Context) {

    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val isInternetConnected = remember { mutableStateOf(false) }

    val networkChangeReceiver = remember {
        NetworkChangeReceiver { isConnected ->
            isInternetConnected.value = isConnected
            if (isConnected) {
                weatherViewModel.getWeatherByCurrentLocation(context)
            }
        }
    }

    if (locationPermissionState.status.isGranted) {

        DisposableEffect(Unit) {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(networkChangeReceiver, filter)

            onDispose {
                context.unregisterReceiver(networkChangeReceiver)
            }
        }

        LaunchedEffect(Unit) {

            isInternetConnected.value = isInternetConnected(context)
            if (isInternetConnected.value) {
                weatherViewModel.setContext(context)
                weatherViewModel.getWeatherByCurrentLocation(context)
            }
        }

        val weatherResponse = weatherViewModel.weatherResponse.value

        if (isInternetConnected.value) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
            ) {
                if (weatherResponse != null) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Title(weatherResponse.body()!!.name)
                        InfoLastUpdated()
                        GetWeatherByCurrentLocation(weatherResponse.body()!!, context)
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
private fun Title(city: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.09f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(
                        top = 3.dp,
                        start = 20.dp,
                        end = 3.dp,
                        bottom = 3.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = city, color = MaterialTheme.colorScheme.onTertiary, fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
private fun InfoLastUpdated() {
    val date = getDate()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 0.dp, end = 0.dp, bottom = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "LAST UPDATED AT: $date")
    }
}

@Composable
private fun GetWeatherByCurrentLocation(weatherResponse: WeatherResponse, context: Context) {
    val weatherIconUrl = weatherResponse.weather[0].icon

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (weatherIconUrl.isNotBlank()) {
                            LoadImageWithCache(
                                context = context,
                                iconUrl = "https://openweathermap.org/img/wn/${weatherIconUrl}@2x.png",
                                size = 128.dp
                            )
                        }
                        Text(text = "Cloud: ${weatherResponse.clouds.all}%")
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${weatherResponse.main.temp}°C", fontSize = 50.sp
                        ) // temperature
                        Text(
                            text = "Feel like: ${weatherResponse.main.feels_like}°C",
                            fontSize = 20.sp
                        ) // temperature feels like
                    }
                }
                Text(
                    text = weatherResponse.weather[0].description,
                    fontSize = 20.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(1f)
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(48.dp)) {
                    ThemedImage(context = context, nameIcon = "temp_min")
                }
                Text(text = "${weatherResponse.main.temp_min}°C", fontSize = 20.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(48.dp)) {
                    ThemedImage(context = context, nameIcon = "temp_max")
                }
                Text(text = "${weatherResponse.main.temp_max}°C", fontSize = 20.sp)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(
                    top = 5.dp,
                    start = 20.dp,
                    end = 0.dp,
                    bottom = 0.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ThemedImage(context = context, nameIcon = "wind")
                }
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = "Wind: ${weatherResponse.wind.speed} meter/sec"
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ThemedImage(context = context, nameIcon = "pressure")
                }
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = "Pressure: ${weatherResponse.main.pressure} hPa"
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ThemedImage(context = context, nameIcon = "humidity")
                }
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = "Humidity: ${weatherResponse.main.humidity}%"
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(1f)
                .fillMaxHeight(0.8f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(72.dp)) {
                    ThemedImage(context = context, nameIcon = "sunrise")
                }
                Text(
                    text = getTime(weatherResponse.sys.sunrise), fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(72.dp)) {
                    ThemedImage(context = context, nameIcon = "sunset")
                }
                Text(text = getTime(weatherResponse.sys.sunset), fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun LoadImageWithCache(context: Context, iconUrl: String, size: Dp) {
    val imageLoader =
        ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
            MemoryCache.Builder(context).maxSizePercent(0.25).build()
        }.diskCachePolicy(CachePolicy.ENABLED).diskCache {
            DiskCache.Builder().directory(context.cacheDir.resolve("weather_icon_cache"))
                .maxSizePercent(0.02).build()
        }.build()

    var isLoading by remember { mutableStateOf(true) } // Добавляем состояние для отслеживания загрузки

    val imageRequest = remember {
        ImageRequest.Builder(context).data(iconUrl).memoryCacheKey(iconUrl).diskCacheKey(iconUrl)
            .crossfade(true).listener(onSuccess = { _, _ ->
                isLoading = false // Устанавливаем isLoading в false, когда изображение загружено
                Log.d("LOAD_ICON", "onSuccess")
            }, onError = { _, _ ->
                Log.d(
                    "LOAD_ICON", "onError"
                )// Обработка ошибки, если не удалось загрузить изображение
            }).build()
    }

    imageLoader.enqueue(request = imageRequest)

    Box(
        modifier = Modifier
            .height(size)
            .width(size),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
        AsyncImage(
            model = imageRequest,
            imageLoader = imageLoader,
            contentDescription = "Weather Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size),
        )
    }
}

private fun getDate(): String {
    val dateTime = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")

    return dateTime.format(formatter)
}

private fun getTime(milliseconds: Long): String {
    val time = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(milliseconds), ZoneId.systemDefault()
    )

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    return time.format(formatter)
}

@SuppressLint("DiscouragedApi")
@Composable
private fun ThemedImage(context: Context, nameIcon: String) {
    val isDarkTheme = isSystemInDarkTheme()
    val resourceType = "drawable"

    val painter = if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_dark",
                resourceType,
                context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_light",
                resourceType,
                context.packageName
            )
        )
    }

    Image(
        painter = painter,
        contentDescription = nameIcon // Здесь можно указать описание изображения
    )
}

private fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}