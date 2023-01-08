package com.bortxapps.thewise.presentation.screens.utils

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

// Creates a Semantics property of type boolean
val IsBadgeSelected = SemanticsPropertyKey<Boolean>("IsBadgeSelected")
var SemanticsPropertyReceiver.isBadgeSelected by IsBadgeSelected