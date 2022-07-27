package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar


data class MenuAction(val imageVector: ImageVector, val actionCallBack: () -> Unit)

object TopAppBar {

    @Composable
    fun DrawIcon(backCallback: (() -> Unit)?) {
        if (backCallback != null) {
            IconButton(onClick = {
                backCallback()
            }) {
                Icon(Icons.Rounded.ArrowBack, "")
            }
        }
    }

    @Composable
    fun DrawMenu(menuActions: List<MenuAction> = listOf()) {
        menuActions.forEach {
            IconButton(onClick = it.actionCallBack) {
                Icon(
                    it.imageVector,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun GetTopAppBar(
        title: String,
        menuActions: List<MenuAction> = listOf(),
        backCallback: (() -> Unit)? = null,
        showIcon: Boolean
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showIcon) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_the_wise_logo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 10.dp)
                        )
                    }
                    Text(title)
                }
            },
            navigationIcon = { DrawIcon(backCallback) },
            actions = { DrawMenu(menuActions) },
            backgroundColor = colorResource(id = R.color.yellow_800)
        )
    }
}

@Composable
@Preview
fun ShowPreview() {
    GetTopAppBar("The wise", showIcon = false, backCallback = { })
}

@Composable
@Preview
fun ShowPreviewIcon() {
    GetTopAppBar("The wise", showIcon = true)
}