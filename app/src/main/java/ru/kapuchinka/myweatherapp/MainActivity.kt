package ru.kapuchinka.myweatherapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.kapuchinka.myweatherapp.ui.theme.MyWeatherAppTheme
import ru.kapuchinka.myweatherapp.utils.permission.RequestPermission
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherRoomViewModel: WeatherRoomViewModel by viewModels()
    private val context: Context = this

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.setContext(this)

//        weatherRoomViewModel.insertWeather(WeatherModel(id = null, city = "Moscow", lat = 55.7522, lon = 37.6156))

        setContent {
            MyWeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)
                    Column {
//                        GetWeatherByCity("Moscow", weatherViewModel)
                        GetWeatherByCurrentLocation(context = context, weatherViewModel = weatherViewModel)
//                        GetAll(weatherRoomViewModel = weatherRoomViewModel)
                        GetWeatherById(weatherRoomViewModel = weatherRoomViewModel, id = 2)
                    }
                }
            }
        }
    }
}

@Composable
fun GetWeatherByCity(city: String, weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(city) {
        weatherViewModel.getWeatherByCity(city)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val temp = weatherResponse?.main?.temp
    val weatherIconUrl = weatherViewModel.weatherIcon.value

    Column {
        Text(
            text = "Hello $temp!",
            modifier = modifier
        )
        if (!weatherIconUrl.isNullOrBlank()) {
            WeatherIcon(iconUrl = "https://openweathermap.org/img/w/${weatherIconUrl}.png", size = 128.dp)
        }
    }
}

@Composable
fun GetWeatherByCurrentLocation(context: Context, weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(context) {
        weatherViewModel.getWeatherByCurrentLocation(context)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val temp = weatherResponse?.main?.temp
    val weatherIconUrl = weatherViewModel.weatherIcon.value

    Column {
        Text(
            text = "Temp: $temp",
            modifier = modifier
        )
        if (!weatherIconUrl.isNullOrBlank()) {
            LoadImageWithCache(iconUrl = "https://openweathermap.org/img/w/${weatherIconUrl}.png", size = 128.dp, context = context)
        }
    }
}

@Composable
fun WeatherIcon(iconUrl: String, size: Dp) {
    var isLoading by remember { mutableStateOf(true) } // Добавляем состояние для отслеживания загрузки

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
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .listener(
                    onSuccess = { request, metadata ->
                        isLoading = false // Устанавливаем isLoading в false, когда изображение загружено
                    },
                    onError = { request, throwable ->
                        // Обработка ошибки, если не удалось загрузить изображение
                    }
                )
                .build(),
            contentDescription = "Weather Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size),
        )
    }
}

@Composable
fun LoadImageWithCache(context: Context, iconUrl: String, size: Dp){
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
                },
                onError = { request, throwable ->
                    // Обработка ошибки, если не удалось загрузить изображение
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

@Composable
fun GetAll(weatherRoomViewModel: WeatherRoomViewModel) {
    val allData by weatherRoomViewModel.allData.observeAsState(emptyList())
    Column {
        allData.forEach { weatherModel ->
            Text(text = weatherModel.toString())
        }
    }
}

@Composable
fun GetWeatherById(weatherRoomViewModel: WeatherRoomViewModel, id: Int) {
    LaunchedEffect(id) {
        weatherRoomViewModel.getWeatherById(id)
    }
    val weather by weatherRoomViewModel.weatherById.observeAsState()
    Column {
        Text(text = weather.toString())
    }
}


