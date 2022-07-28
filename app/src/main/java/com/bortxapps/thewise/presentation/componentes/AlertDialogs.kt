package com.bortxapps.thewise.presentation.componentes

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bortxapps.thewise.R

@Composable
fun DeleteAlertDialog(closeCallBack: () -> Unit, acceptCallBack: () -> Unit) {
    AlertDialog(
        onDismissRequest = closeCallBack,
        title = { Text(stringResource(id = R.string.delete_election)) },
        text = { Text(stringResource(R.string.delete_disclaimer)) },
        confirmButton = {
            Button(
                onClick = {
                    closeCallBack()
                    acceptCallBack()
                }) {
                Text(stringResource(R.string.forget))
            }
        },
        dismissButton = {
            Button(onClick = closeCallBack) {
                Text(stringResource(R.string.keep))
            }
        }
    )
}