package com.bortxapps.thewise.presentation.componentes.text

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

object TextDescription {
    @Composable
    fun GetTextDescription(description: String) {
        Text(
            text = description,
            color = colorResource(id = R.color.black),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(horizontal = 15.dp)
        )
    }
}