package com.bawp.babytrackerapp.di

import com.bawp.babytrackerapp.repository.*
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


    @Singleton
    @Provides
    fun provideFireActivityRepository()
            = FireActivityRepo(activityQuery = FirebaseFirestore.getInstance()
        .collection("activities"))


    @Singleton
    @Provides
    fun provideFireUserRepository()
            = FireUserRepo(userQuery = FirebaseFirestore.getInstance()
        .collection("users"))

    @Singleton
    @Provides
    fun provideFireBabiesRepository()
            = FireBabyRepo(babyQuery = FirebaseFirestore.getInstance()
        .collection("babies"))


}