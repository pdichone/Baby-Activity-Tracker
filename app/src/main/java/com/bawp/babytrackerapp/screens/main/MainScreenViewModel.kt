package com.bawp.babytrackerapp.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Diaper
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.model.Baby
import com.bawp.babytrackerapp.model.MUser
import com.bawp.babytrackerapp.repository.FireActivityRepo
import com.bawp.babytrackerapp.repository.FireBabyRepo
import com.bawp.babytrackerapp.repository.FireDiaperRepo
import com.bawp.babytrackerapp.repository.FireUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: FireActivityRepo,
    private val fireUserRepo: FireUserRepo,
    private val babyRepo: FireBabyRepo,
    private  val fireDiaperRepo: FireDiaperRepo): ViewModel() {
    private val diaperData: MutableState<DataOrException<List<Diaper>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))
    val data: MutableState<DataOrException<List<Activity>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    val fireUserData: MutableState<DataOrException<List<MUser>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    val fireBabyData: MutableState<DataOrException<List<Baby>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    init {
        getAllActivities()
       getAllBabies()
        getAllUsers()
    }

    private fun getAllBabies() {
        viewModelScope.launch {
            fireBabyData.value.loading = true
            fireBabyData.value = babyRepo.getAllBabies()
            if (!fireBabyData.value.data.isNullOrEmpty()){
                fireBabyData.value.loading = false

                Log.d("GET", "getAllActivities: ${fireBabyData.value.data}")
            }
        }

    }
    private fun getAllActivities() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllActivities()
            if (!data.value.data.isNullOrEmpty()){
                data.value.loading = false

                Log.d("GET", "getAllActivities: ${data.value.data}")
            }
        }

    }

    private fun getAllUsers() {
        viewModelScope.launch {
            fireUserData.value.loading = true
            fireUserData.value = fireUserRepo.getAllUsers()
            if (!fireUserData.value.data.isNullOrEmpty()){
                fireUserData.value.loading = false

                Log.d("GET", "getAllUsers: ${fireUserData.value.data}")
            }
        }

    }
    private fun getAllDiapers() {
        viewModelScope.launch {
            diaperData.value.loading = true
            diaperData.value = fireDiaperRepo.getAllDiapers()
            if (!diaperData.value.data.isNullOrEmpty()){
                diaperData.value.loading = false

                Log.d("GET", "DiapersFromDB: ${diaperData.value.data}")
            }
        }


    }

    }