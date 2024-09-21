package com.example.currencyconverter.presentation

sealed class MainScreenEvent {

    object FromCurrencySelect: MainScreenEvent()
    object ToCurrencySelect: MainScreenEvent()
    object SwapIconClicked: MainScreenEvent()
    data class ButtomSheetItemClicked(val value: String): MainScreenEvent()
    data class NumberButtonClicked(val value: String): MainScreenEvent()

}