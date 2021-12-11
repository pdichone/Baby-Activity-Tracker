package com.bawp.babytrackerapp.screens

import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.navigation.BabyScreens
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Column
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
                OvershootInterpolator(8f).getInterpolation(it)
            })
                       )

        delay(1000L)
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(BabyScreens.LoginScreen.name)
        }else {
            navController.navigate(BabyScreens.MainScreen.name)
        }

    })
    Column(Modifier.fillMaxHeight().fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
          ) {
        Surface(
            modifier = Modifier.padding(15.dp).size(330.dp).scale(scale.value),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(
                width = 2.dp, color = Color.LightGray
                                 )
               ) {
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                  ) {
                Image(
                    painter = painterResource(id = R.drawable.baby_color),
                    contentDescription = "sunny icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(95.dp)
                     )
                Text(
                    text = "Baby Tracker",
                    style = MaterialTheme.typography.h5,
                    color = Color.LightGray
                    )
            }


        }
    }
}