package com.example.coinmonitoringapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coinmonitoringapp.db.entity.InterestCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestCoinDAO {
    //getAllData
    //Flow -> 데이터의 변경사항을 감지하기 좋다
    @Query("SELECT * FROM interest_coin_table")
    fun getAllData() : Flow<List<InterestCoinEntity>>

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(interestCoinEntity: InterestCoinEntity)

    //update
    @Update
    fun update(interestCoinEntity: InterestCoinEntity)

    //getSelectedCoinList -> 내가 관심있는 코인 데이터를 가져오는 것
    @Query("SELECT * FROM INTEREST_COIN_TABLE WHERE selected= :selected")
    fun getSelectedData(selected:Boolean = true) : List<InterestCoinEntity>
}