package com.example.efisherypricelist

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.efisherypricelist.model.Fish

@Composable
fun DropdownSort(filter: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Name", "Price", "Size")
    var selectedText by remember { mutableStateOf("Sort By") }
    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .padding(20.dp, 0.dp),
        horizontalAlignment = Alignment.End
    ) {
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
fun ItemData(fish: Fish, activity: Activity) {
    //Untuk mencegah beberapa data yang terlalu panjang namanya
    if (fish.name.length < 30) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra("data", fish)
                    activity.startActivity(intent)
                }
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    fish.name,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    "${fish.city}, ${fish.province}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                )
                Text(
                    fish.price.toRupiah(),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun Header(search: (String) -> Unit) {
    val textState = remember { mutableStateOf("") }
    val view = LocalView.current
    val onValueChange: (String) -> Unit = {
        textState.value = it
        search(textState.value)
    }
    Box(modifier = Modifier.height(200.dp)) {
        Surface(
            color = Color(77, 199, 160),
            modifier = Modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 30.dp)
        ) {
            Image(
                painterResource(R.drawable.logo_efishery),
                "logo efishery",
                modifier = Modifier.padding(70.dp)
            )
        }
        Card(
            modifier = Modifier.padding(10.dp).align(Alignment.BottomCenter),
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
                label = { Text("Search") },
                trailingIcon = {
                    if (textState.value.isEmpty()) {
                        Icon(painterResource(R.drawable.ic_search), "search")
                    } else {
                        IconButton(onClick = {
                            onValueChange("")
                            view.clearFocus()
                        }) {
                            Icon(painterResource(R.drawable.ic_clear), "clear")
                        }
                    }
                },
                keyboardActions = KeyboardActions(onDone = {
                    view.clearFocus()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )
        }
    }
}

@Composable
fun ExposedTextField(
    data: List<String>,
    modifier: Modifier,
    label: String,
    selected: (Int) -> Unit
) {
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded) painterResource(R.drawable.ic_arrow_drop_up)
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

@Composable
fun TextOnList(name: String, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("$name:", color = Color.Gray)
        Text(text)
    }
}