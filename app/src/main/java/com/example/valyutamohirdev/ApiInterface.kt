package com.example.valyutamohirdev

import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("arkhiv-kursov-valyut/json/")
    suspend fun getCurrencyRates(): Response<List<CurrencyRate>>
}