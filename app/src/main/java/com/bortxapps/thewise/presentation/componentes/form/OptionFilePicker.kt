package com.bortxapps.thewise.presentation.componentes.form

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
import com.skydoves.landscapist.glide.GlideImage

object OptionFilePicker {

    @Composable
    fun ImagePickerField(
        imageUrl: String,
        onImageSelected: (Bitmap) -> Unit,
    ) {
        val context = LocalContext.current
        val url = imageUrl.ifEmpty {
            "file:///android_asset/no_image.png"
        }

        var showPhotoDialog by remember {
            mutableStateOf(false)
        }

        var bitmap by remember {
            mutableStateOf<Bitmap?>(null)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)!!
            onImageSelected(bitmap!!)
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) {

        }

        val source = remember { MutableInteractionSource() }

        Column(modifier = Modifier.padding(top = 10.dp)) {
            GlideImage(imageModel = if (bitmap != null) bitmap else url,
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(200.dp)
                    .clickable(
                        interactionSource = source,
                        indication = LocalIndication.current
                    ) {})
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
    ImagePickerField("AAAAA") {

    }
}