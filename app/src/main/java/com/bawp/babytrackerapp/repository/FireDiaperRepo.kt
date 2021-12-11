package com.bawp.babytrackerapp.repository

import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Diaper
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

import javax.inject.Inject

class FireDiaperRepo @Inject constructor(
    private val fireDiaperQuery: Query
                                        ) {

    suspend fun getAllDiapers(): DataOrException<List<Diaper>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Diaper>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  fireDiaperQuery.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Diaper::class.java)!!
            }.asReversed()
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }
}