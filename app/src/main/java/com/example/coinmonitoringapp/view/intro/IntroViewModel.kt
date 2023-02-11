package com.example.coinmonitoringapp.view.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmonitoringapp.dataStore.MyDataStore
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {

    private val _first = MutableLiveData<Boolean>()
    val first: LiveData<Boolean>
        get() = _first

    fun checkFirstFlag() = viewModelScope.launch {
        val getData = MyDataStore().getFirstData()
        _first.value= getData
    }
}