package com.bortxapps.thewise.navigation

import androidx.compose.material.icons.Icons
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.NavigationConstants.CONDITIONS_SCREEN_ARGUMENT_ID
import com.bortxapps.thewise.navigation.NavigationConstants.ELECTION_SCREEN_ARGUMENT_ID
import com.bortxapps.thewise.navigation.NavigationConstants.OPTIONS_SCREEN_ARGUMENT_ID

sealed class Screen(
    val routeSourcePath: String,
    //Just for tab navigation
    val label: Int = 0,
    //Just for tab navigation
    val iconId: Int = 0
) {
    val MyAppIcons = Icons.Rounded

    abstract fun getFullRoute(): String

    fun getRouteWithId(electionId: String): String {
        return "$routeSourcePath/$electionId"
    }

    object Splash : Screen("splash_screen") {
        override fun getFullRoute(): String {
            return routeSourcePath
        }
    }

    //region elections
    object Home : Screen("home_screen") {
        override fun getFullRoute(): String {
            return routeSourcePath
        }
    }

    object ElectionForm : Screen("election_form_screen/{$ELECTION_SCREEN_ARGUMENT_ID}") {
        override fun getFullRoute(): String {
            return routeSourcePath
        }
    }

    object InfoElection : Screen(
        "info_election_screen",
        R.string.questions,
        R.drawable.round_contact_support_20
    ) {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$ELECTION_SCREEN_ARGUMENT_ID}"
        }
    }
    //endregion

    //region options
    object OptionsList :
        Screen(
            "options_list_screen",
            R.string.options,
            R.drawable.round_lightbulb_20
        ) {

        override fun getFullRoute(): String {
            return "$routeSourcePath/{$ELECTION_SCREEN_ARGUMENT_ID}"
        }
    }

    object OptionForm : Screen("option_form_screen") {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$OPTIONS_SCREEN_ARGUMENT_ID}"
        }
    }

    object InfoOption : Screen("info_option_screen") {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$OPTIONS_SCREEN_ARGUMENT_ID}"
        }
    }
    //endregion

    //region conditions
    object ConditionsList :
        Screen("conditions_list_screen", R.string.conditions, R.drawable.round_fact_check_20) {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$ELECTION_SCREEN_ARGUMENT_ID}"
        }
    }

    object InfoCondition :
        Screen("info_conditions_screen") {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$CONDITIONS_SCREEN_ARGUMENT_ID}"
        }
    }

    object ConditionForm : Screen("condition_form_screen") {
        override fun getFullRoute(): String {
            return "$routeSourcePath/{$CONDITIONS_SCREEN_ARGUMENT_ID}"
        }
    }
}
