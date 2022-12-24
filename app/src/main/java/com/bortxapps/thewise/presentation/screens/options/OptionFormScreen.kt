package com.bortxapps.thewise.presentation.screens.options

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.bortxapps.thewise.presentation.components.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.components.conditions.ConditionsSelectionControl
import com.bortxapps.thewise.presentation.components.form.FormDragControl
import com.bortxapps.thewise.presentation.components.form.ImageFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.components.form.NoEmptyTextField
import com.bortxapps.thewise.presentation.components.text.TextError.GetTextError
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormState
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OptionFormScreen(
    formViewModel: OptionFormViewModel = hiltViewModel(),
    formCompletedCallback: () -> Unit
) {
    OptionForm(
        onCreateNewOption = { formViewModel.createNewOption() },
        formCompletedCallback = formCompletedCallback,
        state = formViewModel.state,
        onNameChanged = { name -> formViewModel.setName(name) },
        onImageChanged = { image -> formViewModel.setImage(image) },
        onConditionSelected = { sel, cond -> formViewModel.selectCondition(sel, cond) }
    )
}

@Composable
fun OptionFormFields(
    option: Option,
    onNameChanged: (String) -> Unit,
    onImageChanged: (Uri) -> Unit,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    ImagePickerField(option.imageUrl, onImageChanged, snackBarHostState, scope)
    NoEmptyTextField(stringResource(id = R.string.name_option), option.name, onNameChanged)
}

@Composable
fun OptionForm(
    onCreateNewOption: () -> Unit,
    formCompletedCallback: () -> Unit,
    state: OptionFormState,
    onNameChanged: (String) -> Unit,
    onImageChanged: (Uri) -> Unit,
    onConditionSelected: (Boolean, Condition) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    fun onButtonFormClick() {
        onCreateNewOption()
        formCompletedCallback()
    }

    val scope = rememberCoroutineScope()

    Scaffold(backgroundColor = colorResource(id = R.color.white),
        snackbarHost = { SnackbarHost(snackBarHostState) })
    {

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
                option = state.option,
                onNameChanged = onNameChanged,
                onImageChanged = onImageChanged,
                snackBarHostState = snackBarHostState,
                scope = scope
            )

            Text(
                stringResource(R.string.assign_requisites),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.dark_text)
            )
            if (state.option.matchingConditions.isEmpty()) {
                GetTextError(stringResource(R.string.tooltip_select_requisites))
            }
            ConditionsSelectionControl(
                allConditions = state.allConditions,
                conditionsConfigured = state.option.matchingConditions,
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
                state.isButtonEnabled
            )
        }
    }
}