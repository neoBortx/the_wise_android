package com.bortxapps.thewise.presentation.componentes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.presentation.componentes.OptionFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.componentes.texfield.getBitMap
import com.skydoves.landscapist.glide.GlideImage

object OptionFilePicker {

    @Composable
    fun ImagePickerField(
        itemId: String
    ) {
        val context = LocalContext.current
        val initialBitMap = getBitMap(context, itemId)
        var bitmap by remember { mutableStateOf(initialBitMap) }


        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            context.openFileOutput(itemId, Context.MODE_PRIVATE).use {
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

        val source = remember { MutableInteractionSource() }

        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            GlideImage(imageModel = bitmap,
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable(interactionSource = source, indication = LocalIndication.current) {})
        }

        if (source.collectIsPressedAsState().value) {
            launcher.launch("image/*")
        }
    }
}

@Preview
@Composable
fun PreviewImagePickerTextField() {
    ImagePickerField("AAAAA")
}