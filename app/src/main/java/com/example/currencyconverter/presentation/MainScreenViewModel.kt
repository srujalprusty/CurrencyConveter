package com.example.currencyconverter.presentation

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyconverter.domain.model.Resource
import com.example.currencyconverter.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val repository: CurrencyRepository
): ViewModel() {

    var state by mutableStateOf(MainScreenState())

    init {
        getCurrencyRatesList()
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.FromCurrencySelect -> {
                state = state.copy(selection = SelectionState.FROM)
            }

            is MainScreenEvent.ToCurrencySelect -> {
                state = state.copy(selection = SelectionState.TO)
            }

            is MainScreenEvent.SwapIconClicked -> {
                state = state.copy(
                    fromCurrencyCode = state.toCurrencyCode,
                    fromCurrencyValue = state.toCurrencyValue,
                    toCurrencyCode = state.fromCurrencyCode,
                    toCurrencyValue = state.fromCurrencyValue
                )

            }

            is MainScreenEvent.ButtomSheetItemClicked -> {
                        updateFromCurrencyValue(value = event.value)
            }

            is MainScreenEvent.NumberButtonClicked ->{
                if (state.selection == SelectionState.FROM){
                    state = state.copy(fromCurrencyCode =  event.value)
                }
                else if (state.selection == SelectionState.TO){
                    state = state.copy(toCurrencyCode =  event.value)
                }
                updateFromCurrencyValue("")
            }
        }
    }

    private fun getCurrencyRatesList() {
        viewModelScope.launch {
            repository
                .getCurrencyRatesList()
                .collectLatest { result ->
                    state = when (result) {
                        is Resource.Success -> {
                            state.copy(
                                currencyRates = result.data?.associateBy { it.code } ?: emptyMap(),
                                error = null
                            )
                        }

                        is Resource.Error -> {
                            state.copy(
                                currencyRates = result.data?.associateBy { it.code } ?: emptyMap(),
                                error = result.message
                            )
                        }
                    }
                }
        }
    }


    private fun updateFromCurrencyValue(value: String) {
        val currentCurrencyValue = when (state.selection) {
            SelectionState.FROM -> state.fromCurrencyValue
            SelectionState.TO -> state.toCurrencyValue
        }
        val fromCurrencyRate = state.currencyRates[state.fromCurrencyCode]?.rate ?: 0.0
        val toCurrencyRate = state.currencyRates[state.toCurrencyCode]?.rate ?: 0.0

        val updatedCurrencyValue = when (value) {
            "C" -> "0.00"
            else -> if (currentCurrencyValue == "0.00") value else currentCurrencyValue + value
        }

        val numberFormat = DecimalFormat("#.00")

        when (state.selection) {
            SelectionState.FROM -> {
                val fromValue = updatedCurrencyValue.toDoubleOrNull() ?: 0.0
                val toValue = fromValue / fromCurrencyRate * toCurrencyRate
                state = state.copy(
                    fromCurrencyValue = updatedCurrencyValue,
                    toCurrencyValue = numberFormat.format(toValue)
                )
            }

            SelectionState.TO -> {
                val toValue = updatedCurrencyValue.toDoubleOrNull() ?: 0.0
                val fromValue = toValue / toCurrencyRate * fromCurrencyRate
                state = state.copy(
                    toCurrencyValue = updatedCurrencyValue,
                    fromCurrencyValue = numberFormat.format(fromValue)
                )
            }
        }
    }
}