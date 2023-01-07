package com.bortxapps.thewise.presentation.components.conditions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.components.badges.RemovableConditionBadge
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@ExperimentalMaterialApi
@Composable
fun ConditionsConfigurationControl(
    conditions: List<Condition>,
    onConditionAdded: (String, ConditionWeight) -> Unit,
    onConditionRemoved: (Long) -> Unit
) {
    val maxCharacters = 20
    var conditionName by remember { mutableStateOf("") }
    var conditionWeight by remember { mutableStateOf(ConditionWeight.LOW) }
    var selectedIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var buttonEnabled by remember { mutableStateOf(false) }

    val items = ConditionWeight.values()
    val focusManager = LocalFocusManager.current

    @Composable
    fun GetConditionWeight(weight: ConditionWeight): String {
        return when (weight) {
            ConditionWeight.LOW -> stringResource(R.string.meh)
            ConditionWeight.MEDIUM -> stringResource(R.string.wish)
            ConditionWeight.MUST -> stringResource(R.string.must)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = colorResource(id = R.color.yellow_800),
                    unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                    focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                    unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                    backgroundColor = colorResource(id = R.color.white),
                    textColor = colorResource(id = R.color.light_text)
                ),
                label = { Text(text = stringResource(id = R.string.requisite_name)) },
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .weight(1f)
                    .testTag("conditions_control_name_text_field"),
                value = conditionName,
                singleLine = true,
                onValueChange = {
                    if (it.length < maxCharacters) {
                        conditionName = it
                        buttonEnabled = conditionName.isNotBlank()
                    }
                },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.BottomStart)
                    .padding(horizontal = 10.dp)
            ) {
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedLabelColor = colorResource(id = R.color.yellow_800),
                        unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                        focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                        unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                        backgroundColor = colorResource(id = R.color.white),
                        textColor = colorResource(id = R.color.light_text)
                    ),
                    label = { Text(text = stringResource(id = R.string.requisite_importance)) },
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white))
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable(onClick = { expanded = true }),
                    value = GetConditionWeight(conditionWeight),
                    singleLine = true,
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                            conditionWeight = items[selectedIndex]
                        }) {
                            Text(text = GetConditionWeight(s))
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .size(50.dp)
                    .testTag("conditions_control_add_button"),
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.yellow_800),
                    disabledBackgroundColor = colorResource(id = R.color.disabled_button)
                ),
                onClick = {
                    onConditionAdded(conditionName, conditionWeight)
                    focusManager.clearFocus()
                    conditionName = ""
                    buttonEnabled = false
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.size(40.dp),
                    tint = colorResource(id = R.color.dark_text)
                )
            }
        }
        if (conditions.isEmpty()) {
            Text(
                text = "No conditions configured jet",
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
            )
        } else {
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 13.dp, horizontal = 5.dp)
                    .testTag("conditions_form_control_list"),
                mainAxisAlignment = MainAxisAlignment.Start,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 12.dp,
                mainAxisSpacing = 8.dp
            ) {
                conditions.forEach { condition ->
                    RemovableConditionBadge(condition.name, condition.weight) {
                        onConditionRemoved(condition.id)
                    }
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@Preview
@Composable
fun ShowControl() {
    ConditionsConfigurationControl(
        onConditionRemoved = {},
        conditions = mutableListOf(),
        onConditionAdded = { _, _ -> }
    )
}

