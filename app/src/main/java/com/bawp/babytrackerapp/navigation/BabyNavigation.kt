package com.bawp.babytrackerapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bawp.babytrackerapp.screens.main.MainScreen
import com.bawp.babytrackerapp.screens.SplashScreen
import com.bawp.babytrackerapp.screens.feed.FeedScreen
import com.bawp.babytrackerapp.screens.login.LoginScreen
import com.bawp.babytrackerapp.screens.menu.MainMenuScreen
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
            MainScreen(navController = navController)
        }

        composable(BabyScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(BabyScreens.MenuScreen.name) {
            MainMenuScreen(navController = navController)
        }

        composable(BabyScreens.BabySettingsScreen.name) {
            BabySettingsScreen(navController = navController)
        }
        composable(BabyScreens.FeedScreen.name) {
            FeedScreen(navController = navController)
        }

    }


}