package com.bawp.babytrackerapp.screens.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.components.MainAppBar
import kotlin.random.Random
import androidx.compose.ui.res.painterResource
import com.bawp.babytrackerapp.R

@ExperimentalFoundationApi
@Composable
fun MainMenuScreen(navController: NavController) {
    Scaffold(topBar = {
        MainAppBar(
            title = "Menu", icon = Icons.Default.ArrowBack, false, navController = navController
                  ) {
            navController.popBackStack()
        }
    }) {

        val data = listOf(
            MenuItems(icon = painterResource(id = R.drawable.feeding_bottle),
                                   title = "Feed"),
            MenuItems(icon = painterResource(id = R.drawable.diaper),
                title = "Diaper"),
            MenuItems(icon = painterResource(id = R.drawable.breast_pump),
                title = "Pumping"),
            MenuItems(icon = painterResource(id = R.drawable.sleeping),
                title = "Sleep")
                         )
//source: https://alexzh.com/jetpack-compose-building-grids/
        ShowMenuGrid(data) {

        }

    }
}

@ExperimentalFoundationApi
@Composable
private fun ShowMenuGrid(data: List<MenuItems>,
                        onItemClicked: (String) -> Unit = {}) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)) {

        items(items = data) { item ->

            Card(
                modifier = Modifier.padding(12.dp).clickable {
                    onItemClicked.invoke(item.title)
                },
                backgroundColor = Color(0xFFEF9A9A),
                elevation = 4.dp,
                ) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                    Image(
                        painter = item.icon, contentDescription = null, modifier = Modifier.size(35.dp)
                         )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.subtitle2,
                        )
                }
            }

        }


    }
}

data class MenuItems(val icon: Painter, val title: String)