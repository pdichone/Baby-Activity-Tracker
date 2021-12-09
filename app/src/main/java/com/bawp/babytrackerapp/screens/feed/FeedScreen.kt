package com.bawp.babytrackerapp.screens.feed

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.bawp.babytrackerapp.components.MainAppBar
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.bawp.babytrackerapp.util.ComposeVerticalSlider
import com.bawp.babytrackerapp.util.rememberComposeVerticalSliderState
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.model.Feed
import com.bawp.babytrackerapp.util.saveToFirebase
import com.google.firebase.auth.FirebaseAuth
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun FeedScreen(navController: NavController) {
    Scaffold(topBar = {
        MainAppBar(
            title = "Feed", icon = Icons.Default.ArrowBack,
            false, navController = navController
                  ) {
            navController.popBackStack()
        }
    }) {
        val context = LocalContext.current
        val cornerRadius = 8.dp
        val items = listOf("Breast", "Bottle")
        val selectedIndex = remember {
            mutableStateOf(0)
        }
        val isShowLog = remember {
            mutableStateOf(false)
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
               ) {
                Spacer(modifier = Modifier.weight(1f))
                ButtonChips(items, selectedIndex)
                Spacer(modifier = Modifier.weight(1f))
            }
            //Show either Breast or Formula Views here depending on the selection
            when (selectedIndex.value) {
                0 -> ShowBreastView(navController = navController, isShowLog)
                1 -> ShowBottleView(navController = navController, isShowLog = isShowLog)
            }


        }

    }
}

@ExperimentalComposeUiApi
@Composable
fun ShowBottleView(
    navController: NavController, isShowLog: MutableState<Boolean>,
                  ) {
    Surface() {
        val foodTypeList = listOf("Breast Milk", "Formula")

        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally
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

                ButtonChips(items = foodTypeList, selectedIndex)

            }
            Spacer(modifier = Modifier.height(8.dp))


            var sliderProgressValue by rememberSaveable { mutableStateOf(10) }
            if (!isShowLog.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = (18.dp)),
                    contentAlignment = TopCenter
                   ) {
                    Column(horizontalAlignment = CenterHorizontally) {
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

                        //Next button
                        Button(onClick = {
                            //hide Bottle and breast milk formulat buttons and show time screen
                            isShowLog.value = !isShowLog.value

                        }) {

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null
                                )
                            Text(text = "Next")

                        }

                    }
                }


            } else {
                ShowLogTimeView(
                    navController = navController,
                    milkAmount = sliderProgressValue,
                    foodType = foodTypeList[selectedIndex.value],
                    feed = "Bottle"
                               )
            }

        }

    }

}

@Composable
fun ShowLogTimeView(navController: NavController, milkAmount: Int,
                    feed: String = "",
                    foodType: String = ""
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

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

            dateToShow.value = "${mMonth+1}/$mDay/$mYear"
            date.value = "${month+1}$day$year"
            calendar.set(year , month, day)
            Log.d("ToShow", "ShowLogTimeView: ${dateToShow.value}")

            //Date(year, month, dayOfMonth)
            dateState.value = Date(calendar.timeInMillis)

        }, year, month, day)


    val timePickerDialog = TimePickerDialog(
        context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
           // time.value = "$hourOfDay:$minute"
            time.value = getTime(hr = hourOfDay, min = minute).toString()


            Log.d("TAG", "ShowLogTimeView: $hourOfDay:$minute")
        }, mHour, mMinute, false)


    Column(
        horizontalAlignment = CenterHorizontally, verticalArrangement = Arrangement.SpaceAround
          ) {
        Column(horizontalAlignment = CenterHorizontally) {
            Surface(
                modifier = Modifier
                    .padding(5.dp)
                    .size(49.dp),
                shape = CircleShape,
                color = Color.DarkGray,
                border = BorderStroke(1.dp, color = Color.LightGray)
                   ) {
                Icon(
                    painter = painterResource(id = R.drawable.feeding_bottle),
                    contentDescription = null,
                    tint = Color.LightGray,
                    )

            }
            Text(text = "Amount")
            Text(
                text = milkAmount.toString(), textAlign = TextAlign.Center
                )
        }
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { datePickerDialog.show() }) {
                Text(text = "Date")

            }

            Button(onClick = { timePickerDialog.show()}) {
                Text(text = "Time")

            }
        }

        Column(modifier = Modifier.padding(5.dp)) {

            Text(text = "Date: ${dateToShow.value}", style = MaterialTheme.typography.body1)
            Text(text = "Time: ${time.value}",style = MaterialTheme.typography.body1)
        }

    }

    Button(onClick = {
        /**
         * Save a Feed Object
         * we save amount, baby id, userId, time and date, type of feed
         */

        val mFeed = Feed(
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            babyId = "IdHoPLrao8NpyWKS0d1G", //for now
                       foodType = foodType,
            amount = milkAmount,
            date = date.value ,
            feed = feed,
            timeEntered = time.value)
      saveToFirebase(mFeed, navController)

    }) {
        Text(text = "Save")

    }

}

private fun getTodaysDate(): String? {
    val cal = Calendar.getInstance()
    val year = cal[Calendar.YEAR]
    var month = cal[Calendar.MONTH]
    month += 1
    val day = cal[Calendar.DAY_OF_MONTH]
    return "$month-$day-$year"
}
@ExperimentalComposeUiApi
@Composable
fun VerticalSlider(progressValue: Int? = null, value: (Int) -> Unit) {

    val state = rememberComposeVerticalSliderState()

    ComposeVerticalSlider(state = state,
        enabled = state.isEnabled.value,
        progressValue = progressValue,
        trackColor = Color(0xFFD4E157),
        progressTrackColor = Color(0xFFFF7043),
        onProgressChanged = {
            // Log.d("TAG", "VerticalSlider: ${it * 3}")
            value(it * 3) // for mL mult by 3, for oz div by 10.0
        },
        onStopTrackingTouch = {
            value(it * 3)
        })
}

@Composable
fun ShowBreastView(navController: NavController, isShowLog: MutableState<Boolean>) {

    Surface() {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = CenterHorizontally
              ) {
            val selectedIndex = remember {
                mutableStateOf(0)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 58.dp),
                horizontalArrangement = Arrangement.Center
               ) {
                Text(text = "Show Breast View")
            }
            //Todo: Show timer etc..
        }

    }

}

@Composable
private fun ButtonChips(items: List<String>, selectedIndex: MutableState<Int>) {
    var selectedIndex1 = selectedIndex.value
    items.forEachIndexed { index, item ->
        OutlinedButton(
            onClick = { selectedIndex.value = index },
            modifier = when (index) {
                0 -> {
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = 5.dp, color = Color(0xFF66BB6A), shape = CircleShape
                                   )
                    } else {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
                else -> {
                    val offset = -1 * index
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = 5.dp, color = Color(0xFF66BB6A), shape = CircleShape
                                   )


                    } else {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
            },

            )

        {
            Text(
                text = item, color = if (selectedIndex.value == index) {
                    MaterialTheme.colors.primary
                } else {
                    Color.DarkGray.copy(alpha = 0.9f)
                }, modifier = Modifier.padding(horizontal = 8.dp)
                )
        }
    }
}

private fun getTime(hr: Int, min: Int): String? {
    val tme = Time(hr, min, 0) //seconds by default set to zero
    val formatter: Format
    formatter = SimpleDateFormat("h:mm a")
    return formatter.format(tme)
}