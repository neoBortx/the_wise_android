package com.bortxapps.thewise.presentation.componentes.form

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R

@Composable
fun FormDragControl(
) {
    Divider(
        color = colorResource(R.color.dark_text),
        thickness = 3.dp,
        modifier = Modifier
            .padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)
            .width(50.dp)
    )
}