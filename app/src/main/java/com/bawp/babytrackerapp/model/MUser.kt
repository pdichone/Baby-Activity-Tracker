package com.bawp.babytrackerapp.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MUser(

    @Exclude var id: String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("display_name")
    @set:PropertyName("display_name")
    var displayName: String? = null,

    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String? = null,

    var quote: String? = null,
    var profession: String? = null){

//    fun toMap(): MutableMap<String, Any> {
//        return mutableMapOf("user_id" to this.userId,
//            "display_name" to this.displayName,
//            "quote" to this.quote,
//            "profession" to this.profession,
//            "avatar_url" to this.avatarUrl)
   // }

}
