package com.example.efisherypricelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getParcelableExtra<Fish>("data")!!

        setContent {
            EfisheryPriceListTheme {
                DetailView(data)
            }
        }
    }
}