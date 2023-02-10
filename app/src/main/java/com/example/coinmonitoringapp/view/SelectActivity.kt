package com.example.coinmonitoringapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinmonitoringapp.R
import com.example.coinmonitoringapp.databinding.ActivitySelectBinding
import com.example.coinmonitoringapp.view.adapter.SelectRVAdapter
import timber.log.Timber

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
    }
}