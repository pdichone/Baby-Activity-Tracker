package com.bawp.babytrackerapp.di

import com.bawp.babytrackerapp.repository.FireDiaperRepo
import com.bawp.babytrackerapp.repository.FireRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository()
            = FireRepository(
        queryFeed = FirebaseFirestore.getInstance()
    .collection("feeds"))


    @Singleton
    @Provides
    fun provideFireDiapersRepository()
            = FireDiaperRepo(fireDiaperQuery = FirebaseFirestore.getInstance()
        .collection("diapers"))


}