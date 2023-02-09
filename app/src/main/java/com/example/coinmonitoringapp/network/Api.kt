package com.example.coinmonitoringapp.network

import com.example.coinmonitoringapp.network.model.CurrentPriceList
import retrofit2.http.GET

interface Api {

    @GET("public/ticker/ALL_KRW")
    suspend fun getCurrentCoinList() :CurrentPriceList
}