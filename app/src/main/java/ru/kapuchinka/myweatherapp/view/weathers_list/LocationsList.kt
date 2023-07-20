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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel
import ru.kapuchinka.myweatherapp.view.weather.ShowWeatherDialog
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

@Composable
fun LocationsList(
    weatherRoomViewModel: WeatherRoomViewModel,
    weatherViewModel: WeatherViewModel,
    context: Context
) {

    val allData: List<WeatherModel> by weatherRoomViewModel.allData.observeAsState(emptyList())

    Column(modifier = Modifier.padding(bottom = 56.dp)) {
        Title(context = context, weatherRoomViewModel = weatherRoomViewModel)
        GetListCities(
            allData = allData,
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
                    text = "Locations",
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 25.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .padding(
                        top = 3.dp,
                        start = 3.dp,
                        end = 15.dp,
                        bottom = 3.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                AddLocationImage(
                    context = context,
                    weatherRoomViewModel = weatherRoomViewModel
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
            Box(
                modifier = Modifier.fillMaxWidth(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DeleteLocationImage(
                            context = context,
                            weatherRoomViewModel = weatherRoomViewModel,
                            city = weatherModel.city
                        )
                    }
                    Box(
                        modifier = Modifier.size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ThemedImageFavorite(
                            weatherModel = weatherModel,
                            context = context,
                            weatherRoomViewModel = weatherRoomViewModel
                        )
                    }
                }

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
    allData: List<WeatherModel>,
    weatherRoomViewModel: WeatherRoomViewModel,
    weatherViewModel: WeatherViewModel,
    context: Context
) {

    if (allData.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Locations List is empty",
                fontSize = 20.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(allData.size) { city ->
                CardItem(
                    weatherRoomViewModel = weatherRoomViewModel,
                    weatherViewModel = weatherViewModel,
                    weatherModel = allData[city],
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

    var isFavorite by remember { mutableStateOf(weatherModel.is_favorite) }

    val painter = if (isFavorite) {
        getPainter(isDarkTheme, context, "favorite")
    } else {
        getPainter(isDarkTheme, context, "no_favorite")
    }

    val updatedPainter = rememberUpdatedState(painter)

    Image(
        painter = updatedPainter.value,
        contentDescription = "favorite",
        modifier = Modifier.clickable {
            if (!isFavorite) {
                weatherRoomViewModel.updateCity(true, weatherModel.city)
                Log.d("ADD_TO_FAVORITE", weatherModel.city)
            } else {
                weatherRoomViewModel.updateCity(false, weatherModel.city)
                Log.d("REMOVE_FROM_FAVORITE", weatherModel.city)
            }
            isFavorite = !isFavorite
        }
    )
}

@SuppressLint("DiscouragedApi")
@Composable
private fun getPainter(
    isDarkTheme: Boolean,
    context: Context,
    nameIcon: String,
): Painter {
    return if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_dark", "drawable", context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_light", "drawable", context.packageName
            )
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun AddLocationImage(
    context: Context,
    weatherRoomViewModel: WeatherRoomViewModel
) {
    val isDarkTheme = isSystemInDarkTheme()
    val resourceType = "drawable"

    val showDialog = remember { mutableStateOf(false) }

    fun openDialog() {
        showDialog.value = true
    }

    fun closeDialog() {
        showDialog.value = false
    }

    val painter = if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "plus_dark", resourceType, context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "plus_light", resourceType, context.packageName
            )
        )
    }

    Image(
        painter = painter,
        contentDescription = "plus",
        modifier = Modifier.clickable {
            openDialog()
        }
    )

    if (showDialog.value) {
        AddLocationDialog(
            weatherRoomViewModel = weatherRoomViewModel,
            onDismiss = { closeDialog() })
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun DeleteLocationImage(
    context: Context,
    weatherRoomViewModel: WeatherRoomViewModel,
    city: String
) {
    val isDarkTheme = isSystemInDarkTheme()
    val resourceType = "drawable"

    val painter = if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "trash_dark", resourceType, context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "trash_light", resourceType, context.packageName
            )
        )
    }

    Image(
        painter = painter,
        contentDescription = "trash",
        modifier = Modifier.clickable {
            weatherRoomViewModel.deleteCity(city)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddLocationDialog(
    weatherRoomViewModel: WeatherRoomViewModel,
    onDismiss: () -> Unit
) {
    var city by remember { mutableStateOf("") }

    val textFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                )
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                TextField(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth(0.9f),
                    value = city,
                    onValueChange = { newText -> city = newText },
                    label = { Text("Enter city") },
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = {
                        addLocation(
                            weatherRoomViewModel = weatherRoomViewModel, city = city
                        )
                        onDismiss()
                    }) {
                    Text(text = "Add")
                }
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }
}

private fun addLocation(weatherRoomViewModel: WeatherRoomViewModel, city: String) {
    val mCity = city.trim().toLowerCase().capitalize()
    if (checkInputCity(mCity)) {
        Log.d("ADD_CITY", "ADD: $mCity")
        weatherRoomViewModel.insertWeather(mCity)
    }
}

private fun checkInputCity(city: String): Boolean {
    val regex = "[A-Za-zА-Яа-я]+(-[0-9]+)?"
    return city.matches(regex.toRegex())
}