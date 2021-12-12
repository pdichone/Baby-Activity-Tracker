package com.bawp.babytrackerapp.screens.pumping

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.ButtonChips
import com.bawp.babytrackerapp.components.MainAppBar
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.screens.feed.ShowLogTimeView
import com.bawp.babytrackerapp.screens.feed.VerticalSlider
import com.bawp.babytrackerapp.util.getTime
import com.bawp.babytrackerapp.util.saveActivityToFirebase
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun PumpingScreen(navController: NavController) {
    Scaffold(topBar = {
        MainAppBar(
            title = "Pumping", icon = Icons.Default.ArrowBack, false,
            navController = navController
                  ) {
            navController.popBackStack()
        }
    }) {
        ShowPumpingScreen(navController = navController)
    }


}

@ExperimentalComposeUiApi
@Composable
fun ShowPumpingScreen(
    navController: NavController) {
    Surface() {

        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
              ) {
            val selectedIndex = remember {
                mutableStateOf(0)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 34.dp),
                horizontalArrangement = Arrangement.Center
               ) {

            }
            Spacer(modifier = Modifier.height(8.dp))

            var sliderProgressValue by rememberSaveable { mutableStateOf(10) }
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .padding(top = (18.dp)),
                 contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$sliderProgressValue ml",
                            textAlign = TextAlign.Center,
                            fontSize = 33.sp
                            )
                        Card(
                            modifier = Modifier.wrapContentSize(),
                            shape = RoundedCornerShape(30.dp),
                            elevation = 5.dp
                            ) {
                            VerticalSlider(progressValue = sliderProgressValue) {
                                sliderProgressValue = it
                            }

                        }
                        com.bawp.babytrackerapp.screens.pumping.ShowLogTimeView(
                            navController = navController,
                            milkAmount = sliderProgressValue)

                        //Next button
//                        OutlinedButton(onClick = {
//                            //hide Bottle and breast milk formulate buttons and show time screen
//
//                        }, shape = CircleShape) {
//
//                            Icon(
//                                imageVector = Icons.Default.ArrowForward, contentDescription = null
//                                )
//
//
//                        }

                    }
                }

        }

    }

}

@Composable
fun ShowLogTimeView(
    navController: NavController, milkAmount: Int,
    feed: String = "Pumping",
    foodType: String = "Pumping"
                   ) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val mHour = calendar[Calendar.HOUR_OF_DAY]
    val mMinute = calendar[Calendar.MINUTE]

    val dateState = remember {
        mutableStateOf(Date.from(Instant.now()))
    }
    val date = remember { mutableStateOf("") }
    val dateToShow = remember { mutableStateOf("") }

    val timeState = remember {
        mutableStateOf(Date.from(Instant.now()))
    }

    val time = remember { mutableStateOf("") }

    val context = LocalContext.current

    val datePickerDialog =
        DatePickerDialog(context, { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

            dateToShow.value = "${mMonth + 1}/$mDay/$mYear"
            date.value = "${mMonth + 1}/$mDay/$mYear"
            calendar.set(year, month, day)
            Log.d("ToShow", "ShowLogTimeView: ${dateToShow.value}")

            //Date(year, month, dayOfMonth)
            dateState.value = Date(calendar.timeInMillis)

        }, year, month, day)


    val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
        // time.value = "$hourOfDay:$minute"
        time.value = getTime(hr = hourOfDay, min = minute).toString()

        Log.d("TAG", "ShowLogTimeView: $hourOfDay:$minute")
    }, mHour, mMinute, false)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround
          ) {

        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
           ) {
            OutlinedButton(onClick = { datePickerDialog.show() }, shape = CircleShape) {
                Text(text = "Date")

            }

            Column(modifier = Modifier.padding(5.dp)) {

                Text(text = "Date: ${dateToShow.value}", style = MaterialTheme.typography.caption)
                Text(text = "Time: ${time.value}", style = MaterialTheme.typography.caption)
            }
            OutlinedButton(onClick = { timePickerDialog.show() }, shape = CircleShape) {
                Text(text = "Time")

            }
        }


    }

    OutlinedButton(onClick = {

        val pumping = Activity(
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            babyId = "IdHoPLrao8NpyWKS0d1G", //for now
            amount = milkAmount,
            date = date.value,
            timeEntered = time.value,
            activityType = "Pumping")
        saveActivityToFirebase(pumping, navController)

    }, shape = CircleShape) {
        Text(text = "Save")

    }

}
