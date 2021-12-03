package com.bawp.babytrackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bawp.babytrackerapp.screens.MainScreen
import com.bawp.babytrackerapp.screens.SplashScreen

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

    }


}