package com.vanced.manager.origin.ui.viewmodels

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.origin.R
import com.vanced.manager.origin.adapter.LinkAdapter.Companion.TELEGRAM
import com.vanced.manager.origin.model.ButtonTag
import com.vanced.manager.origin.model.DataModel
import com.vanced.manager.origin.model.RootDataModel
import com.vanced.manager.origin.ui.dialogs.AppDownloadDialog
import com.vanced.manager.origin.ui.dialogs.InstallationFilesDetectedDialog
import com.vanced.manager.origin.ui.dialogs.MusicPreferencesDialog
import com.vanced.manager.origin.ui.dialogs.VancedPreferencesDialog
import com.vanced.manager.origin.utils.*
import com.vanced.manager.origin.utils.AppUtils.log
import com.vanced.manager.origin.utils.AppUtils.managerPkg
import com.vanced.manager.origin.utils.AppUtils.microgPkg
import com.vanced.manager.origin.utils.AppUtils.musicPkg
import com.vanced.manager.origin.utils.AppUtils.musicRootPkg
import com.vanced.manager.origin.utils.AppUtils.vancedPkg
import com.vanced.manager.origin.utils.AppUtils.vancedRootPkg
import com.vanced.manager.origin.utils.PackageHelper.apkExist
import com.vanced.manager.origin.utils.PackageHelper.musicApkExists
import com.vanced.manager.origin.utils.PackageHelper.uninstallApk
import com.vanced.manager.origin.utils.PackageHelper.uninstallRootApk
import com.vanced.manager.origin.utils.PackageHelper.vancedInstallFilesExist
import kotlinx.coroutines.launch

//TODO fix leak
@SuppressLint("StaticFieldLeak")
class HomeViewModel(private val activity: FragmentActivity) : ViewModel() {

    private val prefs = getDefaultSharedPreferences(activity)
    private val variant get() = prefs.getString("vanced_variant", "nonroot")

    val vancedModel = MutableLiveData<DataModel>()
    val vancedRootModel = MutableLiveData<RootDataModel>()
    val microgModel = MutableLiveData<DataModel>()
    val musicModel = MutableLiveData<DataModel>()
    val musicRootModel = MutableLiveData<RootDataModel>()
    val managerModel = MutableLiveData<DataModel>()

    fun fetchData() {
        viewModelScope.launch {
            loadJson(activity)
        }
    }

    private val microgToast = Toast.makeText(activity, R.string.no_microg, Toast.LENGTH_LONG)

    fun openUrl(url: String) {
        val color: Int =
            when (url) {
                TELEGRAM -> R.color.Telegram
                else -> R.color.Vanced
            }

        openUrl(url, color, activity)
    }

    fun launchApp(app: String, isRoot: Boolean) {
        val componentName = when (app) {
            activity.getString(R.string.vanced) -> if (isRoot) ComponentName(
                vancedRootPkg,
                "$vancedRootPkg.HomeActivity"
            ) else ComponentName(vancedPkg, "$vancedRootPkg.HomeActivity")
            activity.getString(R.string.music) -> if (isRoot) ComponentName(
                musicRootPkg,
                "$musicRootPkg.activities.MusicActivity"
            ) else ComponentName(musicPkg, "$musicRootPkg.activities.MusicActivity")
            activity.getString(R.string.microg) -> ComponentName(
                microgPkg,
                "org.microg.gms.ui.SettingsActivity"
            )
            else -> throw IllegalArgumentException("Can't open this app")
        }
        try {
            activity.startActivity(Intent().setComponent(componentName))
        } catch (e: ActivityNotFoundException) {
            log("VMHMV", e.toString())
        }

    }

    fun openInstallDialog(buttonTag: ButtonTag?, app: String) {
        if (variant == "nonroot" && app != activity.getString(R.string.microg) && !microgModel.value?.isAppInstalled?.value!!) {
            microgToast.show()
            return
        }

        if (buttonTag == ButtonTag.UPDATE) {
            when (app) {
                activity.getString(R.string.vanced) -> VancedPreferencesDialog().show(activity)
                activity.getString(R.string.music) -> MusicPreferencesDialog().show(activity)
                else -> AppDownloadDialog.newInstance(app).show(activity)
            }
            return
        }

        when (app) {
            activity.getString(R.string.vanced) -> {
                when (variant) {
                    "nonroot" -> {
                        if (vancedInstallFilesExist(activity)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(activity)
                        } else {
                            VancedPreferencesDialog().show(activity)
                        }
                    }
                    "root" -> {
                        VancedPreferencesDialog().show(activity)
                    }
                }
            }
            activity.getString(R.string.music) -> {
                when (variant) {
                    "nonroot" -> {
                        if (musicApkExists(activity)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(activity)
                        } else {
                            MusicPreferencesDialog().show(activity)
                        }
                    }
                    "root" -> {
                        MusicPreferencesDialog().show(activity)
                    }
                }
            }
            activity.getString(R.string.microg) -> {
                if (apkExist(activity, "microg.apk")) {
                    InstallationFilesDetectedDialog.newInstance(app).show(activity)
                } else {
                    AppDownloadDialog.newInstance(app).show(activity)
                }
            }
        }

    }

    fun uninstallPackage(pkg: String) {
        if (variant == "root" && uninstallRootApk(pkg)) {
            viewModelScope.launch { loadJson(activity) }
        } else {
            uninstallApk(pkg, activity)
        }
    }

    init {
        with(activity) {
            if (variant == "root") {
                vancedRootModel.value = RootDataModel(
                    vanced,
                    this,
                    this,
                    vancedRootPkg,
                    this.getString(R.string.vanced),
                    activity.getString(R.string.description_vanced),
                    R.drawable.ic_vanced,
                    "vanced"
                )
                musicRootModel.value = RootDataModel(
                    music,
                    this,
                    this,
                    musicRootPkg,
                    this.getString(R.string.music),
                    activity.getString(R.string.description_vanced_music),
                    R.drawable.ic_music,
                    "music"
                )
            } else {
                vancedModel.value = DataModel(
                    vanced,
                    this,
                    this,
                    vancedPkg,
                    this.getString(R.string.vanced),
                    activity.getString(R.string.description_vanced),
                    R.drawable.ic_vanced
                )
                musicModel.value = DataModel(
                    music,
                    this,
                    this,
                    musicPkg,
                    this.getString(R.string.music),
                    activity.getString(R.string.description_vanced_music),
                    R.drawable.ic_music
                )
                microgModel.value = DataModel(
                    microg,
                    this,
                    this,
                    microgPkg,
                    this.getString(R.string.microg),
                    activity.getString(R.string.description_microg),
                    R.drawable.ic_microg
                )
            }
            managerModel.value = DataModel(
                manager,
                this,
                this,
                managerPkg,
                this.getString(R.string.app_name),
                "Just manager meh",
                R.mipmap.ic_launcher
            )
        }
    }
}
