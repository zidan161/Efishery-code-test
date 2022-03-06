package com.example.efisherypricelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                Column {
                    Header { viewModel.getPricesByName(it) }
                    DropdownSort { by ->
                        when (by) {
                            "Name" -> viewModel.sortPricesByName().observe(this@MainActivity) {
                                viewModel.setData(it)
                            }
                            "Price" -> viewModel.sortPricesByPrice().observe(this@MainActivity) {
                                viewModel.setData(it)
                            }
                            "Size" -> viewModel.sortPricesBySize().observe(this@MainActivity) {
                                viewModel.setData(it)
                            }
                        }
                    }
                    val listData by viewModel.prices.observeAsState(initial = emptyList())
                    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(listData) { fish ->
                            ItemData(fish)
                        }
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
    Row {
        Text(fish.name, )
        Column {
            Text(fish.price.toString())
        }
    }
}

@Composable
fun Header(callback: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Image(painterResource(R.drawable.logo_efishery), "logo efishery")
        Card (modifier = Modifier.padding(10.dp)) {
            TextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                    callback(it.text)
                },
                modifier = Modifier.border(
                    BorderStroke(width = 2.dp, color = Purple500),
                    shape = RoundedCornerShape(50)
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(painterResource(R.drawable.ic_search), "search")
                }
            )
        }
    }
}