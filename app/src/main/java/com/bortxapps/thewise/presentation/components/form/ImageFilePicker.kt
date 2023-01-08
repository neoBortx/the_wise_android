package com.bortxapps.thewise.presentation.components.form

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bortxapps.thewise.R
import com.bortxapps.thewise.permissions.PermissionData
import com.bortxapps.thewise.permissions.RequestSinglePermission
import com.bortxapps.thewise.presentation.components.dialog.TakeDialog
import com.bortxapps.thewise.presentation.screens.utils.getImagePath
import kotlinx.coroutines.CoroutineScope
import java.io.File

object ImageFilePicker {

    private fun imageExists(path: String) = File(path).exists()

    private const val noImageUrl = "file:///android_asset/no_image.png"

    @Composable
    fun ImagePickerField(
        currentImageUrl: String,
        onImageSelected: (Uri) -> Unit,
        snackBarHostState: SnackbarHostState,
        scope: CoroutineScope
    ) {
        val context = LocalContext.current
        var enablePhotos by remember {
            mutableStateOf(false)
        }

        var askPermissions by remember {
            mutableStateOf(false)
        }

        val currentImageFullPath = getImagePath(currentImageUrl)

        val imageUri = Uri.parse(
            if (currentImageUrl.isNotEmpty() && imageExists(currentImageFullPath)) {
                currentImageFullPath
            } else {
                noImageUrl
            }
        )


        var cameraUri by remember {
            mutableStateOf<Uri?>(null)
        }

        var showPhotoDialog by remember {
            mutableStateOf(false)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                onImageSelected(uri)
            }
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && cameraUri != null) {
                onImageSelected(cameraUri!!)
            }
        }

        Column(modifier = Modifier.padding(top = 10.dp)) {
            AsyncImage(model = imageUri, "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(200.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = LocalIndication.current
                    ) {
                        askPermissions = true
                    })
        }

        if (showPhotoDialog) {
            TakeDialog(
                closeCallBack = { showPhotoDialog = false },
                photoCallBack = {
                    cameraUri = ComposeFileProvider.getImageUri(context)
                    cameraLauncher.launch(cameraUri)
                },
                galleryCallback = { launcher.launch("image/*") },
                enableCamera = enablePhotos
            )
        }

        if (askPermissions) {
            RequestSinglePermission(
                permissionData = PermissionData(
                    permission = Manifest.permission.CAMERA,
                    rationaleMessage = stringResource(R.string.rationale_message),
                    dismissMessage = stringResource(R.string.dismiss_permissions_message),
                    checkPermissionsActionMessage = stringResource(R.string.check_permissions),
                    onAccepted = {
                        askPermissions = false
                        enablePhotos = true
                        showPhotoDialog = true
                    },
                    onDismiss = {
                        askPermissions = false
                        enablePhotos = false
                        showPhotoDialog = true
                    }),
                context = LocalContext.current,
                snackBarHostState = snackBarHostState,
                scope
            )
        }
    }
}

@Preview
@Composable
fun PreviewImagePickerTextField() {

}