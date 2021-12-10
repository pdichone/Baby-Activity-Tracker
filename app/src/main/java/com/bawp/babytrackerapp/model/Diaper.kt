package com.bawp.babytrackerapp.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Diaper(
    @Exclude var id: String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("baby_id")
    @set:PropertyName("baby_id")
    var babyId: String? = null,

    @get:PropertyName("activity_type")
    @set:PropertyName("activity_type")
    var activityType: String? = null, //always Diaper

    var date: String? = null,

    @get:PropertyName("time_entered")
    @set:PropertyName("time_entered")
    var timeEntered: String? = null ,

    var status: String? = null, //poop, pee, clean, Pee, Mixed
    var color: String? = null // gray, yellow,brown, black, red, green
                 )
