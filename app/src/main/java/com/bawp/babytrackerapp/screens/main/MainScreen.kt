package com.bawp.babytrackerapp.screens.main

import android.util.Log
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.model.Feed
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.bawp.babytrackerapp.model.Diaper
import okhttp3.internal.format
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun MainScreen(
    navController: NavController, viewModel: MainScreenViewModel
              ) {



    var listOfFeeds = emptyList<Feed>()
    var listOfDiapers = emptyList<Diaper>()

    val currentUser = FirebaseAuth.getInstance().currentUser
    var status by remember {
        mutableStateOf(false)
    }




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
        if (viewModel.data.value.data.isNullOrEmpty()) {
            CircularProgressIndicator()
            
            Text(text = "Nope")
            status = true

        } else if (!viewModel.data.value.data.isNullOrEmpty()) {
            viewModel.data.value.loading = false
            status = false

            listOfFeeds = viewModel.data.value.data!!.toList()

            Log.d("Feeds", "HomeContent: $listOfFeeds")
        }

        if (viewModel.diaperData.value.data.isNullOrEmpty()) {

        }else {
            listOfDiapers = viewModel.diaperData.value.data!!.toList()
            Log.d("Diapers", "HomeContent: $listOfDiapers")
        }

        Surface {

            val modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colors.background)
            //.verticalScroll(rememberScrollState())
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
                        Row {
                            Surface(
                                Modifier
                                    .clip(shape = CircleShape)
                                    .padding(6.dp)
                                    .clickable {
                                        //go to baby settings
                                        navController.navigate(BabyScreens.BabySettingsScreen.name)
                                    }, color = Color(0xFDD6D1D6)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                    Text(
                        text = "Recent Activity".uppercase(),
                        modifier = Modifier.alpha(alpha = 0.3f),
                        style = MaterialTheme.typography.h6,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                        )

                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                }

                if (status) {
                    //LinearProgressIndicator()
                } else {
                   // status = false
                    //Show all activities here
                    Column(Modifier.padding(6.dp), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        LazyColumn() {
                            items(items = listOfFeeds.toList()) { item: Feed ->
                                FeedCard(item)

                            }
                        }
                    }
                }


            }

        }

    }
}

@Composable
private fun FeedCard(item: Feed) {

    Row(Modifier) {
        Column(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxHeight()
              ) {
            Surface(
                Modifier.padding(5.dp), shape = CircleShape, color = Color(0xFFDDD3EC)
                   ) {
                Icon(
                    painter = painterResource(id = R.drawable.feeding_bottle),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                    )
            }
        }


        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            elevation = 5.dp
            ) {

            Column(
                Modifier.padding(5.dp), horizontalAlignment = Alignment.Start
                  ) {

                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss'Z'")
                val date = format(item.date!!)



                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = " ${date}, ${item.timeEntered}",
                        style = MaterialTheme.typography.caption
                        )
                    Icon(imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.clickable { })
                }



                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                   ) {
                    Text(text = "Feed")
                    Spacer(modifier = Modifier.width(3.dp))
                    Surface(
                        Modifier
                            .padding(5.dp)
                            .wrapContentWidth(),
                        shape = CircleShape,
                        color = Color(0xFFCFB2FB)
                           ) {
                        Text(
                            text = item.feed!!,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp)
                            )
                    }
                }

                Text(
                    text = "Amount: ${item.amount} ml", color = Color.DarkGray
                    )
                Text(text = "Type: ${item.foodType}", color = Color.DarkGray)

            }


        }
    }

}

