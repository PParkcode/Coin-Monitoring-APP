package com.example.coinmonitoringapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.coinmonitoringapp.db.entity.InterestCoinEntity
import com.example.coinmonitoringapp.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val dbRepository = DBRepository()

    lateinit var selectedCoinList: LiveData<List<InterestCoinEntity>>

    //CoinListFragment에서 사용할 것것
    fun getAllInterestCoinData() = viewModelScope.launch {

        //coroutine에서 flow란?
        //coinList -> LiveData<List<InterestCoinEntity>>
        val coinList = dbRepository.getAllInterestCoinData().asLiveData()
        selectedCoinList = coinList

    }

    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) =
        viewModelScope.launch(Dispatchers.IO) {

            if (interestCoinEntity.selected) {
                interestCoinEntity.selected = false
            } else {
                interestCoinEntity.selected = true
            }

            dbRepository.updateInterestCoinData(interestCoinEntity)
        }

    //PriceChangeFragment에서 사용할 것
}