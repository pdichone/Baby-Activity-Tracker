package com.bawp.babytrackerapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Activity(
    @Exclude var id: String? = null,

    @get:PropertyName("activity_type")
    @set:PropertyName("activity_type")
    var activityType: String? = null, //Diaper or Food....

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("baby_id")
    @set:PropertyName("baby_id")
    var babyId: String? = null,


    var date: String? = null,

    @get:PropertyName("time_entered")
    @set:PropertyName("time_entered")
    var timeEntered: String? = null,

    var status: String? = null, //poop, pee, clean, Pee, Mixed
    var color: String? = null, // gray, yellow,brown, black, red, green

    @get:PropertyName("food_type")
@set:PropertyName("food_type")
var foodType: String? = null, //breast milk or formula

var feed: String? = null, // Bottle, Breast
var amount: Int? = null,

    //Breast feeding Timer
    var duration: String? = null,

    @get:PropertyName("left_duration")
    @set:PropertyName("left_duration")
    var leftDuration: String? = null,

    @get:PropertyName("right_duration")
    @set:PropertyName("right_duration")
    var rightDuration: String? = null,

    @get:PropertyName("breast_entry_timestamp")
    @set:PropertyName("breast_entry_timestamp")
    var breastEntryTimeStamp: Timestamp? = null,


    var note: String? = null,

    @get:PropertyName("note_timestamp")
    @set:PropertyName("note_timestamp")
    var noteTimestamp: Timestamp? = null
                   )
