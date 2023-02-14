package com.example.coinmonitoringapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmonitoringapp.dataModel.CurrentPrice
import com.example.coinmonitoringapp.dataModel.CurrentPriceResult
import com.example.coinmonitoringapp.dataStore.MyDataStore
import com.example.coinmonitoringapp.db.entity.InterestCoinEntity
import com.example.coinmonitoringapp.repository.DBRepository
import com.example.coinmonitoringapp.repository.NetworkRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

// SelectActivity에서 사용

class SelectViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()
    private val dbRepository = DBRepository()

    private lateinit var currentPriceResultList: ArrayList<CurrentPriceResult>

    //데이터 변화를 관찰하는 LiveData
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult: LiveData<List<CurrentPriceResult>>
        get() = _currentPriceResult

    private val _saved = MutableLiveData<String>()
    val save: LiveData<String>
        get() = _saved

    fun getCurrentCoinList() = viewModelScope.launch {

        val result = networkRepository.getCurrentCoinList()
        //networkRepository.getCurrentCoinList() -> client.getCurrentCoinList() -> Api.getCurrentCoinList()  순 호출

        currentPriceResultList = ArrayList()

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

        _currentPriceResult.value = currentPriceResultList
    }

    fun setUpFirstFlag() = viewModelScope.launch {
        MyDataStore().setUpFirstData()
    }

    //DB에 데이터 저장
    //Dispatchers.IO란 이 디스패처는 기본 스레드 외부에서 디스크 또는 네트워크IO를 실행하도록
    //최적화되어 있다. 예를 들어 Room 구성 요소를 사용하고 파일에서 읽기 쓰기 네트워크 작업 실행한다
    fun saveSelectedCoinList(selectedCoinList: ArrayList<String>) =
        viewModelScope.launch(Dispatchers.IO) {

            // 1. 전체 코인 데이터를 가져와서
            for (coin in currentPriceResultList) {
                //포함하면 true, 아니면 false
                // 2. 내가 선택한 코인인지 아닌지 구분해서
                val selected = selectedCoinList.contains(coin.coinName)

                val interstCoinEntity = InterestCoinEntity(
                    0,
                    coin.coinName,
                    coin.coinInfo.opening_price,
                    coin.coinInfo.closing_price,
                    coin.coinInfo.min_price,
                    coin.coinInfo.max_price,
                    coin.coinInfo.units_traded,
                    coin.coinInfo.acc_trade_value,
                    coin.coinInfo.prev_closing_price,
                    coin.coinInfo.units_traded_24H,
                    coin.coinInfo.acc_trade_value_24H,
                    coin.coinInfo.fluctate_24H,
                    coin.coinInfo.fluctate_rate_24H,
                    selected
                )
                // 3. 저장
                interstCoinEntity.let {
                    dbRepository.insertInterestCoinData(it)
                }
            }

            // 쓰레드를 메인 쓰레드로 변경 해줌 -> 이 withContext(..Main) 때문에 에러 안남
            withContext(Dispatchers.Main){
                _saved.value="done"
            }



        }

}