package com.example.efisherypricelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme

class AddDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EfisheryPriceListTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val name = remember { mutableStateOf(TextFieldValue()) }
                    val area = remember { mutableStateOf(TextFieldValue()) }
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it }
                    )
                    OutlinedTextField(
                        value = area.value,
                        onValueChange = { area.value = it }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    EfisheryPriceListTheme {
        Greeting("Android")
    }
}