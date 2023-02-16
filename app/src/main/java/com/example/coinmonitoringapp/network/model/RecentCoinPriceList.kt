package com.example.coinmonitoringapp.network.model

import com.example.coinmonitoringapp.dataModel.RecentPriceData

data class RecentCoinPriceList(

    val status: String,
    val data: List<RecentPriceData>

)