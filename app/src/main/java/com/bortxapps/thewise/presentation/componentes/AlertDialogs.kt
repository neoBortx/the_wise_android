package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R

@Composable
fun DeleteAlertDialog(closeCallBack: () -> Unit, acceptCallBack: () -> Unit) {
    AlertDialog(
        onDismissRequest = closeCallBack,
        title = { Text(stringResource(id = R.string.delete_election)) },
        text = {
            Column() {
                Text(stringResource(R.string.delete_disclaimer))
                Image(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        },
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

@Composable
@Preview
fun PreviewDeleteAlertDialog() {
    DeleteAlertDialog(closeCallBack = { /*TODO*/ }) {

    }
}