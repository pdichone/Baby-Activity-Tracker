package com.bawp.babytrackerapp.screens.feed

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.ButtonChips
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.util.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.lang.Math.PI
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@ExperimentalComposeUiApi
@Composable
fun FeedScreen(navController: NavController) {
    Scaffold(topBar = {
        MainAppBar(
            title = "Feed", icon = Icons.Default.ArrowBack, false, navController = navController
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
                        OutlinedButton(onClick = {
                            //hide Bottle and breast milk formulate buttons and show time screen
                            isShowLog.value = !isShowLog.value

                        }, shape = CircleShape) {


                            Icon(
                                imageVector = Icons.Default.ArrowForward, contentDescription = null
                                )


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
fun ShowLogTimeView(
    navController: NavController, milkAmount: Int, feed: String = "", foodType: String = ""
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
            OutlinedButton(onClick = {
                timePickerDialog.show()
                                     },
                shape = CircleShape) {
                Text(text = "Time")
            }
        }


    }

    OutlinedButton(onClick = {

        val mFeed = Activity(
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            babyId = "IdHoPLrao8NpyWKS0d1G", //for now
            foodType = foodType,
            amount = milkAmount,
            date = date.value,
            feed = feed,
            timeEntered = time.value,
            activityType = "Feed")
        saveActivityToFirebase(mFeed, navController)

    }, shape = CircleShape) {
        Text(text = "Save")

    }

}



@ExperimentalComposeUiApi
@Composable
fun VerticalSlider(progressValue: Int? = null,
                   value: (Int) -> Unit) {

    val state = rememberComposeVerticalSliderState()

    ComposeVerticalSlider(state = state,
        enabled = state.isEnabled.value,
        progressValue = progressValue,
        trackColor = Color(0xFFB5B7B7),
        progressTrackColor = Color(0xFF848282),
        onProgressChanged = {
            // Log.d("TAG", "VerticalSlider: ${it * 3}")
            value(it * 3) // for mL mult by 3, for oz div by 10.0
        },
        onStopTrackingTouch = {
            value(it * 3)
        })
}

@Composable
fun ShowBreastView(
    navController: NavController, isShowLog: MutableState<Boolean>
                  ) {

    Surface() {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = CenterHorizontally
              ) {
            val selectedIndex = remember {
                mutableStateOf(0)
            }
            var leftBreastTimeState by remember {
                mutableStateOf(0L)
            }
            var rightBreastTimeState by remember {
                mutableStateOf(0L)
            }
            var totalTimerState by remember {
                mutableStateOf(0L)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 58.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
               ) {
                //left
                Timer(
                    //totalTime = 0L * 1000L,
                    timerLabel = "Left",
                    totalTime = 1L * 1000L,
                    handleColor = Color.White,
                    inactiveBarColor = Color.DarkGray,
                    activeBarColor = Color(0xFF7C7E7B),
                    modifier = Modifier.size(140.dp)
                     ) {
                    leftBreastTimeState = it
                   // Log.d("Time", "ShowBreastView: $it")
                }

                //right
                Timer(
                    //totalTime = 0L * 1000L,
                    timerLabel = "Right",
                    totalTime = 1L * 1000L,
                    handleColor = Color.White,
                    inactiveBarColor = Color.DarkGray,
                    activeBarColor = Color(0xFF7C7E7B),
                    modifier = Modifier.size(140.dp)
                     ) {
                    rightBreastTimeState = it
                    Log.d("Time", "ShowBreastView: $it")
                }
            }
            totalTimerState = (leftBreastTimeState + rightBreastTimeState) //to get seconds

            if (totalTimerState > 0)Text(text = "Total time: ${totalTimerState/1000L}")


            Spacer(modifier = Modifier.height(30.dp))
          if (totalTimerState > 0) {
              OutlinedButton(onClick = {
                  val date: DateFormat = SimpleDateFormat("MMM dd yyyy, h:mm a")
                  val dateFormatted: String = date.format(Calendar.getInstance().time).split(",")[1]

                  val mDate: DateFormat = SimpleDateFormat("MMM/dd/yyyy, h:mm a")

                  val dateFormattedSecond: String = mDate.format(Calendar.getInstance().time).split(",")[0]
                  Log.d("TAG", "ShowBreastView: $dateFormattedSecond")
                  val month: String = when(dateFormattedSecond.split("/")[0]) {
                      "Dec" -> "12"
                      "Nov" -> "11"
                      "Oct" -> "10"
                      "Sep" -> "09"
                      "Aug" -> "08"
                      "Jul" -> "07"
                      "Jun" -> "06"
                      "May" -> "05"
                      "Apr" -> "04"
                      "Mar" -> "03"
                      "Feb" -> "02"
                      "Jan" -> "01"
                      else -> { ""}
                  }
                  val day = dateFormattedSecond.split("/")[1]
                  val year = dateFormattedSecond.split("/")[2]
                  val finalDate = "$month/$day/$year"


                  val breast = Activity(
                      userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                      babyId = "IdHoPLrao8NpyWKS0d1G", //for now
                      activityType = "Breast",
                      duration = totalTimerState.toString(),
                      leftDuration = leftBreastTimeState.toString(),
                      rightDuration = rightBreastTimeState.toString(),
                      date = finalDate,
                      //SimpleDateFormat("MM/dd/yyyy").format(Date.from(Instant.now())),
                      timeEntered = dateFormatted,
                                       ) //colors
                  saveActivityToFirebase(breast, navController)


              }, shape = CircleShape) {
                  Text(text = "Save")

              }
          }else Box() {}

        }

    }

}

@Composable
fun Timer(
    timerLabel: String = "",
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
    onStop: (Long) -> Unit = {}
         ) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(initialValue)
    }
    var currentTime by remember {
        mutableStateOf(0L)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime += 100L
            // currentTime -= 100L
            //value = currentTime / totalTime.toFloat()
            value = currentTime.toFloat()
        }
    }
    Box(contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged {
            size = it
        }) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f, //250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                   )
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                   )
            val center = Offset(size.width / 2f, size.height / 2f)
            //val beta = (360f * value + 145f) * (PI / 180f).toFloat()
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
                      )
        }
        Column(
            modifier = Modifier.wrapContentSize(align = Center),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = (currentTime / 1000L).toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
                )
            Text(
                text = "secs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
                )

            Button(
                onClick = {
                    onStop(currentTime)
                    if (currentTime <= 0L) {
                        currentTime = totalTime
                        isTimerRunning = true
                    } else {
                        isTimerRunning = !isTimerRunning
                    }
                },
                //modifier = Modifier.align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                        Color.LightGray
                    } else {
                        Color.Red
                    }
                                                    )
                  ) {
                Text(
                    text = if (isTimerRunning && currentTime >= 0L) "Stop"
                    else if (!isTimerRunning && currentTime >= 0L) "Start"
                    else "Restart"
                    )
            }
            Text(text = timerLabel,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                fontSize = 15.sp,
                color = Color.Black)
        }

    }
}


