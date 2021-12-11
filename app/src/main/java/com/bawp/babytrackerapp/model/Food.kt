package com.bawp.babytrackerapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Food(
    @Exclude var id: String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("baby_id")
    @set:PropertyName("baby_id")
    var babyId: String? = null,

    @get:PropertyName("food_type")
    @set:PropertyName("food_type")
    var foodType: String? = null, //breast milk or formula

    var feed: String? = null, // Bottle, Breast


    var date: String? = null,
    var amount: Int? = null,

    @get:PropertyName("time_entered")
    @set:PropertyName("time_entered")
    var timeEntered: String? = null)



