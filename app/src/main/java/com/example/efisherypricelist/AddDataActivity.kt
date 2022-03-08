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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.ui.theme.EfisheryPriceListTheme

class AddDataActivity : ComponentActivity() {

    private lateinit var viewModel: AddDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AddDataViewModel(MainRepository(application))

        val area = mutableListOf<String>()
        val allSize = mutableListOf<String>()

        val liveArea = viewModel.getArea()
        val liveSize = viewModel.getSize()

        liveArea.observe(this) {
            for (i in it) {
                area.add("${i.city}, ${i.province}")
            }
        }

        liveSize.observe(this) {
            for (i in it) {
                allSize.add(i.size.toString())
            }
        }

        setContent {
            EfisheryPriceListTheme {
                Surface {
                    val network by remember { mutableStateOf(isNetworkAvailable()) }
                    if (!network) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(painterResource(R.drawable.ic_no_internet), "no_internet")
                            Text("No Internet Connection!")
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            var name by remember { mutableStateOf("") }
                            var price by remember { mutableStateOf("") }
                            var city by remember { mutableStateOf("") }
                            var province by remember { mutableStateOf("") }
                            var size by remember { mutableStateOf(0) }

                            Text("Add New Data +", fontSize = 25.sp, modifier = Modifier.fillMaxWidth())
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)) {
                                OutlinedButton(
                                    onClick = { finish() },
                                    modifier = Modifier.wrapContentWidth(),
                                    shape = RoundedCornerShape(10.dp)
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
                                    modifier = Modifier.wrapContentWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
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