package com.example.coinmonitoringapp.repository

import com.example.coinmonitoringapp.App
import com.example.coinmonitoringapp.db.CoinPriceDatabase
import com.example.coinmonitoringapp.db.entity.InterestCoinEntity

class DBRepository {
    val context = App.context()
    val db= CoinPriceDatabase.getDatbase(context)

    //InterestCoin

    fun getAllInterestCoinData() = db.interestCoinDAO().getAllData()

    //코인 데이터 넣기
    fun insertInterestCoinData(interestCoinEntity: InterestCoinEntity) = db.interestCoinDAO().insert(interestCoinEntity)

    //코인 데이터 업데이트
    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) = db.interestCoinDAO().update(interestCoinEntity)

    //사용자가 관심있어한 코인만 가져오기
    fun getAllInterestSelectedCoinData() = db.interestCoinDAO().getSelectedData()
}