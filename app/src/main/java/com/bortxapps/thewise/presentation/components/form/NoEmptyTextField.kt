package com.bortxapps.thewise.presentation.components.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R


@Composable
fun NoEmptyTextField(
    label: String,
    textValue: String,
    testTag: String,
    callbackMethod: (text: String) -> Unit
) {
    var value = textValue
    var isEmpty by remember { mutableStateOf(false) }
    val errorMessage = stringResource(id = R.string.cant_be_empty)
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(0.dp)) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = colorResource(id = R.color.yellow_800),
                unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                backgroundColor = colorResource(id = R.color.white),
                textColor = colorResource(id = R.color.light_text)
            ),
            value = value,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .testTag(testTag),
            label = { Text(text = label) },
            isError = isEmpty,
            onValueChange = { newValue ->
                value = newValue
                callbackMethod(value)
                isEmpty = newValue.isBlank()
            },
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        if (isEmpty) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .testTag("${testTag}_error_label")
            )
        }
    }
}

@Composable
fun RegularTextField(
    label: String,
    defaultValue: String,
    testTag: String,
    callbackMethod: (text: String) -> Unit
) {
    var value = defaultValue
    val focusManager = LocalFocusManager.current

    Column {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = colorResource(id = R.color.yellow_800),
                unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                backgroundColor = colorResource(id = R.color.white),
                textColor = colorResource(id = R.color.light_text)
            ),
            value = value,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .testTag(testTag),
            label = { Text(text = label) },
            onValueChange = { newValue ->
                value = newValue
                callbackMethod(newValue)
            },
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
    }
}

fun emptyCallBack(text: String) {
    println(text)
}

@Preview
@Composable
fun PreviewNoEmptyTextField() {
    NoEmptyTextField("label", "textValue", "tag", ::emptyCallBack)
}

@Preview
@Composable
fun PreviewRegularTextField() {
    RegularTextField("label", "textValue", "tag", ::emptyCallBack)
}