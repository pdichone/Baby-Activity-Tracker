package com.bawp.babytrackerapp.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.bawp.babytrackerapp.model.Diaper
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.model.Food
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat


fun saveDiaperToFirebase(diaper: Diaper, navController: NavController, home: (navController: NavController) -> Unit = {}) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("diapers")

    if (diaper.toString().isNotEmpty()){
        dbCollection.add(diaper)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //home(navController)
                            navController.navigate(BabyScreens.MainScreen.name)
                        }

                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating doc",it)
                    }

            }


    }else {
        return
    }


}

fun saveActivityToFirebase(activity: Activity,
                           navController: NavController,
                           home: (navController: NavController) -> Unit = {}) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("activities")

    if (activity.toString().isNotEmpty()){
        dbCollection.add(activity)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //home(navController)
                            navController.navigate(BabyScreens.MainScreen.name)
                        }

                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating doc",it)
                    }

            }


    }else {
        return
    }


}
object HexToJetpackColor {
    fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor("#$colorString"))
    }}
fun saveToFirebase(feed: Food, navController: NavController, home: (navController: NavController) -> Unit = {}) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("feeds")

    if (feed.toString().isNotEmpty()){
        dbCollection.add(feed)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //home(navController)
                            navController.navigate(BabyScreens.MainScreen.name)
                        }

                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating doc",it)
                    }

            }


    }else {
        return
    }


}

fun getTime(hr: Int, min: Int): String? {
    val tme = Time(hr, min, 0) //seconds by default set to zero
    val formatter: Format
    formatter = SimpleDateFormat("h:mm a")
    return formatter.format(tme)
}