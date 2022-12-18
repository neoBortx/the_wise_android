package com.bortxapps.thewise.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RequestSinglePermission(
    permissionData: PermissionData,
    context: Context,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: PermissionsViewModel = hiltViewModel(),
) {

    viewModel.initialize()

    val uiState = viewModel.componentState.collectAsState()

    CalculateRequestOperation(
        permissionData,
        context,
        snackBarHostState,
        uiState.value,
        scope,
        onProcessRequestPermissionResponse = { viewModel.processRequestPermissionResponse(it) },
        onCalculatePermission = { permissionState, shouldRationale ->
            viewModel.determineAction(
                permissionState,
                shouldRationale
            )
        }
    )
}

@Composable
fun CalculateRequestOperation(
    permissionData: PermissionData,
    context: Context,
    snackBarHostState: SnackbarHostState,
    componentState: PermissionComponentState,
    scope: CoroutineScope,
    onProcessRequestPermissionResponse: (Boolean) -> Unit,
    onCalculatePermission: (Int, Boolean) -> Unit
) {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onProcessRequestPermissionResponse(isGranted)
        }

    val permissionState = ContextCompat.checkSelfPermission(context, permissionData.permission)

    val shouldRationale =
        shouldShowRequestPermissionRationale(context as Activity, permissionData.permission)

    onCalculatePermission(permissionState, shouldRationale)

    if (componentState.deniedPermissions) {
        DismissMessage(permissionData.dismissMessage, snackBarHostState, scope)
        Log.d("permissions", "showing snack bar message")
        permissionData.onDismiss()
    }

    if (componentState.acceptedPermissions) {
        permissionData.onAccepted()
    }

    if (componentState.showRationale) {
        Log.d("permissions", "showing rationale")
        Rationale(
            message = permissionData.rationaleMessage,
            actionMessage = permissionData.checkPermissionsActionMessage,
            snackBarHostState = snackBarHostState,
            scope = scope,
            permission = permissionData.permission,
            onProcessRequestPermissionResponse = onProcessRequestPermissionResponse
        )
    }

    if (componentState.askPermissions) {
        LaunchedEffect(Unit) {
            launcher.launch(permissionData.permission)
        }
    }
}

@Composable
private fun DismissMessage(
    message: String,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    LaunchedEffect(scope) {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Long
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Rationale(
    message: String,
    actionMessage: String,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    permission: String,
    onProcessRequestPermissionResponse: (Boolean) -> Unit,
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onProcessRequestPermissionResponse(isGranted)
        }

    scope.launch {
        val res = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionMessage,
            duration = SnackbarDuration.Long
        )

        if (res == SnackbarResult.ActionPerformed) {
            launcher.launch(permission)
        }
    }
}

