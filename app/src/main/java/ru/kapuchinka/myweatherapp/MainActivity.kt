package ru.kapuchinka.myweatherapp

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
import ru.kapuchinka.myweatherapp.ui.theme.MyMaterialTheme
import ru.kapuchinka.myweatherapp.view.weathers_list.FavoritesList
import ru.kapuchinka.myweatherapp.view.weathers_list.LocationsList
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherRoomViewModel: WeatherRoomViewModel by viewModels()
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.setContext(this)

        setContent {
            MyMaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    WeatherScreen(weatherViewModel = weatherViewModel, context = context)
//                    LocationsList(
//                        weatherRoomViewModel = weatherRoomViewModel,
//                        weatherViewModel = weatherViewModel,
//                        context = context
//                    )
                    FavoritesList(
                        weatherRoomViewModel = weatherRoomViewModel,
                        weatherViewModel = weatherViewModel,
                        context = context
                    )
                }
            }
        }
    }
}
