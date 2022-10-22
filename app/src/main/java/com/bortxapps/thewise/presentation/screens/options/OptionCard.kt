package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@ExperimentalMaterialApi
@Composable
fun OptionCard(
    option: Option,
    clickCallback: () -> Unit,
    deleteCallBack: (() -> Unit)?,
    showWinningIcon: Boolean
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    @Composable
    fun OptionHeader(
        showWinningIcon: Boolean,
        option: Option,
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

            IconButton(onClick = { expanded = !expanded }) {
                if (!expanded) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                        tint = colorResource(id = R.color.light_text)
                    )
                } else {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "",
                        tint = colorResource(id = R.color.light_text)
                    )
                }
            }
        }
    }

    @Composable
    fun Requisites(showBottomMargin: Boolean, conditions: List<Condition>) {
        Text(
            text = stringResource(id = R.string.matching_conditions_label),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 5.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            color = colorResource(id = R.color.light_text),
            overflow = TextOverflow.Ellipsis
        )
        FlowRow(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 10.dp,
                    end = 6.dp,
                    bottom = if (showBottomMargin) 0.dp else 10.dp
                ),
            mainAxisAlignment = MainAxisAlignment.Start,
            mainAxisSize = SizeMode.Expand,
            crossAxisSpacing = 5.dp,
            mainAxisSpacing = 5.dp,
        ) {
            conditions.sortedByDescending { condition -> condition.weight }
                .forEach { condition ->
                    SimpleConditionBadge(condition.name, condition.weight)
                }
        }
    }

    @Composable
    fun DeleteButton(deleteCallBack: (() -> Unit)?) {

        val colTitle = colorResource(id = R.color.yellow_800)

        deleteCallBack?.let {
            Button(
                onClick = { deleteCallBack() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    textDecoration = TextDecoration.Underline,
                    color = colTitle,
                    textAlign = TextAlign.End
                )
            }
        }
    }

    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 0.dp),
        onClick = { clickCallback() },
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.background(
                color = colorResource(
                    id = R.color.white
                )
            )
        ) {
            OptionHeader(
                showWinningIcon = showWinningIcon,
                option = option,
            )

            androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                Column {
                    Divider(modifier = Modifier.padding(vertical = 0.dp, horizontal = 5.dp))
                    Requisites(
                        showBottomMargin = deleteCallBack != null,
                        conditions = option.matchingConditions
                    )
                    DeleteButton(deleteCallBack)
                }
            }

        }
    }
}


@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewPaintOptionRow() {
    OptionCard(
        Option(
            id = 0,
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

        },
        true
    )
}
