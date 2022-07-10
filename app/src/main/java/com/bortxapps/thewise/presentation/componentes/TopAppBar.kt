package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bortxapps.thewise.R

object TopAppBar {
    @Composable
    fun GetTopAppBar(showBackButton: Boolean, title: String, backCallback: () -> Unit) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (!showBackButton) {
                    Icon(
                        modifier = Modifier.size(40.dp).padding(start = 20.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_the_wise_logo),
                        contentDescription = "app icon"
                    )
                } else {
                    IconButton(onClick = {
                        backCallback()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, "")
                    }
                }
            },
            backgroundColor = colorResource(id = R.color.yellow_800)

        )
    }
}