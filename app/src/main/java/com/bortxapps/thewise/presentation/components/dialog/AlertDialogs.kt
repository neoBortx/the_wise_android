package com.bortxapps.thewise.presentation.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R

@Composable
fun DeleteAlertDialog(closeCallBack: () -> Unit, acceptCallBack: () -> Unit) {
    AlertDialog(
        onDismissRequest = closeCallBack,
        title = { Text(stringResource(id = R.string.delete_question)) },
        text = {
            Column {
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
fun TakeDialog(
    photoCallBack: () -> Unit,
    galleryCallback: () -> Unit,
    closeCallBack: () -> Unit,
    enableCamera: Boolean
) {
    AlertDialog(
        onDismissRequest = closeCallBack,
        title = { Text(stringResource(id = R.string.get_photo)) },
        text = {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.ic_photo),
                    contentDescription = "",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    photoCallBack()
                    closeCallBack()
                }, enabled = enableCamera
            ) {
                Text(stringResource(R.string.camera))
            }
        },
        dismissButton = {
            Button(onClick = {
                galleryCallback()
                closeCallBack()
            }) {
                Text(stringResource(R.string.gallery))
            }
        },
        backgroundColor = colorResource(id = R.color.white)
    )
}

@Composable
@Preview
fun PreviewDeleteAlertDialog() {
    DeleteAlertDialog(closeCallBack = { }) {

    }
}

@Composable
@Preview
fun PreviewDialog() {
    TakeDialog(closeCallBack = { }, photoCallBack = {}, galleryCallback = {}, enableCamera = false)
}