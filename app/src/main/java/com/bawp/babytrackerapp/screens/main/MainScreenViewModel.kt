package com.bawp.babytrackerapp.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bawp.babytrackerapp.data.DataOrException
import com.bawp.babytrackerapp.model.Diaper
import com.bawp.babytrackerapp.model.Feed
import com.bawp.babytrackerapp.repository.FireDiaperRepo
import com.bawp.babytrackerapp.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: FireRepository,
    private  val fireDiaperRepo: FireDiaperRepo): ViewModel() {
    val diaperData: MutableState<DataOrException<List<Diaper>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))
    val data: MutableState<DataOrException<List<Feed>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    init {
        getAllBooksFromDatabase()
        getAllDiapers()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllFeeds()
            if (!data.value.data.isNullOrEmpty()){
                data.value.loading = false

                Log.d("GET", "getAllBooksFromDatabase: ${data.value.loading}")
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