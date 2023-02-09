package com.example.coinmonitoringapp.network.model

data class CurrentPriceList(
    val status :String,
    val data: Map<String,Any>
)