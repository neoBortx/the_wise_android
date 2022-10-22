package com.bortxapps.thewise.presentation.screens.options

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.form.FormDragControl
import com.bortxapps.thewise.presentation.componentes.form.OptionFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.componentes.text.TextError.GetTextError
import com.bortxapps.thewise.presentation.screens.conditions.ConditionsSelectionControl
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import kotlinx.coroutines.launch

@Composable
fun OptionFormScreen(
    formViewModel: OptionFormViewModel = hiltViewModel(),
    formCompletedCallback: () -> Unit
) {
    OptionForm(
        onCreateNewOption = { formViewModel.createNewOption() },
        formCompletedCallback = formCompletedCallback,
        option = formViewModel.option,
        onNameChanged = { name -> formViewModel.setName(name) },
        onImageChanged = { image -> formViewModel.setImage(image) },
        onConditionSelected = { sel, cond -> formViewModel.selectCondition(sel, cond) },
        isButtonEnabled = formViewModel.isButtonEnabled,
        allConditions = formViewModel.allConditions,
        configuredConditions = formViewModel.configuredConditions
    )
}

@Composable
fun OptionFormFields(
    imageUrl: String,
    name: String,
    onNameChanged: (String) -> Unit,
    onImageChanged: (Bitmap) -> Unit
) {
    val nameLabel = stringResource(id = R.string.name_option)
    ImagePickerField(imageUrl, onImageChanged)
    NoEmptyTextField(nameLabel, name, onNameChanged)
}

@Composable
fun OptionForm(
    onCreateNewOption: () -> Unit,
    formCompletedCallback: () -> Unit,
    option: Option,
    onNameChanged: (String) -> Unit,
    onImageChanged: (Bitmap) -> Unit,
    allConditions: List<Condition>,
    configuredConditions: List<Condition>,
    onConditionSelected: (Boolean, Condition) -> Unit,
    isButtonEnabled: Boolean
) {

    val scope = rememberCoroutineScope()

    fun onButtonFormClick() {
        onCreateNewOption()
        formCompletedCallback()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white)) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            FormDragControl()
            OptionFormFields(
                imageUrl = option.imageUrl,
                name = option.name,
                onNameChanged = onNameChanged,
                onImageChanged = onImageChanged
            )

            Text(
                stringResource(R.string.assign_requisites),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.dark_text)
            )
            if (configuredConditions.isEmpty()) {
                GetTextError(stringResource(R.string.tooltip_select_requisites))
            }
            ConditionsSelectionControl(
                allConditions = allConditions,
                conditionsConfigured = configuredConditions,
                onConditionSelected = onConditionSelected
            )
            Spacer(Modifier.weight(1f, false))
            GetBottomButton(
                {
                    scope.launch {
                        onButtonFormClick()
                    }
                },
                R.string.save_option,
                isButtonEnabled
            )
        }
    }
}