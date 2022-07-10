package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bortxapps.thewise.R

object TextHeader {
    @Composable
    fun GetTextHeader(tittle: String) {
        Text(
            text = tittle,
            color = colorResource(id = R.color.yellow_800),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top= 15.dp, bottom = 20.dp)
                .padding(horizontal = 15.dp),

            )
    }
}