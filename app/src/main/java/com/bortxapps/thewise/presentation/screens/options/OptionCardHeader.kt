package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.screens.utils.getImagePath
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun OptionHeader(
    showWinningIcon: Boolean,
    option: Option,
    expandCallBack: () -> Unit,
    expanded: Boolean
) {
    val colTitle = colorResource(id = R.color.yellow_800)

    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 150.dp),
        imageModel = getImagePath(imageName = option.imageUrl),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (showWinningIcon) {
            Icon(
                painterResource(id = R.drawable.ic_trophy),
                contentDescription = "",
                tint = colorResource(id = R.color.yellow_800),
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 10.dp)
            )
        }
        Text(
            text = option.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            color = colTitle,
            maxLines = 2
        )

        Spacer(Modifier.weight(1f))

        IconButton(onClick = { expandCallBack }) {
            if (!expanded) {
                Icon(Icons.Default.ArrowDropDown, "")
            } else {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "")
            }
        }
    }
}