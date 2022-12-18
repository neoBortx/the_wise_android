package com.bortxapps.thewise.permissions

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PermissionsViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    var componentState = MutableStateFlow(
        PermissionComponentState(
            acceptedPermissions = false,
            deniedPermissions = false,
            showRationale = false,
            askPermissions = false
        )
    )
        private set

    fun initialize() {
        viewModelScope.launch {
            componentState.emit(
                PermissionComponentState(
                    acceptedPermissions = false,
                    deniedPermissions = false,
                    showRationale = false,
                    askPermissions = false
                )
            )
        }
    }

    fun processRequestPermissionResponse(isGranted: Boolean) {
        viewModelScope.launch {
            componentState.emit(
                componentState.value.copy(
                    acceptedPermissions = isGranted,
                    deniedPermissions = !isGranted
                )
            )
        }
    }

    fun determineAction(permissionState: Int, shouldRationale: Boolean) {
        viewModelScope.launch {
            when {
                permissionState == PackageManager.PERMISSION_GRANTED -> {
                    componentState.emit(componentState.value.copy(acceptedPermissions = true))
                }

                shouldRationale -> {
                    componentState.emit(componentState.value.copy(showRationale = true))
                }

                else -> {
                    componentState.emit(componentState.value.copy(askPermissions = true))
                }
            }
        }
    }
}