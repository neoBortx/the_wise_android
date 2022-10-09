package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Option

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

    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 0.dp),
        onClick = { clickCallback() },
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            OptionHeader(
                showWinningIcon = showWinningIcon,
                option = option,
                expandCallBack = { expanded = !expanded },
                expanded = expanded
            )
            OptionCardBody(
                expanded = expanded,
                deleteCallBack = deleteCallBack,
                conditions = option.matchingConditions
            )

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
