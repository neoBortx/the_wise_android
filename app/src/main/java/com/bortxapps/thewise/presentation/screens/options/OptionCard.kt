package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.bortxapps.thewise.presentation.screens.utils.getImagePath
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PaintOptionRow(
    option: Option,
    clickCallback: () -> Unit,
    deleteCallBack: (() -> Unit)?
) {

    val colTitle = colorResource(id = R.color.yellow_800)
    val colBack = colorResource(id = R.color.transparent)

    var palette by remember { mutableStateOf<Palette?>(null) }
    val scope = rememberCoroutineScope()

    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(100.dp, 300.dp)
            .padding(start = 0.dp),
        onClick = {
            scope.launch {
                clickCallback()
            }
        },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (option.imageUrl.isNotBlank()) 100.dp else 50.dp)
            ) {
                if (option.imageUrl.isNotBlank()) {
                    GlideImage(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        imageModel = getImagePath(imageName = option.imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        bitmapPalette = BitmapPalette {
                            palette = it
                        })
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(palette?.vibrantSwatch?.bodyTextColor?.let {
                        Color(it)
                    } ?: colBack)) {
                    Text(text = option.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                        color = palette?.vibrantSwatch?.rgb?.let {
                            Color(it)
                        } ?: colTitle,
                        maxLines = 2)
                }
            }
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = if (deleteCallBack != null) 0.dp else 10.dp
                    ),
                mainAxisAlignment = MainAxisAlignment.Start,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 5.dp,
                mainAxisSpacing = 5.dp
            ) {
                option.matchingConditions.sortedByDescending { condition -> condition.weight }
                    .forEach { condition ->
                        SimpleConditionBadge(condition.name, condition.weight)
                    }
            }

            deleteCallBack?.let {
                Button(
                    onClick = { scope.launch { deleteCallBack() } },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "DELETE",
                        textDecoration = TextDecoration.Underline,
                        color = palette?.vibrantSwatch?.rgb?.let {
                            Color(it)
                        } ?: colTitle,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewPaintOptionRow() {
    PaintOptionRow(
        Option(id = 0,
            electionId = 0,
            name = "option 1",
            imageUrl = "",
            matchingConditions = mutableListOf<Condition>().apply {
                add(
                    Condition(
                        id = 0,
                        electionId = 0,
                        "Condition 1",
                        weight = ConditionWeight.MUST
                    )
                )
                add(
                    Condition(
                        id = 1,
                        electionId = 0,
                        "Condition 2",
                        weight = ConditionWeight.LOW
                    )
                )
            }), clickCallback = {

        }, deleteCallBack = {

        }
    )
}
