package com.bawp.babytrackerapp.screens.settings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bawp.babytrackerapp.R
import com.bawp.babytrackerapp.components.EmailInput
import com.bawp.babytrackerapp.components.MainAppBar
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun BabySettingsScreen(navController: NavController) {

val firestore = FirebaseFirestore.getInstance().collection("babies")
    val storage = FirebaseStorage.getInstance().reference

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
           // RequestContentPermission()

          UserForm(){ name, date, timestamp, imageUri ->
              var photoUrl = ""

              Log.d("TAG", "BabySettingsScreen: $name $date $timestamp $imageUri")
              val imageRef = storage.child("images/"+ FirebaseAuth.getInstance().currentUser!!.uid
                                          + "/" +imageUri.lastPathSegment)
              val uploadTask = imageRef.putFile(imageUri)
              uploadTask.addOnSuccessListener {
                  val downloadUrl = imageRef.downloadUrl
                  downloadUrl.addOnSuccessListener {
                      photoUrl = it.toString()
                      //update our Cloud Firestore with the public image URI
                      FirebaseFirestore.getInstance().collection("babies")
                  .document("shw7hvzJLcJZkQnW3HBP")//for now - later we might need to change this
                  .update(
                      hashMapOf("name" to name, "dob" to timestamp, "pic" to photoUrl) as Map<String, Any>
                      ).addOnSuccessListener {
                              navController.navigate(BabyScreens.MainScreen.name)
                          }


                  }

              }

          }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String, Timestamp, Uri) -> Unit = { name, dob, timeStamp, imageUri ->}
            ) {

    val year: Int
    val month: Int
    val day: Int
    val mHour: Int
    val mMinute: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    // Get Current Time

    mHour = calendar.get(Calendar.HOUR_OF_DAY);
    mMinute = calendar.get(Calendar.MINUTE);

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

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

   // val dialogState = rememberMaterialDialogState()


    val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
        Log.d("TAG", "UserForm: $hourOfDay $minute")

    }, mHour, mMinute, false)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "${month+1}/$dayOfMonth/$year"


            calendar.set(year , month, dayOfMonth)
            calendar.time
               //Date(year, month, dayOfMonth)
            dateState.value = Timestamp(calendar.time)

            Log.d("STX", "UserForm: ${dateState.value}")
            Log.d("STX", "DAte: ${date.value}")
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
                .size(100.dp)
                .clickable {
                    //add baby photo
                    launcher.launch("image/*")
                },
            color = Color(0xFDD6D1D6)
               ) {



            imageUri?.let {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)

                bitmap.value?.let {  btm ->

                    Image(bitmap = btm.asImageBitmap(),
                        contentDescription =null,
                        modifier = Modifier.size(100.dp))
                }
            }
//            Image(
//                painter = painterResource(id = R.drawable.baby_boy),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//                    .padding(6.dp)
//                 )
        }
        EmailInput(
            emailState = name,
            labelId = "Baby's Name",
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(name.value, date.value, dateState.value, imageUri!!)

            },)


        Surface(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .padding(10.dp)
                .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                .clickable {
                    //timePickerDialog.show()
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
       // val dialogState = rememberMaterialDialogState()

        Button(onClick = {
            onDone(name.value, date.value, dateState.value, imageUri!!)
//            FirebaseFirestore.getInstance().collection("babies")
//                .add(
//                    hashMapOf("name" to name.value,
//                              "dob" to dateState.value)
//                    )

            keyboardController?.hide()
        }) {
            Text(text = "Save")
        }

    }


}




