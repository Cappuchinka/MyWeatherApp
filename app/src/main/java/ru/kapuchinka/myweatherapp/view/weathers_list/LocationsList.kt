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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel

@Composable
fun LocationsList(weatherRoomViewModel: WeatherRoomViewModel, context: Context) {
    Column() {
        Title(context = context, weatherRoomViewModel = weatherRoomViewModel)
        GetListCities(
            weatherRoomViewModel = weatherRoomViewModel, context = context
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
                        start = 15.dp,
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
                ThemedImageFavorite(
                    context = context,
                    nameIcon = "plus",
                    weatherRoomViewModel = weatherRoomViewModel
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    weatherRoomViewModel: WeatherRoomViewModel, weatherModel: WeatherModel, context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp))
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
                    context = context,
                    "no_favorite",
                    weatherRoomViewModel = weatherRoomViewModel
                )
            }
        }
    }
}

@Composable
private fun GetListCities(weatherRoomViewModel: WeatherRoomViewModel, context: Context) {

    val allData: List<WeatherModel> by weatherRoomViewModel.allData.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(allData.size) { city ->
            CardItem(
                weatherRoomViewModel = weatherRoomViewModel,
                weatherModel = allData[city],
                context = context
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun ThemedImageFavorite(
    context: Context, nameIcon: String, weatherRoomViewModel: WeatherRoomViewModel
) {
    val isDarkTheme = isSystemInDarkTheme()
    val resourceType = "drawable"

    val showDialog = remember { mutableStateOf(false) }

    fun showDialog() {
        showDialog.value = true
    }

    val painter = if (isDarkTheme) {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_dark", resourceType, context.packageName
            )
        )
    } else {
        painterResource(
            context.resources.getIdentifier(
                "${nameIcon}_light", resourceType, context.packageName
            )
        )
    }

    if (nameIcon == "plus") {
        Image(
            painter = painter,
            contentDescription = nameIcon,
            modifier = Modifier.clickable {
                showDialog()
            }
        )
        if (showDialog.value) {
            AddLocationDialog(weatherRoomViewModel = weatherRoomViewModel, showDialog = showDialog)
        }
    } else if (nameIcon == "no_favorite" || nameIcon == "favorite") {
        Image(
            painter = painter,
            contentDescription = nameIcon,
            modifier = Modifier.clickable {
                Log.d("ADD_TO_FAVORITE", "ADD_TO_FAVORITE")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddLocationDialog(
    weatherRoomViewModel: WeatherRoomViewModel,
    showDialog: MutableState<Boolean>
) {
    var city by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {}) {
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier)
                TextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = city,
                    onValueChange = { newText -> city = newText },
                    label = { Text("Enter city") })
                Spacer(modifier = Modifier)
                Button(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = {
                        addLocation(
                            weatherRoomViewModel = weatherRoomViewModel, city = city
                        )
                        showDialog.value = !showDialog.value
                    }) {
                    Text(text = "Add")
                }
                Spacer(modifier = Modifier)
            }
        }
    }
}

private fun addLocation(weatherRoomViewModel: WeatherRoomViewModel, city: String) {
    Log.d("ADD_CITY", "ADD: ${city.trim().toLowerCase().capitalize()}")
    weatherRoomViewModel.insertWeather(city.trim().toLowerCase().capitalize())
}