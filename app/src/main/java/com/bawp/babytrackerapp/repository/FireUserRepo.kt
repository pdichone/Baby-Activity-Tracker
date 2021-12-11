package com.bawp.babytrackerapp.repository

import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.model.MUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireUserRepo @Inject constructor(private val userQuery: Query) {

    suspend fun getAllUsers(): DataOrException<List<MUser>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MUser>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  userQuery.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MUser::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }
}