package com.example.efisherypricelist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                Surface {
                    if (!isNetworkAvailable()) {
                        Column {
                            Image(painterResource(R.drawable.ic_no_internet), "no_internet")
                            Text("No Internet Connection!")
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(20.dp)) {
                            var name by remember { mutableStateOf("") }
                            var price by remember { mutableStateOf("") }
                            var city by remember { mutableStateOf("") }
                            var province by remember { mutableStateOf("") }
                            var size by remember { mutableStateOf(0) }

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedTextField(area, Modifier.fillMaxWidth(), "Area") { i ->
                                city = liveArea.value!![i].city
                                province = liveArea.value!![i].province
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Price") },
                                    modifier = Modifier.weight(1.5f),
                                )

                                ExposedTextField(allSize, Modifier.weight(1f), "Size") { i ->
                                    size = liveSize.value!![i].size
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.End.also { Arrangement.spacedBy(10.dp) },
                                modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = { finish() },
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Text("Cancel")
                                }
                                Button(
                                    onClick = {
                                        postData(name, province, city, size, price.toInt()) {
                                            Toast.makeText(this@AddDataActivity, "Success added data", Toast.LENGTH_SHORT).show()
                                            viewModel.refreshData()
                                            finish()
                                        }
                                    },
                                    modifier = Modifier.wrapContentWidth()) {
                                    Text("Add")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun postData(
        name: String,
        province: String,
        city: String,
        size: Int,
        price: Int,
        onFinished: () -> Unit
    ) {
        val post = viewModel.postPrice(name, province, city, size, price)
        post.observe(this) {
            if (it != null) onFinished()
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
fun ExposedTextField(data: List<String>, modifier: Modifier, label: String, selected: (Int) -> Unit) {
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    var expanded by remember { mutableStateOf(false) }

    val icon = if(expanded) painterResource(R.drawable.ic_arrow_drop_up)
    else painterResource(R.drawable.ic_arrow_drop_down)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .onFocusChanged { expanded = it.isFocused }
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }.fillMaxWidth(),
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(icon, "arrow_down")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            data.forEachIndexed { i, text ->
                DropdownMenuItem(onClick = {
                    selectedText = text
                    expanded = false
                    selected(i)
                }) {
                    Text(text = text)
                }
            }
        }
    }
}