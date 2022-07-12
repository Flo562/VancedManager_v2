package com.vanced.manager.origin.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vanced.manager.origin.R
import com.vanced.manager.origin.utils.openUrl

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    fun openUrl(url: String) {
        openUrl(url, R.color.GitHub, getApplication())
    }

}