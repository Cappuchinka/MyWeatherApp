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
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, context: Context) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title("Voronezh")
        InfoLastUpdated()
        GetWeatherByCurrentLocation(weatherViewModel, context)
    }
}


@Composable
fun Title(city: String) {
    Box(modifier = Modifier
        .fillMaxHeight(0.08f)
        .background(MaterialTheme.colorScheme.primary)) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxHeight(1f)
                .padding(15.dp),
                contentAlignment = Alignment.CenterStart) {
                Text(text = city,
                     color = MaterialTheme.colorScheme.onTertiary,
                     fontSize = 22.sp)
            }
            Box(modifier = Modifier
                .fillMaxSize(1f)
                .padding(11.dp),
                contentAlignment = Alignment.CenterEnd)
            {
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
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        contentAlignment = Alignment.Center) {
        Text(text = "LAST UPDATED AT: $date")
    }
}

@Composable
private fun GetWeatherByCurrentLocation(weatherViewModel: WeatherViewModel, context: Context) {
    LaunchedEffect(context) {
        weatherViewModel.setContext(context)
        weatherViewModel.getWeatherByCurrentLocation(context)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val weatherIconUrl = weatherViewModel.weatherIcon.value

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.3f),
        contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                LoadImageWithCache(context = context,
                                   iconUrl = "https://openweathermap.org/img/wn/${weatherIconUrl}@2x.png",
                                   size = 128.dp)
                Text(text = "${weatherResponse?.weather?.getOrNull(0)?.description}")
            }
            Column(verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${weatherResponse?.main?.temp}°C", fontSize = 50.sp) // temperature
                Text(text = "${weatherResponse?.main?.feels_like}°C", fontSize = 20.sp) // temperature feels like
            }
        }
    }
}

@Composable
fun LoadImageWithCache(context: Context, iconUrl: String, size: Dp) {

    val imageLoader = ImageLoader.Builder(context)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("weather_icon_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()

    var isLoading by remember { mutableStateOf(true) } // Добавляем состояние для отслеживания загрузки

    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(iconUrl)
            .memoryCacheKey(iconUrl)
            .diskCacheKey(iconUrl)
            .crossfade(true)
            .listener(
                onSuccess = { request, metadata ->
                    isLoading = false // Устанавливаем isLoading в false, когда изображение загружено
                    Log.d("LOAD_ICON", "onSuccess")
                },
                onError = { request, throwable ->
                    Log.d("LOAD_ICON", "onError")// Обработка ошибки, если не удалось загрузить изображение
                }
            )
            .build()
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