package com.example.valyutamohirdev

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyRepository {

    private val api: CurrencyApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cbu.uz/uz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(CurrencyApiService::class.java)
    }

    suspend fun getCurrencyRates(): List<CurrencyRate>? {
        val response = api.getCurrencyRates()
        return if (response.isSuccessful){
            response.body() } else null
    }
}
