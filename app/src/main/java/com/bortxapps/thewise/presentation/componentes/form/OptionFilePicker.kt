package com.bortxapps.thewise.presentation.componentes.form

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.presentation.componentes.TakeDialog
import com.bortxapps.thewise.presentation.componentes.form.OptionFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.componentes.texfield.getBitMap
import com.skydoves.landscapist.glide.GlideImage

object OptionFilePicker {

    fun getUri(context: Context, itemId: String): Uri? {
        return try {
            Uri.parse(context.applicationInfo.dataDir + "/" + itemId)
        } catch (exception: java.lang.Exception) {
            null
        }

    }

    @Composable
    fun ImagePickerField(
        itemId: String,
    ) {
        val context = LocalContext.current
        val initialBitMap = getBitMap(context, itemId)
        var imageUri by remember {
            mutableStateOf(getUri(context, itemId))
        }
        var bitmap by remember { mutableStateOf(initialBitMap) }
        var showPhotoDialog by remember {
            mutableStateOf(false)
        }


        fun saveBitMap(image: Bitmap?) = context.openFileOutput(itemId, Context.MODE_PRIVATE).use {
            image?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            saveBitMap(bitmap)
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) {

        }

        val source = remember { MutableInteractionSource() }

        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            GlideImage(imageModel = imageUri,
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable(interactionSource = source, indication = LocalIndication.current) {})
        }

        if (showPhotoDialog) {
            TakeDialog(
                closeCallBack = { showPhotoDialog = false },
                photoCallBack = { cameraLauncher.launch(null) },
                galleryCallback = { launcher.launch("image/*") })
        }

        if (source.collectIsPressedAsState().value) {
            showPhotoDialog = true
        }
    }
}

@Preview
@Composable
fun PreviewImagePickerTextField() {
    ImagePickerField("AAAAA")
}