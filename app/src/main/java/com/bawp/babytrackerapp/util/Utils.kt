package com.bawp.babytrackerapp.util

import android.util.Log
import androidx.navigation.NavController
import com.bawp.babytrackerapp.model.Feed
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.google.firebase.firestore.FirebaseFirestore

fun saveToFirebase(feed: Feed, navController: NavController, home: (navController: NavController) -> Unit = {}) {
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