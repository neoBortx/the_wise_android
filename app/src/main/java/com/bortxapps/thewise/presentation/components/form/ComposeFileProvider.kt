package com.bortxapps.thewise.presentation.components.form

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.bortxapps.thewise.R
import java.io.File

class ComposeFileProvider : FileProvider(R.xml.image_path) {

    companion object {
        fun getImageUri(context: Context): Uri {
            // 1
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            // 2
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            // 3
            val authority = context.packageName + ".fileprovider"
            // 4
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }

}