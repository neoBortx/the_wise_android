package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.OptionFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.componentes.TextError.GetTextError
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.componentes.texfield.SelectableConditionBadge
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.viewmodels.ConditionViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun OptionFormScreen(
    formViewModel: OptionFormViewModel = hiltViewModel(),
    conditionalViewModel: ConditionViewModel = hiltViewModel(),
    electionId: Long,
    formCompletedCallback: () -> Job
) {
    conditionalViewModel.configure(electionId)

    val nameLabel = stringResource(id = R.string.name_option)

    val scope = rememberCoroutineScope()

    fun onButtonFormClick() {
        formViewModel.createNewOption()
        formViewModel.clearOption()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white)) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(
                color = colorResource(R.color.dark_text),
                thickness = 3.dp,
                modifier = Modifier
                    .padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)
                    .width(50.dp)
            )
            ImagePickerField(formViewModel.optionImageUrl)

            NoEmptyTextField(nameLabel, formViewModel.optionName) { formViewModel.setName(it) }

            Text(
                stringResource(R.string.assign_requisites),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.dark_text)
            )
            if (formViewModel.configuredConditions.isEmpty()) {
                GetTextError(stringResource(R.string.tooltip_select_requisites))
            }
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 10.dp)
                    .padding(horizontal = 20.dp),
                mainAxisAlignment = MainAxisAlignment.Start,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 12.dp,
                mainAxisSpacing = 8.dp
            ) {
                formViewModel.allConditions.forEach { condition ->
                    SelectableConditionBadge(
                        label = condition.name,
                        weight = condition.weight,
                        isSelected = formViewModel.configuredConditions.any { it == condition }
                    ) {
                        formViewModel.selectCondition(it, condition)
                    }
                }
            }
            Spacer(Modifier.weight(1f, false))
            GetBottomButton(
                {
                    scope.launch {
                        onButtonFormClick()
                    }
                },
                R.string.save_option,
                formViewModel.isButtonEnabled
            )
        }
    }

}