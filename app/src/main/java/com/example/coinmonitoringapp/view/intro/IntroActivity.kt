package com.example.coinmonitoringapp.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.example.coinmonitoringapp.view.main.MainActivity
import com.example.coinmonitoringapp.databinding.ActivityIntroBinding

//Splash 화면 만들기
//Android Splash Screen 이용할 것
class IntroActivity : AppCompatActivity() {

    private lateinit var binding:ActivityIntroBinding
    private val viewModel:IntroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.checkFirstFlag()

        viewModel.first.observe(this, Observer {
            if(it){
                //처음 접속하는 유저가 아님
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else{
                //처음 접속하는 유저
                binding.animationView.visibility=View.INVISIBLE
                binding.fragmentContainerView.visibility= View.VISIBLE
            }
        })
    }
}