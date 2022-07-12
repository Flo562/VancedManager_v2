package com.vanced.manager.origin.model

data class SelectAppModel(
    val appName: String,
    val appDescription: String,
    val tag: String,
    var isChecked: Boolean
)