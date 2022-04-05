package com.example.efisherypricelist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme

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
                Box(modifier = Modifier.fillMaxHeight()) {
                    Column {
                        Header { viewModel.getPricesBySearch(it) }
                        DropdownSort { by ->
                            when (by) {
                                "Name" -> viewModel.sortPricesByName()
                                "Price" -> viewModel.sortPricesByPrice()
                                "Size" -> viewModel.sortPricesBySize()
                            }
                        }
                        val listData by viewModel.prices.observeAsState(listOf())
                        LazyColumn(
                            contentPadding = PaddingValues(20.dp, 10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(listData) { fish ->
                                ItemData(fish, this@MainActivity)
                            }
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                        onClick = {
                            val intent = Intent(this@MainActivity, AddDataActivity::class.java)
                            startActivity(intent)
                        }
                    ) { Icon(painterResource(R.drawable.ic_add), "add") }
                }
            }
        }
    }
}