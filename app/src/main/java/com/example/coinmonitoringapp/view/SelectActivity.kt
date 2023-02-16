package com.example.coinmonitoringapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.coinmonitoringapp.background.GetCoinPriceRecentContractedWorkManager
import com.example.coinmonitoringapp.view.main.MainActivity
import com.example.coinmonitoringapp.databinding.ActivitySelectBinding
import com.example.coinmonitoringapp.view.adapter.SelectRVAdapter
import java.util.concurrent.TimeUnit

class SelectActivity : AppCompatActivity() {

    private lateinit var binding :ActivitySelectBinding
    private val viewModel:SelectViewModel by viewModels()

    private lateinit var selectRVAdapter: SelectRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCurrentCoinList()

        //데이터 변화 감지
        viewModel.currentPriceResult.observe(this, Observer{
            selectRVAdapter = SelectRVAdapter(this,it)


            //어댑터 연결해줌
            binding.coinListRV.adapter=selectRVAdapter
            binding.coinListRV.layoutManager = LinearLayoutManager(this)

        })
        //FirstFlag 초기 설정
        viewModel.setUpFirstFlag()

        binding.laterTextArea.setOnClickListener {
            viewModel.setUpFirstFlag()
            viewModel.saveSelectedCoinList(selectRVAdapter.selectedCoinList)
            Toast.makeText(this,"버튼 클릭",Toast.LENGTH_SHORT).show()


        }
        viewModel.save.observe(this, Observer {
            if(it.equals("done")){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // 가장 처음으로 저장한 코인 정보가 저장되는 시점
                saveInterestCoinDataPeriodic()
            }
        })
    }

    private fun saveInterestCoinDataPeriodic() {

        val myWork = PeriodicWorkRequest.Builder(
            GetCoinPriceRecentContractedWorkManager::class.java,
            15,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "GetCoinPriceRecentContractedWorkManager",
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }

}