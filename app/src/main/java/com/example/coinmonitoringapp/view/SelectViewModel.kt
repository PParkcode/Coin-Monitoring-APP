package com.example.coinmonitoringapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmonitoringapp.repository.NetworkRepository
import kotlinx.coroutines.launch
import timber.log.Timber

// SelectActivity에서 사용

class SelectViewModel: ViewModel() {

    private val networkRepository = NetworkRepository()
    fun getCurrentCoinList() = viewModelScope.launch {

        val result= networkRepository.getCurrentCoinList()
        //networkRepository.getCurrentCoinList() -> client.getCurrentCoinList()
                                                //         -> Api.getCurrentCoinList()  순 호출
        Timber.d(result.toString())
    }

}