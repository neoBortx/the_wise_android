package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R

object BottomButton {
    @Composable
    fun GetFormBottomButton(callback: () -> Unit, stringResourceId: Int, formResult: List<String>) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.yellow_800)),
                onClick = { callback() },
                enabled = formResult.any()
            ) {
                Text(stringResource(stringResourceId))
            }
        }
    }

    @Composable
    fun GetBottomButton(callback: () -> Unit, stringResourceId: Int, enabled: Boolean) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.yellow_800)),
                onClick = { callback() },
                enabled = enabled
            ) {
                Text(stringResource(stringResourceId))
            }
        }
    }
}