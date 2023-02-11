package com.example.coinmonitoringapp.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.coinmonitoringapp.App

class MyDataStore {

    private val context = App.context()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_pref")

    }

    private val mDataStore: DataStore<Preferences> = context.dataStore

    private val FIRST_FLAG = booleanPreferencesKey("FIRST_FLAG")

    //메인액티비티로 갈 때 True로 변경

    //처음 접속하는 유저가 아니면 --> true
    //처음 접속하는 유저이면    --> false
    suspend fun setUpFirstData() {
        mDataStore.edit { Preferences ->
            Preferences[FIRST_FLAG] = true
        }
    }

    //현재 FIRST_FLAG값을 가져오는 함수수
   suspend fun getFirstData(): Boolean {
        var currentValue = false

        mDataStore.edit { preferences ->
            currentValue = preferences[FIRST_FLAG] ?: false

        }

        return currentValue
    }
}