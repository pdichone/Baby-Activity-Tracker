package com.bawp.babytrackerapp.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.ButtonChips
import com.bawp.babytrackerapp.components.ButtonChipsColors
import com.bawp.babytrackerapp.components.MainAppBar
import com.bawp.babytrackerapp.components.showToast
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.model.Diaper
import com.bawp.babytrackerapp.screens.feed.ShowBottleView
import com.bawp.babytrackerapp.screens.feed.ShowBreastView
import com.bawp.babytrackerapp.util.getTime
import com.bawp.babytrackerapp.util.saveActivityToFirebase
import com.bawp.babytrackerapp.util.saveDiaperToFirebase
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.util.*

@Composable
fun DiapersScreen(navController: NavController) {
    Scaffold(topBar = { MainAppBar(title = "Diapers",
        icon = Icons.Default.ArrowBack,
        showProfile = false,navController){
        navController.popBackStack()
         //navController.navigate(BabyScreens.MainScreen.name)
    }}) {

        val menuItems = listOf("Time", "Status")
        val selectedIndex = remember {
            mutableStateOf(0)
        }
        val isShowStatus = remember {
            mutableStateOf(false)
        }
        val dateEnteredState = remember {
            mutableStateOf("")
        }

        val timeEnteredState = remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier.padding(paddingValues = PaddingValues(4.dp)),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
               ) {
                Spacer(modifier = Modifier.weight(1f))
                ButtonChips(menuItems, selectedIndex)
                Spacer(modifier = Modifier.weight(1f))
            }
            //Show either Breast or Formula Views here depending on the selection
            when (selectedIndex.value) {
                0 -> ShowDiaperLogTimeView(navController = navController, isShowStatus,
                                          timeEnteredVal = timeEnteredState, dateEnteredVal = dateEnteredState)
                1 -> ShowDiaperStatusView(navController = navController,
                    timeEntered = timeEnteredState.value, dateEntered = dateEnteredState.value,
                    isShowStatus)
            }

        }


    }
}

@Composable
fun ShowDiaperStatusView(
    navController: NavController,
    timeEntered: String? = null,
    dateEntered: String? = null,
    isShowStatus: MutableState<Boolean>
                        ) {
    val statusList = listOf("Clean", "Poo", "Pee", "Mixed")
    val pooStatusColorList = listOf(
        Color(0xFF2C7D36),
        Color(0xFFF6D209),
        Color(0xFF925C34),
        Color(0xFF525352),
        Color(0xFF992518),
        Color(0xFFA5A7A5))



    val selectedIndex = remember {
        mutableStateOf(0)
    }
    val pooColorStatusIndex = remember {
        mutableStateOf(0)
    }
    val statusSelectedState = remember {
        mutableStateOf("")
    }
    val pooStatusSelectedState = remember {
        mutableStateOf("")
    }

    val isShowPoopColor = remember {
        mutableStateOf(false)
    }
    
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column() {
            Icon(imageVector = Icons.TwoTone.DateRange, contentDescription = null,
                modifier = Modifier.size(45.dp))
            Text(text = "Date: $dateEntered", style = MaterialTheme.typography.overline)
            Text(text = "Time $timeEntered", style = MaterialTheme.typography.overline)


        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
               ) {
                Spacer(modifier = Modifier.weight(1f))
                ButtonChips(statusList, selectedIndex,
                    borderColor = Color(0xFFFF8A65))
                Spacer(modifier = Modifier.weight(1f))
            }
            //Show either Breast or Formula Views here depending on the selection
            when (selectedIndex.value) {
                0 -> {statusSelectedState.value = "Clean"
                  isShowPoopColor.value = false
                }
                1 -> {
                    statusSelectedState.value = "Poo"
                    //show color picker!
                    isShowPoopColor.value = true
                }
                2 -> {statusSelectedState.value = "Pee"
                    isShowPoopColor.value = false
                }
                3 -> {statusSelectedState.value = "Mixed"
                    isShowPoopColor.value = true}


            }

            if (isShowPoopColor.value) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                   ) {
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonChipsColors(items = pooStatusColorList, pooColorStatusIndex,
                        borderColor = Color(0xFFFF8A65))
                    Spacer(modifier = Modifier.weight(1f))
                }
                when (pooColorStatusIndex.value) {
                   0 -> pooStatusSelectedState.value = "0xFF2C7D36"
                   1 -> pooStatusSelectedState.value = "0xFFF6D209"
                    2 -> pooStatusSelectedState.value = "0xFF925C34"
                    3 -> pooStatusSelectedState.value = "0xFF525352"
                    4 -> pooStatusSelectedState.value = "0xFF992518"
                    5 -> pooStatusSelectedState.value = "0xFFA5A7A5"

                }

            }


        }



        OutlinedButton(onClick = {
            if (dateEntered.isNullOrBlank() || timeEntered.isNullOrEmpty()){
                //show message! No go!

            }else {
                val diaper = Activity(
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    babyId = "IdHoPLrao8NpyWKS0d1G", //for now
                    activityType = "Diaper",
                    date = dateEntered ,
                    timeEntered = timeEntered,
                    status = statusSelectedState.value, //clean, poo, pee, mixed
                    color = pooStatusSelectedState.value,) //colors
                saveActivityToFirebase(diaper, navController)

            }


        }, shape = CircleShape) {

            Text(text = "Save")
        }

        
    }


}

@Composable
fun ShowDiaperLogTimeView(
    navController: NavController,
    isShowStatus: MutableState<Boolean>,
    dateEnteredVal: MutableState<String>,
    timeEnteredVal: MutableState<String>

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
            date.value = "${mMonth+1}/$mDay/$mYear"

            dateEnteredVal.value = "${mMonth+1}/$mDay/$year"
            calendar.set(year , month, day)


            Log.d("ToShow", "ShowLogTimeView: ${dateToShow.value}")

            //Date(year, month, dayOfMonth)
            dateState.value = Date(calendar.timeInMillis)

        }, year, month, day)


    val timePickerDialog = TimePickerDialog(
        context, { _, hourOfDay, minute ->
            // time.value = "$hourOfDay:$minute"
            time.value = getTime(hr = hourOfDay, min = minute).toString()
            timeEnteredVal.value = getTime(hr = hourOfDay, min = minute).toString()

            Log.d("TAG", "ShowLogTimeView: $hourOfDay:$minute")
        }, mHour, mMinute, false)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
          ) {

        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { datePickerDialog.show() }, shape = CircleShape) {
                Text(text = "Date")

            }


            OutlinedButton(onClick = { timePickerDialog.show()}, shape = CircleShape) {
                Text(text = "Time")

            }
        }

        Column(modifier = Modifier.padding(5.dp)) {

            Text(text = "Date: ${dateToShow.value}", style = MaterialTheme.typography.body1)
            Text(text = "Time: ${time.value}",style = MaterialTheme.typography.body1)
        }



    }



}
