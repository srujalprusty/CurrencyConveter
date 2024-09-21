package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Currency

@Composable
fun CurrencyRow(
    modifier: Modifier = Modifier,
    currencyCode : String,
    currencyName : String,
    onDropDownMenuClicked : () -> Unit
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = currencyCode, fontSize = 20.sp,
            fontFamily = FontFamily.Serif
        )
        IconButton(onClick = onDropDownMenuClicked) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription =null )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = currencyName , fontSize = 18.sp, fontWeight = FontWeight.Light)

    }
}


@Composable

fun KeyboardButton(
    modifier: Modifier = Modifier,
    key: String,
    backgroundColor: Color,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = backgroundColor)
            .clickable { onClick(key) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = key, fontSize = 32.sp)
    }
}

















@Preview
@Composable
fun CurrencyRowPreview() {
    KeyboardButton(key = "", backgroundColor = Color.Cyan) {
        
    }
}