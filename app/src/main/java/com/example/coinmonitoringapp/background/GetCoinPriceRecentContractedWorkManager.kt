package com.example.coinmonitoringapp.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coinmonitoringapp.db.entity.SelectedCoinPriceEntity
import com.example.coinmonitoringapp.network.model.RecentCoinPriceList
import com.example.coinmonitoringapp.repository.DBRepository
import com.example.coinmonitoringapp.repository.NetworkRepository
import java.util.*


// 최근 거래된 코인 가격 내역을 가져오는 WorkManager

// 1.  관심있어하는 코인리스트를 가져와서
// 2. 관심있는 코인 각각의 가격 변동 정보를 가져와서
// 3. DB에 저장

class GetCoinPriceRecentContractedWorkManager(val context: Context, workerParameters: WorkerParameters)
    : CoroutineWorker(context, workerParameters){

    private val dbRepository = DBRepository()
    private val networkRepository = NetworkRepository()
    override suspend fun doWork(): Result {

        getAllInterestSelectedCoinData()

        return Result.success()
    }

    // 1.  관심있어하는 코인 리스트를 가져와서
    suspend fun getAllInterestSelectedCoinData() {
        val selectedCoinList = dbRepository.getAllInterestSelectedCoinData()

        val timeStamp = Calendar.getInstance().time

        for(coinData in selectedCoinList){
            // 2. 관심있는 코인 각각의 가격 변동 정보를 가져와서
            val recentCoinPriceList = networkRepository.getInterestCoinPriceData(coinData.coin_name)

            saveSelectedCoinPrice(
                coinData.coin_name,
                recentCoinPriceList,
                timeStamp
            )
        }
    }




    // 3. DB에 저장

    fun saveSelectedCoinPrice(
        coinName:String,
        recentCoinPriceList: RecentCoinPriceList,
        timeStamp: Date
    ){
        val selectedCoinPriceEntity = SelectedCoinPriceEntity(
            0,
            coinName,
            recentCoinPriceList.data[0].transaction_date,
            recentCoinPriceList.data[0].type,
            recentCoinPriceList.data[0].units_traded,
            recentCoinPriceList.data[0].price,
            recentCoinPriceList.data[0].total,
            timeStamp
        )

        dbRepository.insertCoinPriceData(selectedCoinPriceEntity)
    }

}