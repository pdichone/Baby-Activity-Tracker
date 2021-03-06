package com.bawp.babytrackerapp.repository

import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Activity
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor( private val queryFeed: Query){

    suspend fun getAllFeeds(): DataOrException<List<Activity>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Activity>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  queryFeed.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Activity::class.java)!!
            }.asReversed()
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }



}