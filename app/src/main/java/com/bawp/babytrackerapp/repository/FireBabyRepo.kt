package com.bawp.babytrackerapp.repository

import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.model.Baby
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBabyRepo @Inject constructor(  private val babyQuery: Query) {

    suspend fun getAllBabies(): DataOrException<List<Baby>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Baby>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  babyQuery.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Baby::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }
}
