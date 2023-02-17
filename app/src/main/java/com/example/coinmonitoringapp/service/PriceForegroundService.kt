package com.example.coinmonitoringapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.coinmonitoringapp.R
import com.example.coinmonitoringapp.dataModel.CurrentPrice
import com.example.coinmonitoringapp.dataModel.CurrentPriceResult
import com.example.coinmonitoringapp.network.model.CurrentPriceList
import com.example.coinmonitoringapp.repository.NetworkRepository
import com.example.coinmonitoringapp.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class PriceForegroundService : Service() {

    private val networkRepository = NetworkRepository()
    private val NOTIFICATION_ID = 10000
    lateinit var job:Job

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            "START" -> {
                job = CoroutineScope(Dispatchers.Default).launch {
                    while(true){
                        startForeground(NOTIFICATION_ID, makeNotification())
                        delay(3000)
                    }

                }

            }
            "STOP" -> {
                try{
                    job.cancel()
                    stopForeground(true)
                    stopSelf()
                } catch (e: java.lang.Exception){

                }

            }
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    suspend fun makeNotification(): Notification {

        val result = getAllCoinList()

        val randomNum = Random().nextInt(result.size)

        val title = result[randomNum].coinName
        val content = result[randomNum].coinInfo.fluctate_24H
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
            .setContentTitle("코인 이름 : ${title}")
            .setContentText("변동 가격 : ${content}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return builder.build()

    }

    suspend fun getAllCoinList(): ArrayList<CurrentPriceResult> {

        val result = networkRepository.getCurrentCoinList()
        //networkRepository.getCurrentCoinList() -> client.getCurrentCoinList() -> Api.getCurrentCoinList()  순 호출

        val currentPriceResultList = ArrayList<CurrentPriceResult>()

        //코인 이름에 맞는 데이터를 추출하고 코인 이름을 추출하여 CurrentPriceResult로 가공하기 위한 반복문
        for (coin in result.data) {

            try {
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))  //여기서 coin.key는 코인 이름

                //gsonToJson 이라는 Json 데이터를 currentPrince 클래스에 맞게 변화하고 gsonFromJson에 저장하는 코드
                val gsonFromJson = gson.fromJson(gsonToJson, CurrentPrice::class.java)

                //currentPriceResult를 가공하여 저장
                val currentPriceResult = CurrentPriceResult(coin.key, gsonFromJson)

                currentPriceResultList.add(currentPriceResult)

            } catch (e: java.lang.Exception) {
                Timber.d(e.toString())
            }

        }
        return currentPriceResultList

    }
}