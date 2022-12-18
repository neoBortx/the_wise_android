package com.bortxapps.thewise.permissions

data class PermissionComponentState(
    val acceptedPermissions: Boolean,
    val deniedPermissions: Boolean,
    val showRationale: Boolean,
    val askPermissions: Boolean,
)