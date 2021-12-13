package com.bawp.babytrackerapp.screens.main

import android.util.Log
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
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.bawp.babytrackerapp.model.Baby
import com.bawp.babytrackerapp.model.Diaper
import com.bawp.babytrackerapp.model.MUser
import com.bawp.babytrackerapp.util.HexToJetpackColor
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.String.format
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun MainScreen(
    navController: NavController, viewModel: MainScreenViewModel
              ) {


var listOBabies = emptyList<Baby>()
var listOfUsers = emptyList<MUser>()
    var listOfActivities = emptyList<Activity>()
    var listOfDiapers = emptyList<Diaper>()

    val currentUser = FirebaseAuth.getInstance().currentUser
    var status by remember {
        mutableStateOf(false)
    }
    var babyObject by remember {
        mutableStateOf(Baby())
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


            if (viewModel.fireUserData.value.data.isNullOrEmpty()) {
                //do nothing
            }else {
                listOfUsers = viewModel.fireUserData.value.data!!.filter {
                     it.userId == currentUser!!.uid

                }
            }
        if (viewModel.data.value.data.isNullOrEmpty()) {
            CircularProgressIndicator()

            Text(text = "Nope")
            status = true

        } else if (!viewModel.data.value.data.isNullOrEmpty()) {
            viewModel.data.value.loading = false
            status = false

            listOfActivities = viewModel.data.value.data!!.toList()

            Log.d("Activities", "HomeContent: $listOfActivities")
        }

            if (viewModel.fireBabyData.value.data.isNullOrEmpty()) {
                //do nothing
            }else {
                listOBabies = viewModel.fireBabyData.value.data!!.toList()
                babyObject = listOBabies[0]
                Log.d("Babies", "MainScreen: $babyObject")
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
                                        navController.navigate(BabyScreens.BabySettingsScreen.name)
                                    }
                                   ) {

                                Image(painter = rememberImagePainter( babyObject.pic.toString(),
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    }),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                     )

                            }

//                            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss'Z'")
//                            val date = format("MM/dd/yyyy'T'HH:mm:ss'Z'", cal)
                            //val date: String =
                               // SimpleDateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
                           // Log.d("DOBx", "DOBx: ${ babyObject.dob}")
                            Column(modifier = Modifier.padding(4.dp),
                                  verticalArrangement = Arrangement.Center) {
                                Text(text = babyObject.name.toString().uppercase(locale = Locale.US),
                                    style = MaterialTheme.typography.h6)
                             if (babyObject.dob != null) Text(text = babyObject.dob!!.toDate().getTimeAgo(),
                                 style = MaterialTheme.typography.body2)
                            }

                        }

                    }

                }

                //Last 24 hours

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
                    Column(
                        Modifier.padding(6.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                          ) {

                        LazyColumn {
                            items(items = listOfActivities.toList()) { item: Activity ->
                                FeedCard(navController = navController, item, listOfUsers)

                            }
                        }
                    }
                }


            }

        }

    }
}

