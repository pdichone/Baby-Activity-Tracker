package com.bawp.babytrackerapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bawp.babytrackerapp.screens.DiapersScreen
import com.bawp.babytrackerapp.screens.main.MainScreen
import com.bawp.babytrackerapp.screens.SplashScreen
import com.bawp.babytrackerapp.screens.feed.FeedScreen
import com.bawp.babytrackerapp.screens.login.LoginScreen
import com.bawp.babytrackerapp.screens.main.MainScreenViewModel
import com.bawp.babytrackerapp.screens.menu.MainMenuScreen
import com.bawp.babytrackerapp.screens.notes.NotesScreen
import com.bawp.babytrackerapp.screens.pumping.PumpingScreen
import com.bawp.babytrackerapp.screens.settings.BabySettingsScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun BabyNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = BabyScreens.SplashScreen.name ) {
        composable(BabyScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        //www.google.com/cityname="seattle"
        val mainRoute = BabyScreens.MainScreen.name
        composable(mainRoute) {
            val viewModel = hiltViewModel<MainScreenViewModel>()
            MainScreen(navController = navController, viewModel = viewModel)
        }

        composable(BabyScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(BabyScreens.MenuScreen.name) {
            val viewModel = hiltViewModel<MainScreenViewModel>()
            MainMenuScreen(navController = navController, viewModel = viewModel)
        }

        composable(BabyScreens.BabySettingsScreen.name) {
            BabySettingsScreen(navController = navController)
        }
        composable(BabyScreens.FeedScreen.name) {
            FeedScreen(navController = navController)
        }

        composable(BabyScreens.DiapersScreen.name) {
            DiapersScreen(navController = navController)
        }

        composable(BabyScreens.PumpingScreen.name) {
            PumpingScreen(navController = navController)
        }

        composable(BabyScreens.NotesScreen.name) {
            NotesScreen(navController = navController)
        }

    }


}