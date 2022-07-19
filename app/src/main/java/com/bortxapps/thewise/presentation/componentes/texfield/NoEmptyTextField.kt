package com.bortxapps.thewise.presentation.componentes.texfield

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun NoEmptyTextField(label: String, textValue: String, callbackMethod: (text: String) -> Unit) {
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
            ),
            value = value,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
            label = { Text(text = label) },
            isError = isEmpty,
            onValueChange = { newValue ->
                isEmpty = newValue.isBlank()
                value = newValue
                callbackMethod(value)
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
                modifier = Modifier.padding(start = 30.dp)
            )
        }
    }
}

@Composable
fun RegularTextField(label: String, defaultValue: String, callbackMethod: (text: String) -> Unit) {
    var value = defaultValue
    val focusManager = LocalFocusManager.current

    Column {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = colorResource(id = R.color.yellow_800),
                unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                focusedIndicatorColor = colorResource(id = R.color.dark_text),
                unfocusedIndicatorColor = colorResource(id = R.color.dark_text),
                backgroundColor = colorResource(id = R.color.white)
            ),
            value = value,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
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

fun getBitMap(context: Context, itemId: String): Bitmap? {
    return try {
        context.openFileInput(itemId).use {
            BitmapFactory.decodeStream(it)
        }
    } catch (exception: java.lang.Exception) {
        BitmapFactory.decodeResource(context.resources, R.drawable.no_image)
    }

}

@Composable
fun ImagePickerField(
    label: String, itemId: String
) {

    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf(getBitMap(context, itemId))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        context.openFileOutput(itemId, Context.MODE_PRIVATE).use {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    val source = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text(
            text = label, color = colorResource(id = R.color.yellow_800)
        )
        GlideImage(imageModel = bitmap,
            Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(10.dp)
                .clickable(interactionSource = source, indication = LocalIndication.current) {})
    }

    if (source.collectIsPressedAsState().value) {
        launcher.launch("image/*")
    }
}

fun emptyCallBack(text: String) {
    println(text)
}

@Preview
@Composable
fun PreviewNoEmptyTextField() {
    NoEmptyTextField("AAAAA", "BBBBBB", ::emptyCallBack)
}

@Preview
@Composable
fun PreviewRegularTextField() {
    RegularTextField("AAAAA", "BBBBBB", ::emptyCallBack)
}

@Preview
@Composable
fun PreviewImagePickerTextField() {
    ImagePickerField("AAAAA", "BBBBBB")
}