package com.bawp.babytrackerapp.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.bawp.babytrackerapp.navigation.BabyNavigation
import com.bawp.babytrackerapp.ui.theme.BabyTrackerAppTheme

@Composable
fun BabyApp( finishedActivity: () -> Unit = {} ) {
    BabyTrackerAppTheme {
        Scaffold(topBar = { TopAppBar(title = {},
            navigationIcon ={} )
        }) {
            BabyNavigation()

        }
    }

}