package ru.kapuchinka.myweatherapp.utils.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun getDate(): String {
    val dateTime = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")

    return dateTime.format(formatter)
}

fun getTime(milliseconds: Long): String {
    val time = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(milliseconds), ZoneId.systemDefault()
    )

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    return time.format(formatter)
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

@SuppressLint("DiscouragedApi")
@Composable
fun ThemedImage(context: Context, nameIcon: String) {
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

