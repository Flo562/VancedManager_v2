package com.vanced.manager.origin.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.vanced.manager.origin.R
import com.vanced.manager.origin.core.ui.base.BindingDialogFragment
import com.vanced.manager.origin.databinding.DialogServicedTimerBinding
import com.vanced.manager.origin.utils.PackageHelper
import com.vanced.manager.origin.utils.PackageHelper.getPackageDir
import com.vanced.manager.origin.utils.PackageHelper.getPkgNameRoot
import com.vanced.manager.origin.utils.PackageHelper.scriptExists
import com.vanced.manager.origin.utils.defPrefs
import com.vanced.manager.origin.utils.writeServiceDScript
import java.io.IOException
import java.util.*

class ServiceDTimerDialog : BindingDialogFragment<DialogServicedTimerBinding>() {

    private val prefs by lazy { requireActivity().defPrefs }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogServicedTimerBinding = DialogServicedTimerBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            servicedSlider.value = prefs.getInt("serviced_sleep_timer", 1).toFloat()
            servicedCancel.setOnClickListener {
                dismiss()
            }
            servicedSave.setOnClickListener {
                try {
                    arrayOf("vanced", "music").forEach { app ->
                        if (scriptExists(app)) {
                            val apkFPath =
                                "${PackageHelper.apkInstallPath}/${app.capitalize(Locale.ROOT)}/base.apk"
                            getPackageDir(
                                requireActivity(),
                                getPkgNameRoot(app)
                            )?.let { it1 ->
                                requireActivity().writeServiceDScript(
                                    apkFPath,
                                    it1,
                                    app
                                )
                            }
                        }
                    }
                } catch (e: IOException) {
                    Toast.makeText(
                        requireActivity(),
                        R.string.script_save_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                prefs.edit {
                    putInt("serviced_sleep_timer", servicedSlider.value.toInt())
                }
                dismiss()
            }
        }
    }

}