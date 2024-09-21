package com.example.currencyconverter.presentation


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.theme.brown1
import com.example.currencyconverter.ui.theme.green1
import com.example.currencyconverter.ui.theme.green2
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun MainScreen(
    state : MainScreenState,
    onEvent: (MainScreenEvent) -> Unit
) {

    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "DEL")

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var shouldBottomSheetShowBy by remember {
        mutableStateOf(false)
    }

    if (shouldBottomSheetShowBy) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { shouldBottomSheetShowBy = false },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Select Currency",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                }
            }, content = {
                    BottomSheetContent(onItemClicked = {currencyCode ->
                        onEvent(MainScreenEvent.ButtomSheetItemClicked(currencyCode))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible)  shouldBottomSheetShowBy = false
                        }
                    }, currenciesList = state.currencyRates.values.toList()
                    )

            }
        )
    }


    LaunchedEffect(key1 = state.error) {
        if (state.error == null) {
            Toast.makeText(context,state.error,Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        color = green2
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),


        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                text = "Currency Converter", fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            //Card
            Spacer(modifier = Modifier.height(35.dp))

            Box(contentAlignment = Alignment.CenterStart) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            CurrencyRow(
                                modifier = Modifier.fillMaxWidth(),
                                currencyCode = state.fromCurrencyCode,
                                currencyName = state.currencyRates[state.fromCurrencyCode]?.name ?: "",
                                onDropDownMenuClicked = {
                                    shouldBottomSheetShowBy  = true
                                    onEvent(MainScreenEvent.FromCurrencySelect)
                                }
                            )
                            Text(text = state.fromCurrencyValue,
                                fontSize = 40.sp, fontWeight = FontWeight.SemiBold
                                , modifier = Modifier.clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {onEvent(MainScreenEvent.FromCurrencySelect)},
                                ),
                                color = if (state.selection == SelectionState.TO){
                                    MaterialTheme.colorScheme.onSurface
                                } else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(text = state.toCurrencyValue, fontSize = 40.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {onEvent(MainScreenEvent.ToCurrencySelect)}
                                ),
                                color = if (state.selection == SelectionState.TO){
                                    MaterialTheme.colorScheme.onSurface
                                } else MaterialTheme.colorScheme.onSurface
                            )
                            CurrencyRow(
                                modifier = Modifier.fillMaxWidth(),
                                currencyCode = state.toCurrencyCode,
                                currencyName = state.currencyRates[state.toCurrencyCode]?.name ?: "",
                                onDropDownMenuClicked = {
                                    shouldBottomSheetShowBy  = true
                                    onEvent(MainScreenEvent.ToCurrencySelect)

                                }
                            )
                        }

                    }

                }
                Box(modifier = Modifier
                    .padding(start = 40.dp)
                    .clickable {
                        onEvent(MainScreenEvent.SwapIconClicked)
                    }
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.background)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_sync_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 20.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(keys) { key ->
                    KeyboardButton(
                        modifier = Modifier.aspectRatio(1f),
                        key = key,
                        backgroundColor = if (key == "DEL") brown1
                        else green1,
                        onClick = {
                            onEvent(MainScreenEvent.NumberButtonClicked(key))
                        }
                    )
                }
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
  //  MainScreen()
}