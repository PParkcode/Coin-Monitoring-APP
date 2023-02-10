package com.example.coinmonitoringapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmonitoringapp.dataModel.CurrentPrice
import com.example.coinmonitoringapp.dataModel.CurrentPriceResult
import com.example.coinmonitoringapp.repository.NetworkRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import timber.log.Timber

// SelectActivity에서 사용

class SelectViewModel: ViewModel() {

    private val networkRepository = NetworkRepository()

    private lateinit var currentPriceResultList :ArrayList<CurrentPriceResult>

    //데이터 변화를 관찰하는 LiveData
    private val _currentPriceResult=MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult: LiveData<List<CurrentPriceResult>>
        get()= _currentPriceResult
    fun getCurrentCoinList() = viewModelScope.launch {

        val result= networkRepository.getCurrentCoinList()
        //networkRepository.getCurrentCoinList() -> client.getCurrentCoinList() -> Api.getCurrentCoinList()  순 호출

        currentPriceResultList = ArrayList()

        //코인 이름에 맞는 데이터를 추출하고 코인 이름을 추출하여 CurrentPriceResult로 가공하기 위한 반복문
        for(coin in result.data){

            try{
                val gson= Gson()
                val gsonToJson=gson.toJson(result.data.get(coin.key))  //여기서 coin.key는 코인 이름

                //gsonToJson 이라는 Json 데이터를 currentPrince 클래스에 맞게 변화하고 gsonFromJson에 저장하는 코드
                val gsonFromJson = gson.fromJson(gsonToJson, CurrentPrice::class.java)

                //currentPriceResult를 가공하여 저장
                val currentPriceResult = CurrentPriceResult(coin.key,gsonFromJson)

                currentPriceResultList.add(currentPriceResult)

            } catch(e:java.lang.Exception){
                Timber.d(e.toString())
            }

        }

        _currentPriceResult.value=currentPriceResultList
    }

}