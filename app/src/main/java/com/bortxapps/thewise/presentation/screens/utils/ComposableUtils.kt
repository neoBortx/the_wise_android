package com.bortxapps.thewise.presentation.screens.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getImagePath(imageName: String): String {
    return LocalContext.current.filesDir.absolutePath + "/" + imageName
}