@Composable
private fun FeedCard(navController: NavController,item: Activity, listOfUsers: List<MUser>) {

    Row {
        Column(
            Modifier
                .height(IntrinsicSize.Min)

              ) {
            Surface(
                Modifier
                    .padding(5.dp)
                    .wrapContentSize(Alignment.Center),
                shape = CircleShape,
                color = Color(0xFFDDD3EC)
                   ) {
                Icon(
                    painter = painterResource(
                        id = when (item.activityType) {
                            "Feed" -> R.drawable.feeding_bottle
                            "Diaper" -> R.drawable.diaper_icon
                            "Pumping" -> R.drawable.breast_pump
                            "Notes" -> R.drawable.notes
                            else -> R.drawable.breastfeeding
                        }
                                             ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(3.dp)
                        .rotate(degrees = -14f)
                    )
            }
        }


        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            elevation = 5.dp
            ) {

            Column(
                Modifier.padding(5.dp), horizontalAlignment = Alignment.Start
                  ) {

                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss'Z'")
                val date = format(item.date!!)
                val openDialog = remember {
                    mutableStateOf(false)
                }
                CreateAlertDialog(navController = navController, openDialog = openDialog, item = item)



                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${date},${item.timeEntered}",
                        style = MaterialTheme.typography.caption
                        )
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            //first show dialog
                            openDialog.value = true

                        })
                }



                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                   ) {
                    Text(text = item.activityType.toString(),
                        color = Color(0xFF009688),
                        fontWeight = FontWeight.SemiBold
                        )
                    Spacer(modifier = Modifier.width(3.dp))
                    if (item.activityType == "Feed"
                        || item.activityType == "Breast" ||
                            item.activityType == "Pumping") Surface(
                        Modifier
                            .padding(5.dp)
                            .wrapContentWidth(),
                        shape = CircleShape,
                        color = Color(0xFFCFB2FB)) {
                        Text(
                            text = if (item.activityType == "Feed")item.feed!!
                            else item.activityType.toString() ,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp)
                            )
                    }
                }

                when (item.activityType) {
                    "Feed" -> {

                        Text(
                            text = "Amount: ${item.amount} ml", color = Color.DarkGray
                            )
                        Text(text = "Type: ${item.foodType}", color = Color.DarkGray)
                    }
                    "Diaper" -> {
                        //Diaper

                        Text(
                            text = "Status: ${item.status} ", color = Color.DarkGray
                            )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Color: ", color = Color.DarkGray
                                )
                            //fix: https://stackoverflow.com/questions/60247480/color-from-hex-string-in-jetpack-compose
                            val color = HexToJetpackColor.getColor(item.color.toString().split("x")[1])


                            Box(modifier = Modifier
                                .size(20.dp)
                                .background(color)) {

                            }
                        }

                    }
                    "Breast" -> {
                        //show breast timer stuff here
                        Text(
                            text = "Duration: ${showFormattedSeconds(item.duration!!.toLong())} ", color = Color.DarkGray
                            )

                        Text(
                            text = "Left: ${showFormattedSeconds(item.leftDuration!!.toLong())} ", color = Color.DarkGray
                            )
                        Text(
                            text = "Right: ${showFormattedSeconds(item.rightDuration!!.toLong())} ", color = Color.DarkGray
                            )


                    }
                    "Pumping" -> {

                        Text(
                            text = "Amount: ${item.amount} ml", color = Color.DarkGray
                            )
                        Text(text = "Type: ${item.activityType}", color = Color.DarkGray)
                    }

                    "Notes" -> {

                        Text(
                            text = " ${item.note}", color = Color.DarkGray,
                             softWrap = true
                            )
                        //Text(text = "Type: ${item.}", color = Color.DarkGray)
                    }


                }

Spacer(modifier = Modifier.height(10.dp))
                if (listOfUsers.isNotEmpty())Text(text = "By: ${listOfUsers[0].displayName}",
                                                 style = MaterialTheme.typography.caption,
                                                 fontStyle = FontStyle.Italic)
            }

        }
    }

}

@Composable
fun CreateAlertDialog(navController: NavController, openDialog: MutableState<Boolean>, item: Activity) {

    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Delete Activity")
            },
            text = {
                Text("Are you sure you want to delete? ")
            },
            confirmButton = {
                Button(
                    onClick = {
                        FirebaseFirestore.getInstance()
                            .collection("activities")
                            .document(item.id!!)
                            .delete().addOnSuccessListener {
                                openDialog.value = false
                                navController.navigate(BabyScreens.MainScreen.name)
                            }.addOnFailureListener {
                                openDialog.value = false
                                Log.d("Exception", "CreateAlertDialog: $it")
                            }
                    }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(

                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Cancel")
                }
            })
    }

}

//https://stackoverflow.com/questions/19667473/how-to-show-milliseconds-in-dayshoursminseconds
fun showFormattedSeconds(timeInMilliSeconds: Long): String {
    val seconds: Long = timeInMilliSeconds / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val time = days.toString() + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60
    return  "" + minutes % 60 + ":" + seconds % 60 + " sec"
}

fun Date.getTimeAgo(isAge: Boolean =  true): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    var word = if (isAge) "old" else "ago"

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val currentCalendar = Calendar.getInstance()

    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)

    return if (year < currentYear ) {
        val interval = currentYear - year
        if (interval == 1) "$interval year $word" else "$interval years $word"
    } else if (month < currentMonth) {
        val interval = currentMonth - month
        if (interval == 1) "$interval month $word" else "$interval months $word"
    } else  if (day < currentDay) {
        val interval = currentDay - day
        if (interval == 1) "$interval day $word" else "$interval days $word"
    } else if (hour < currentHour) {
        val interval = currentHour - hour
        if (interval == 1) "$interval hour $word" else "$interval hours $word"
    } else if (minute < currentMinute) {
        val interval = currentMinute - minute
        if (interval == 1) "$interval minute $word" else "$interval minutes $word"
    } else {
        "a moment $word"
    }
}
