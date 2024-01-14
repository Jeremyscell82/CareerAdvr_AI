package com.lloydsbyte.core.customdialog

/** THIS CLASS HOLDS THE DATA NEEDED TO LAUNCH THE CUSTOM DIALOG**/
data class CustomDialogModel(
    val title: String,
    val message: String,
    val okBtn: String,
    val cancelBtn: String?,
    val okRunnable: Runnable?,
    val cancelRunnable: Runnable?,
    val cancelable: Boolean
)
