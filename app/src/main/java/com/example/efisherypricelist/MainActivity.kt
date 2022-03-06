package com.example.efisherypricelist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme
import com.example.efisherypricelist.ui.theme.Purple500

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: FishViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = FishViewModel(MainRepository(application))

        viewModel.getPrices().observe(this) {
            viewModel.setData(it)
        }

        setContent {
            EfisheryPriceListTheme {
                Box {
                    Column {
                        Header { viewModel.getPricesByName(it) }
                        DropdownSort { by ->
                            when (by) {
                                "Name" -> viewModel.sortPricesByName().observe(this@MainActivity) {
                                    viewModel.setData(it)
                                }
                                "Price" -> viewModel.sortPricesByPrice()
                                    .observe(this@MainActivity) {
                                        viewModel.setData(it)
                                    }
                                "Size" -> viewModel.sortPricesBySize().observe(this@MainActivity) {
                                    viewModel.setData(it)
                                }
                            }
                        }
                        val listData by viewModel.prices.observeAsState(initial = emptyList())
                        LazyColumn(
                            contentPadding = PaddingValues(20.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            items(listData) { fish ->
                                ItemData(fish)
                            }
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                        onClick = {
                            val intent = Intent(this@MainActivity, AddDataActivity::class.java)
                            startActivity(intent)
                        }
                    ) {
                        Text("+")
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownSort(filter: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Name", "Price", "Size")
    var selectedText by remember { mutableStateOf("Sort By") }
    Column (
        modifier = Modifier.fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .padding(20.dp, 0.dp),
        horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(selectedText)
            IconButton(onClick = { expanded = true }) {
                Icon(painterResource(R.drawable.ic_sort), "sort")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedText = items[index]
                    expanded = false
                    filter(selectedText)
                }) {
                    Text(s)
                }
            }
        }
    }
}

@Composable
fun ItemData(fish: Fish) {
    if (fish.name.length < 25) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(20.dp)) {
            Column {
                Text(fish.name, fontSize = 18.sp, color = Color.Black)
                Text("${fish.city}, ${fish.province}", modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
                Text(fish.price.toRupiah(), fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun Header(callback: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val textState = remember { mutableStateOf("") }
        val view = LocalView.current
        val onValueChange: (String) -> Unit = {
            textState.value = ""
            callback(textState.value)
        }
        Image(painterResource(R.drawable.logo_efishery), "logo efishery")
        Card (
            modifier = Modifier.padding(10.dp),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            TextField(
                value = textState.value,
                onValueChange = { onValueChange(it) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    if (textState.value.isEmpty()) {
                        Icon(painterResource(R.drawable.ic_search), "search")
                    } else {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(painterResource(R.drawable.ic_clear), "clear")
                        }
                    }
                },
                keyboardActions = KeyboardActions(onDone = {
                    view.clearFocus()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}