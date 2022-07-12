package com.vanced.manager.origin.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.beust.klaxon.JsonObject
import com.vanced.manager.origin.utils.PackageHelper

class RootDataModel(
    jsonObject: LiveData<JsonObject?>,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    appPkg: String,
    appName: String,
    appDescription: String,
    @DrawableRes appIcon: Int,
    //BUG THIS!
    //kotlin thinks that this value is null if we use
    //private val scriptName: String
    //Although it's impossible for it to be null.
    //Ironic, isn't it?
    private val scriptName: String?
) : DataModel(
    jsonObject, context, lifecycleOwner, appPkg, appName, appDescription, appIcon
) {

    override fun isAppInstalled(pkg: String): Boolean {
        //Adapt to nullable shit
        return if (scriptName?.let { PackageHelper.scriptExists(it) } == true) {
            super.isAppInstalled(appPkg)
        } else {
            false
        }
    }

}