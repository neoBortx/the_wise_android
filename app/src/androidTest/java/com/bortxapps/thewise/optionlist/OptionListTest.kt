package com.bortxapps.thewise.optionlist

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.OptionsListScreen
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionsViewModel
import com.bortxapps.thewise.presentation.screens.utils.HiltActivity
import com.bortxapps.thewise.presentation.screens.utils.IsBadgeSelected
import com.bortxapps.thewise.ui.theme.TheWiseTheme
import com.bortxapps.thewise.utils.createFilledElection
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OptionListTest {

    private var electionService = mockk<IElectionsAppService>()
    private var conditionService = mockk<IConditionsAppService>()
    private var optionService = mockk<IOptionsAppService>()
    private var navHostController =
        TestNavHostController(ApplicationProvider.getApplicationContext())

    private var electionId = UUID.randomUUID().mostSignificantBits

    @BindValue
    @JvmField
    var optionFormViewModel = OptionFormViewModel(
        optionService,
        conditionService,
        ApplicationProvider.getApplicationContext()
    )

    @BindValue
    @JvmField
    var electionFormViewModel = ElectionFormViewModel(electionService, conditionService)

    @BindValue
    @JvmField
    var optionsViewModel = OptionsViewModel(optionService, electionService)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Test approach -> directly open the desired composable window instead of use the regular
     * activity. It is easier to test and mock
     */
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltActivity>()

    @Composable
    fun getElectionFormViewModel() = spyk(hiltViewModel<ElectionFormViewModel>())

    @Before
    fun setup() {
        hiltRule.inject()

        coEvery { electionService.addElection(any()) } returns 5
        coEvery { electionService.deleteElection(any()) } returns Unit
        coEvery { electionService.updateElection(any()) } returns Unit


        coEvery { conditionService.addCondition(any()) } returns Unit
        coEvery { conditionService.deleteCondition(any()) } returns Unit
        coEvery { conditionService.updateCondition(any()) } returns Unit

        coEvery { optionService.addOption(any()) } returns Unit
        coEvery { optionService.deleteOption(any()) } returns Unit
        coEvery { optionService.updateOption(any()) } returns Unit
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_list_with_no_options_expect_no_option_message() {

        val numConditions = 6

        val election =
            createFilledElection(
                electionId,
                "Name election",
                "description election",
                0,
                numConditions
            )

        every { electionService.getElection(electionId = election.id) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("top_title_test").assertTextEquals(election.name)
        composeTestRule.onNodeWithTag("no_option_col").assertIsDisplayed()
        composeTestRule.onNodeWithTag("no_option_col_text").assertIsDisplayed()
        composeTestRule.onNodeWithTag("no_option_col_image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_list_floating_button").assertIsDisplayed()

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_list_with_options_expect_a_filled_list_of_options() {
        val numConditions = 3
        val numOptions = 8
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                numOptions,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("option_list_column").onChildren()
            .assertCountEquals(numOptions)
        composeTestRule.onNodeWithTag("option_list_floating_button").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun click_option_card_expand_icon_expect_card_size_modified() {
        val numConditions = 7
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                1,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("option_card_matching_conditions_row", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("option_card_delete_button", useUnmergedTree = true)
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("option_card_expand_button").performClick()
        composeTestRule.onNodeWithTag("option_card_matching_conditions_row", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_card_matching_conditions_row", useUnmergedTree = true)
            .onChildren().assertCountEquals(numConditions)
        composeTestRule.onNodeWithTag("option_card_delete_button", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("option_card_expand_button").performClick()
        composeTestRule.onNodeWithTag("option_card_matching_conditions_row", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("option_card_delete_button", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_list_edit_current_eelection_expet_form_filled() {
        val numConditions = 6
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                1,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("menu_button_edit").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .assertTextEquals(election.name)
        composeTestRule.onNodeWithTag("election_form_description_text", useUnmergedTree = true)
            .assertTextEquals(election.description)
        composeTestRule.onAllNodesWithTag("removable_badge_condition", useUnmergedTree = true)
            .assertCountEquals(numConditions)

        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .performTextInput("ajdhgfksjfsk")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .performTextReplacement("")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_list_delete_current_election_expect_dialog_shown() {
        val numConditions = 12
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                1,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }
        composeTestRule.onNodeWithTag("menu_button_delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete_dialog_forget", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertDoesNotExist()

        composeTestRule.onNodeWithTag("menu_button_delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete_dialog_keep", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertDoesNotExist()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_press_floating_button_expect_new_option_form() {
        val numConditions = 5
        val numOptions = 5
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                numOptions,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }
        every { conditionService.getConditionsFromElection(electionId) } returns flow {
            emit(
                election.conditions
            )
        }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }
        composeTestRule.onNodeWithTag("option_form_column", useUnmergedTree = true)
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("option_list_floating_button").performClick()

        composeTestRule.onNodeWithTag("option_form_column", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("option_form_name", useUnmergedTree = true)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()

        composeTestRule.onNodeWithTag("option_form_name", useUnmergedTree = true)
            .performTextInput("new option")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()

        composeTestRule.onNodeWithTag(
            "option_form_condition_selection_row",
            useUnmergedTree = true
        ).onChildAt(0).performClick()
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithTag(
            "option_form_condition_selection_row",
            useUnmergedTree = true
        ).onChildAt(0).performClick()
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()

        composeTestRule.onNodeWithTag(
            "option_form_condition_selection_row",
            useUnmergedTree = true
        ).onChildAt(3).performClick()
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()

        composeTestRule.onNodeWithTag(
            "option_form_condition_selection_row",
            useUnmergedTree = true
        ).onChildAt(2).performClick()
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()

        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_option_press_option_expect_edit_option_form() {
        val numConditions = 5
        val numOptions = 5
        val election =
            createFilledElection(
                electionId,
                "Name election",
                "Description election",
                numOptions,
                numConditions
            )

        every { electionService.getElection(electionId) } returns flow { emit(election) }
        every { conditionService.getConditionsFromElection(electionId) } returns flow {
            emit(
                election.conditions
            )
        }

        composeTestRule.setContent {
            TheWiseTheme {
                OptionsListScreen(
                    optionsViewModel,
                    optionFormViewModel,
                    electionFormViewModel,
                    electionId,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("option_list_column").onChildAt(3).performClick()
        composeTestRule.onNodeWithTag("option_form_column", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_form_name", useUnmergedTree = true)
            .assertTextEquals(election.options[3].name)
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()

        composeTestRule.onNodeWithTag("option_form_name", useUnmergedTree = true)
            .performTextInput("4")

        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onAllNodes(SemanticsMatcher.expectValue(IsBadgeSelected, true))
            .assertCountEquals(numConditions)
        composeTestRule.onNodeWithTag(
            "option_form_condition_selection_row",
            useUnmergedTree = true
        ).onChildAt(0).performClick()
        composeTestRule.onAllNodes(SemanticsMatcher.expectValue(IsBadgeSelected, true))
            .assertCountEquals(numConditions - 1)
    }

}