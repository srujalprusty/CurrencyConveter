package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.local.CurrencyRateDao
import com.example.currencyconverter.data.local.entity.toCurrencyRate
import com.example.currencyconverter.data.local.CurrencyRateDatabase
import com.example.currencyconverter.data.remote.CurrencyAPi
import com.example.currencyconverter.data.remote.dto.toCurrencyRate
import com.example.currencyconverter.domain.model.CurrencyRate
import com.example.currencyconverter.domain.model.Resource
import com.example.currencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class CurrencyRepositoryImpl(
    private val api: CurrencyAPi,
    private val dao: CurrencyRateDao
): CurrencyRepository {

    override fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>> = flow{
        val localCurrencyRates = getLocalCurrencyRates()
        emit(Resource.Success(data = localCurrencyRates))

        try {
           val newRates = getRemoteCurrencyRates()
            updateLocalDatabase(newRates)
            emit(Resource.Success(newRates))

        }catch (e:IOException){
            emit(
                Resource.Error(
                    message = "Couldn't load data",
                    data = localCurrencyRates
                )
            )
        }catch (e:Exception){
            emit(
                Resource.Error(
                    message = "Opps Something went wrong ${e.message}",
                    data = localCurrencyRates
                )
            )

        }
    }

    private suspend fun getLocalCurrencyRates(): List<CurrencyRate> {
        return dao.getAllRates().map { it.toCurrencyRate() }
    }


    private suspend fun getRemoteCurrencyRates(): List<CurrencyRate> {
      val response = api.getLatestRates()
        return response.toCurrencyRate()

    }

    private suspend fun updateLocalDatabase(currencyRate: List<CurrencyRate>){
        dao.upsertAll(currencyRate.map { it.toCurrencyRate() })

    }
}