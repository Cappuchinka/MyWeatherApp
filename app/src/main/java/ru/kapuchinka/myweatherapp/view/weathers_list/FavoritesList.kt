package ru.kapuchinka.myweatherapp.view.weathers_list

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel
import ru.kapuchinka.myweatherapp.view.weather.ShowWeatherDialog
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

@Composable
fun FavoritesList(
    weatherRoomViewModel: WeatherRoomViewModel,
    weatherViewModel: WeatherViewModel,
    context: Context
) {
    Column(modifier = Modifier.padding(bottom = 56.dp)) {
        Title(context = context, weatherRoomViewModel = weatherRoomViewModel)
        GetListCities(
            weatherRoomViewModel = weatherRoomViewModel,
            weatherViewModel = weatherViewModel,
            context = context
        )
    }
}

@Composable
private fun Title(context: Context, weatherRoomViewModel: WeatherRoomViewModel) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.09f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(
                        top = 3.dp,
                        start = 22.dp,
                        end = 3.dp,
                        bottom = 3.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Favorites",
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    weatherRoomViewModel: WeatherRoomViewModel,
    weatherViewModel: WeatherViewModel,
    weatherModel: WeatherModel,
    context: Context
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedCity = remember { mutableStateOf("") }

    fun openDialog() {
        selectedCity.value = weatherModel.city
        showDialog.value = true
    }

    fun closeDialog() {
        showDialog.value = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp))
            .clickable { openDialog() }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weatherModel.city,
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Box(modifier = Modifier.size(36.dp)) {
                ThemedImageFavorite(
                    weatherModel = weatherModel,
                    context = context,
                    weatherRoomViewModel = weatherRoomViewModel
                )
            }
        }
    }

    if (showDialog.value) {
        ShowWeatherDialog(
            city = selectedCity.value,
            weatherViewModel = weatherViewModel,
            context = context,
            onDismiss = { closeDialog() })
    }
}

@Composable
private fun GetListCities(
    weatherRoomViewModel: WeatherRoomViewModel,
    weatherViewModel: WeatherViewModel,
    context: Context
) {

    val favoritesLocations: List<WeatherModel> by weatherRoomViewModel.favoritesLocations.observeAsState(
        emptyList()
    )

    if (favoritesLocations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Favorites List is empty",
                fontSize = 20.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(favoritesLocations.size) { city ->
                CardItem(
                    weatherRoomViewModel = weatherRoomViewModel,
                    weatherViewModel = weatherViewModel,
                    weatherModel = favoritesLocations[city],
                    context = context
                )
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun ThemedImageFavorite(
    weatherModel: WeatherModel,
    context: Context,
    weatherRoomViewModel: WeatherRoomViewModel
) {
    val isDarkTheme = isSystemInDarkTheme()
    val resourceType = "drawable"

    val painter = if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "favorite_dark", resourceType, context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "favorite_light", resourceType, context.packageName
            )
        )
    }

    val updatedPainter = rememberUpdatedState(painter)

    Image(
        painter = updatedPainter.value,
        contentDescription = "favorite",
        modifier = Modifier.clickable {
            weatherRoomViewModel.updateCity(false, weatherModel.city)
            Log.d("REMOVE_TO_FAVORITE", weatherModel.city)
        }
    )
}