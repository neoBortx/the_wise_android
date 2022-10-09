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

object TextError {
    @Composable
    fun GetTextError(error: String) {
        Text(
            text = error,
            color = colorResource(id = R.color.error),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 20.dp),
        )
    }
}