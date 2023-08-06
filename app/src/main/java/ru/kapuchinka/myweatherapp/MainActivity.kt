package ru.kapuchinka.myweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import ru.kapuchinka.myweatherapp.ui.theme.MyMaterialTheme
import ru.kapuchinka.myweatherapp.view.navigation.BottomNavigationWeather
import ru.kapuchinka.myweatherapp.view.navigation.NavGraph
import ru.kapuchinka.myweatherapp.viewmodel.WeatherDialogViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherDialogViewModel: WeatherDialogViewModel by viewModels()
    private val weatherRoomViewModel: WeatherRoomViewModel by viewModels()
    private val context: Context = this

    companion object {
        const val API_KEY = "a5df04fc-aa50-4a3d-b280-ef7eee65595b"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepVisibleCondition  {
                weatherViewModel.isLoading.value
        }
        weatherViewModel.setContext(this)
        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this.application)

        setContent {
            MyMaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationWeather(navController = navController)
                        }
                    ) {
                        NavGraph(
                            navHostController = navController,
                            weatherViewModel = weatherViewModel,
                            weatherDialogViewModel = weatherDialogViewModel,
                            weatherRoomViewModel = weatherRoomViewModel,
                            context = context
                        )
                    }
                }
            }
        }
    }
}
