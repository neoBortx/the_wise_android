package com.bortxapps.thewise.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R

object BottomButton {

    @Composable
    fun GetBottomButton(callback: () -> Unit, stringResourceId: Int, enabled: Boolean) {
        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .fillMaxWidth()
                .testTag("bottom_button"),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.yellow_800),
                disabledBackgroundColor = colorResource(id = R.color.disabled_button)
            ),
            onClick = { callback() },
            enabled = enabled
        ) {
            Text(stringResource(stringResourceId), color = colorResource(id = R.color.dark_text))
        }
    }
}