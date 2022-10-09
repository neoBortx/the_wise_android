package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar


data class MenuAction(val imageVector: ImageVector, val actionCallBack: () -> Unit)

object TopAppBar {

    @Composable
    fun DrawBackArrow(backCallback: (() -> Unit)) {
        IconButton(onClick = { backCallback() }) {
            Icon(
                Icons.Rounded.ArrowBack,
                "",
                tint = colorResource(id = R.color.light_text)
            )
        }
    }

    @Composable
    fun DrawMenu(menuActions: List<MenuAction> = listOf()) {
        menuActions.forEach {
            IconButton(onClick = it.actionCallBack) {
                Icon(
                    it.imageVector,
                    contentDescription = null,
                    tint = colorResource(id = R.color.light_text)
                )
            }
        }
    }

    @Composable
    fun GetTopAppBar(
        title: String,
        menuActions: List<MenuAction> = listOf(),
        backCallback: (() -> Unit)? = null
    ) {
        if (backCallback != null) {
            TopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            title,
                            color = colorResource(id = R.color.light_text)
                        )
                    }
                },
                navigationIcon = { DrawBackArrow(backCallback) },
                actions = { DrawMenu(menuActions) },
                backgroundColor = colorResource(id = R.color.yellow_800)
            )
        } else {
            TopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            title,
                            color = colorResource(id = R.color.light_text),
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.washington_text))
                        )
                    }
                },
                navigationIcon = null,
                actions = { DrawMenu(menuActions) },
                backgroundColor = colorResource(id = R.color.yellow_800)
            )
        }

    }
}

@Composable
@Preview
fun ShowPreview() {
    GetTopAppBar("The wise", backCallback = { })
}

@Composable
@Preview
fun ShowPreviewIcon() {
    GetTopAppBar("The wise")
}