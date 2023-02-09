package com.example.coinmonitoringapp.repository

import com.example.coinmonitoringapp.network.Api
import com.example.coinmonitoringapp.network.RetrofitInstance
import retrofit2.Retrofit

class NetworkRepository {
    //Retrofit Instance 가져오기
    private val client =RetrofitInstance.getInstance().create(Api::class.java)

    suspend fun getCurrentCoinList() = client.getCurrentCoinList()
}