package com.bawp.babytrackerapp.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.RoundedButton
import androidx.compose.material.Surface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Info
import com.bawp.babytrackerapp.navigation.BabyScreens

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Hello, Raf")
        }, navigationIcon = {

        }, actions = {}, backgroundColor = Color(0xFFffffff), elevation = 4.dp
                 )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                      navController.navigate(BabyScreens.MenuScreen.name)
            },
            shape = CircleShape.copy(CornerSize(36.dp)),
                            ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.padding(6.dp),
                tint = Color.White
                )

        }
    }) {
        Surface() {
            val modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colors.background)
                .verticalScroll(rememberScrollState())

            Column(
                modifier, horizontalAlignment = Alignment.Start
                  ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(6.dp),
                    shape = RectangleShape,
                    elevation = 4.dp,
                    border = BorderStroke(1.dp, Color.White)
                    ) {
                    Column(

                        verticalArrangement = Arrangement.SpaceAround,

                        ) {
                        Row() {
                            Surface(
                                Modifier
                                    .clip(shape = CircleShape)
                                    .padding(6.dp)
                                    .clickable {
                                               //go to baby settings
                                               navController.navigate(BabyScreens.BabySettingsScreen.name)
                                    },
                                color = Color(0xFDD6D1D6)
                                   ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baby_boy),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(45.dp)
                                        .padding(6.dp)
                                     )
                            }
                            Column(modifier = Modifier.padding(4.dp)) {
                                Text(text = "Rafael", style = MaterialTheme.typography.subtitle2)
                                Text(text = "2 Weeks", style = MaterialTheme.typography.caption)
                            }

                        }

                    }

                }

                //Last 24 hours

                Text(
                    text = "Last 24 Hours".uppercase(),
                    modifier = Modifier.alpha(alpha = 0.3f),
                    style = MaterialTheme.typography.h5,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                    )

                Row(modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Recent Activity".uppercase(),
                        modifier = Modifier.alpha(alpha = 0.3f),
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                        )

                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                }


            }

        }

    }
}