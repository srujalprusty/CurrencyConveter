package com.example.currencyconverter.data.remote

import com.example.currencyconverter.data.remote.dto.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPi {

    @GET("v1/latest")
    suspend fun getLatestRates(
        @Query("apikey") apiKey: String = API_KEY
    ) : CurrencyDto

    companion object{
        const val API_KEY = "fca_live_U4DUjjmBGhd1d4ijJ4IyOyDAzINU3plzgkPcJy4t"
        const val BASE_URL = "https://api.freecurrencyapi.com/"

    }


}