package com.example.coinmonitoringapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinmonitoringapp.view.main.MainActivity
import com.example.coinmonitoringapp.databinding.ActivitySelectBinding
import com.example.coinmonitoringapp.view.adapter.SelectRVAdapter

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
            }
        })
    }
}