package com.bortxapps.thewise.permissions

data class PermissionData(
    /**
    This is the manifest property to check, example: Manifest.permission.CAMERA
     */
    val permission: String,
    /**
     * When the users rejects the permission. If they don't check not answer again
     * the application will ask in a polite way to check again the permissions with
     * the content of this string
     */
    val rationaleMessage: String,
    /**
     * The message to show when the user reject permissions. This message will include
     * the consequences of the rejection
     */
    val dismissMessage: String,
    /**
     * When the user is asked again to check the permissions, the message is shown in a snack bar
     * with an action button. This is the message of the action button
     */
    val checkPermissionsActionMessage: String,
    /**
     * Call back called when the user accepts the permission
     */
    val onAccepted: () -> Unit,
    /**
     * Call back called when permissions are rejected
     */
    val onDismiss: () -> Unit
)
