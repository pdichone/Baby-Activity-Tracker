package com.bawp.babytrackerapp.screens.settings

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.EmailInput
import com.bawp.babytrackerapp.components.MainAppBar
import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.text.DateFormat
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun BabySettingsScreen(navController: NavController) {


    Scaffold(topBar = {
        MainAppBar(title = "Settings",
            icon = Icons.Default.ArrowBack,
            false,
            navController = navController){
            navController.popBackStack()
        }
    }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
              ) {

          UserForm(){ name, date, timestamp ->
              Log.d("TAG", "BabySettingsScreen: $name $date $timestamp")

          }

        }



    }
}

@ExperimentalComposeUiApi
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String, Timestamp) -> Unit = { name, dob, timeStamp ->}
            ) {

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val context = LocalContext.current




    val name = rememberSaveable { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(name.value, date.value) {
        name.value.trim().isNotEmpty() && date.value.trim().isNotEmpty()

    }

    val dateState = remember {
        mutableStateOf(Timestamp(calendar.time))
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/$month/$year"

            val newDate =
                DateFormat.getDateInstance().parse(date.value.toString()) //NOT Working!
                //calendar.set(year + 1900, month, dayOfMonth)
                //DateTime.newBuilder().setYear(year).setMonth(month).setDay(dayOfMonth)
               // .build()

            dateState.value =  Timestamp(newDate)
            Log.d("STX", "UserForm: ${dateState.value}")
        }, year, month, day)

    val modifier = Modifier
        .height(300.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())


    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center) {
        Surface(
            Modifier
                .clip(shape = CircleShape)
                .padding(6.dp)
                .clickable {
                    //add baby photo
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
        EmailInput(
            emailState = name,
            labelId = "Baby's Name",
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(name.value, date.value, dateState.value)

            },
                  )


        Surface(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .padding(10.dp)
                .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                .clickable {
                    datePickerDialog.show()

                },
            shape = RectangleShape
               ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = MaterialTheme.colors.onPrimary
                    )
                Text(
                    text= if (date.value.isBlank()) "Baby's DOB" else date.value,
                    color = MaterialTheme.colors.onSurface)

            }

        }


        Button(onClick = { /*
           Save baby to db
           convert this date to Timestamp

        */
            onDone(name.value, date.value, dateState.value)
            keyboardController?.hide()
        }) {
            Text(text = "Save")
        }

    }


}

@Composable
fun ShowDatePicker(context: Context, onDateSelected: (Long) -> Unit = {}){

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()


    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/$month/$year"



           //Timestamp(calendar.set(year, month, dayOfMonth))
        }, year, month, day
                                           )

    Surface(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .padding(10.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable {
                datePickerDialog.show()


            },
        shape = RectangleShape
       ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
            horizontalArrangement = Arrangement.Start,
           verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                tint = MaterialTheme.colors.onPrimary
                )
            Text(
                text= if (date.value.isBlank()) "Baby's DOB" else date.value,
                color = MaterialTheme.colors.onSurface)


        }





    }



}


fun dateToTimestamp(date: Date?): Long {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.time = date
    return cal.timeInMillis / 1000L
}


