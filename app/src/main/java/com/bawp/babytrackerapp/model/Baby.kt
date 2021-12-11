package com.bawp.babytrackerapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Baby(

    var dob: Timestamp? = null,
    var name: String? = null,
    var pic: String? = null
               )
