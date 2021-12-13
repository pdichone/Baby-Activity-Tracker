package com.bawp.babytrackerapp.screens.menu

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.ShowMenuGrid
import com.bawp.babytrackerapp.model.Baby
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.bawp.babytrackerapp.screens.main.MainScreenViewModel

@ExperimentalFoundationApi
@Composable
fun MainMenuScreen(navController: NavController,
                   viewModel: MainScreenViewModel = hiltViewModel()) {
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
            MenuItems(icon = painterResource(id = R.drawable.notes),
                title = "Notes")
                         )
//source: https://alexzh.com/jetpack-compose-building-grids/
        ShowMenuGrid(data) {
            Log.d("TAG", "MainMenuScreen: $it")
             when (it) {
                  "Feed" ->   navController.navigate(BabyScreens.FeedScreen.name)
                  "Diaper" ->   navController.navigate(BabyScreens.DiapersScreen.name)
                  "Pumping" -> navController.navigate(BabyScreens.PumpingScreen.name)
                  "Notes" ->   navController.navigate(BabyScreens.NotesScreen.name)
             }

        }

    }
}



data class MenuItems(val icon: Painter, val title: String)