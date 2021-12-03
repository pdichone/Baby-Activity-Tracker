package com.bawp.babytrackerapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(topBar = {},  floatingActionButton = {}) {
        Surface() {
            Column() {
                Text("main Screen")

            }

        }

    }
}