package ru.kapuchinka.myweatherapp.view.weather

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import ru.kapuchinka.myweatherapp.R
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, context: Context) {

    LaunchedEffect(context) {
        weatherViewModel.setContext(context)
        weatherViewModel.getWeatherByCurrentLocation(context)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        if (weatherResponse != null) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(weatherResponse.name)
                InfoLastUpdated()
                GetWeatherByCurrentLocation(weatherResponse, context)
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
fun Title(city: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.09f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(15.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = city, color = MaterialTheme.colorScheme.onTertiary, fontSize = 22.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(11.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(painter = painterResource(id = R.drawable.trash_bin),
                    contentDescription = "trash",
                    modifier = Modifier.clickable { Log.d("TRASH", "CLICKED") })
            }
        }
    }
}

@Composable
fun InfoLastUpdated() {
    val date = getDate()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), contentAlignment = Alignment.Center
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
                        if (!weatherIconUrl.isNullOrBlank()) {
                            LoadImageWithCache(
                                context = context,
                                iconUrl = "https://openweathermap.org/img/wn/${weatherIconUrl}@2x.png",
                                size = 128.dp
                            )
                        }
                        Text(text = "Cloud: ${weatherResponse?.clouds?.all}%")
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${weatherResponse?.main?.temp}°C", fontSize = 50.sp
                        ) // temperature
                        Text(
                            text = "Feel like: ${weatherResponse?.main?.feels_like}°C",
                            fontSize = 20.sp
                        ) // temperature feels like
                    }
                }
                Text(
                    text = "${weatherResponse?.weather?.getOrNull(0)?.description}",
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
                Box(modifier = Modifier.size(56.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.temp_min_light_theme),
                        contentDescription = "temp_min"
                    )
                }
                Text(text = "${weatherResponse?.main?.temp_min}°C", fontSize = 20.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(56.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.temp_max_light_theme),
                        contentDescription = "temp_max"
                    )
                }
                Text(text = "${weatherResponse?.main?.temp_max}°C", fontSize = 20.sp)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wind_light),
                        contentDescription = "wind"
                    )
                }
                Text(text = "Wind: ${weatherResponse?.wind?.speed} meter/sec")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pressure_light),
                        contentDescription = "pressure"
                    )
                }
                Text(text = "Pressure: ${weatherResponse?.main?.pressure} hPa")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.humidity_light),
                        contentDescription = "humidity"
                    )
                }
                Text(text = "Humidity: ${weatherResponse?.main?.humidity}%")
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
                Box(modifier = Modifier.size(80.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise_light),
                        contentDescription = "sunrise"
                    )
                }
                Text(
                    text = "${weatherResponse?.sys?.let { getTime(it.sunrise) }}", fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(80.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.sunset_light),
                        contentDescription = "sunset"
                    )
                }
                Text(text = "${weatherResponse?.sys?.let { getTime(it.sunset) }}", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun LoadImageWithCache(context: Context, iconUrl: String, size: Dp) {
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
            .crossfade(true).listener(onSuccess = { request, metadata ->
                isLoading = false // Устанавливаем isLoading в false, когда изображение загружено
                Log.d("LOAD_ICON", "onSuccess")
            }, onError = { request, throwable ->
                Log.d(
                    "LOAD_ICON", "onError"
                )// Обработка ошибки, если не удалось загрузить изображение
            }).build()
    }

    imageLoader.enqueue(request = imageRequest)

    Box(
        modifier = Modifier
            .height(size)
            .width(size), contentAlignment = Alignment.Center
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