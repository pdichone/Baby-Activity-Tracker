package com.bawp.babytrackerapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.bawp.babytrackerapp.navigation.BabyNavigation
import com.bawp.babytrackerapp.ui.theme.BabyTrackerAppTheme

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
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