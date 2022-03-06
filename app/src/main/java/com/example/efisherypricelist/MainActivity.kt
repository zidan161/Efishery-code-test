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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme
import com.example.efisherypricelist.ui.theme.Purple500

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: FishViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = FishViewModel(MainRepository(application))

        val data = mutableListOf<Fish>()
        viewModel.getPrices().observe(this@MainActivity) {
            data.addAll(it)
        }

        setContent {
            EfisheryPriceListTheme {
                Column {
                    Header()
                    val listData by viewModel.getPrices().observeAsState(initial = emptyList())
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
fun ItemData(fish: Fish) {
    Text(fish.name)
}

@Composable
fun Header() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Image(painterResource(R.drawable.logo_efishery), "logo efishery", modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp))
        Card (modifier = Modifier.padding(10.dp)) {
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EfisheryPriceListTheme {
        Header()
    }
}