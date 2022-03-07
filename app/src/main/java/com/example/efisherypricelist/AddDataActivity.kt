package com.example.efisherypricelist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme

class AddDataActivity : ComponentActivity() {

    private lateinit var viewModel: AddDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AddDataViewModel(MainRepository(application))

        setContent {
            EfisheryPriceListTheme {
                Column(verticalArrangement = Arrangement.Center) {
                    if (!isNetworkAvailable()) {
                        Image(painterResource(R.drawable.ic_no_internet),"no_internet")
                        Text("No Internet Connection!")
                    } else {
                        Column {
                            val name = remember { mutableStateOf(TextFieldValue()) }

                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { name.value = it }
                            )
                            val listArea by viewModel.getArea().observeAsState(initial = emptyList())
                            val listText = mutableListOf<String>()
                            for (i in listArea) {
                                listText.add("${i.city}, ${i.province}")
                            }
                            ExposedTextField(listText)
                            Row {
                                OutlinedTextField(
                                    value = name.value,
                                    onValueChange = { name.value = it }
                                )
                                
                            }
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailable(): Boolean {
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))
    }
}

@Composable
fun ExposedTextField(data: List<String>) {
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    var expanded by remember { mutableStateOf(false) }

    val icon = if(expanded)
        painterResource(R.drawable.ic_arrow_drop_up)
    else
        painterResource(R.drawable.ic_arrow_drop_down)

    OutlinedTextField(
        value = selectedText,
        onValueChange = { selectedText = it },
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textfieldSize = coordinates.size.toSize()
            },
        label = {Text("Area")},
        trailingIcon = {
            Icon(icon,"arrow", Modifier.clickable { expanded = !expanded })
        }
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(with(LocalDensity.current){textfieldSize.width.toDp()})
    ) {
        data.forEach { text ->
            DropdownMenuItem(onClick = {
                selectedText = text
            }) {
                Text(text = text)
            }
        }
    }
}