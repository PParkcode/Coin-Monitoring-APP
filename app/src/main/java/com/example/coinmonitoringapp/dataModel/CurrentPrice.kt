package com.example.coinmonitoringapp.dataModel

//API 를 통해 받아온 데이터를 사용하기 쉽게 가공하기 위한 클래스
data class CurrentPrice(
    val opening_price: String,
    val closing_price: String,
    val min_price: String,
    val max_price: String,
    val units_traded: String,
    val acc_trade_value: String,
    val prev_closing_price: String,
    val units_traded_24H: String,
    val acc_trade_value_24H: String,
    val fluctate_24H: String,
    val fluctate_rate_24H: String
)