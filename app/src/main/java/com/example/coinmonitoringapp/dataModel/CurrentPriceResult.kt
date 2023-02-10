package com.example.coinmonitoringapp.dataModel

//받아온 데이터를 가공하여 저장한 data class
data class CurrentPriceResult(
    val coinName: String,
    val coinInfo: CurrentPrice
)