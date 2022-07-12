package com.vanced.manager.origin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanced.manager.origin.R
import com.vanced.manager.origin.adapter.GetNotifAdapter
import com.vanced.manager.origin.core.ui.base.BindingFragment
import com.vanced.manager.origin.core.ui.ext.showDialog
import com.vanced.manager.origin.databinding.FragmentSettingsBinding
import com.vanced.manager.origin.ui.dialogs.*
import com.vanced.manager.origin.utils.accentColor
import com.vanced.manager.origin.utils.defAccentColor
import com.vanced.manager.origin.utils.getLanguageFormat
import com.vanced.manager.origin.utils.toHex
import java.io.File

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private companion object {
        const val LIGHT = "Light"
        const val DARK = "Dark"
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    private lateinit var variant: String
    private lateinit var parentActivity: FragmentActivity

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        setHasOptionsMenu(true)
        parentActivity = requireActivity()
        bindData()
    }

    private fun bindData() {
        with(binding) {
            variant = prefs.getString("vanced_variant", "nonroot").toString()
            bindRecycler()
            bindManagerVariant()
            bindServiceDTimer()
            bindClearFiles()
            bindManagerTheme()
            bindManagerAccentColor()
            bindManagerLanguage()
            selectApps.setOnClickListener { showDialog(SelectAppsDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindRecycler() {
        notificationsRecycler.apply {
            layoutManager = LinearLayoutManager(parentActivity)
            adapter = GetNotifAdapter(parentActivity)
        }
    }

    private fun FragmentSettingsBinding.bindManagerVariant() {
        managerVariant.apply {
            setSummary(variant)
            setOnClickListener { showDialog(ManagerVariantDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindServiceDTimer() {
        servicedTimer.apply {
            if (variant == "root") this.isVisible = true
            setOnClickListener { showDialog(ServiceDTimerDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindClearFiles() {
        clearFiles.setOnClickListener {
            with(requireActivity()) {
                listOf(
                    "vanced/nonroot",
                    "vanced/root",
                    "music/nonroot",
                    "music/root",
                    "microg"
                ).forEach { dir ->
                    File(getExternalFilesDir(dir)?.path.toString()).deleteRecursively()
                }
                Toast.makeText(this, getString(R.string.cleared_files), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun FragmentSettingsBinding.bindManagerTheme() {
        val themePref = prefs.getString("manager_theme", "System Default")
        managerTheme.apply {
            setSummary(
                when (themePref) {
                    LIGHT -> getString(R.string.theme_light)
                    DARK -> getString(R.string.theme_dark)
                    else -> getString(R.string.system_default)
                }
            )
            setOnClickListener { showDialog(ManagerThemeDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindManagerAccentColor() {
        managerAccentColor.apply {
            setSummary(prefs.getInt("manager_accent_color", defAccentColor).toHex())
            setOnClickListener { showDialog(ManagerAccentColorDialog()) }
            accentColor.observe(viewLifecycleOwner) {
                setSummary(it.toHex())
            }
        }
    }

    private fun FragmentSettingsBinding.bindManagerLanguage() {
        val langPref = prefs.getString("manager_lang", "System Default")
        managerLanguage.apply {
            setSummary(getLanguageFormat(parentActivity, requireNotNull(langPref)))
            setOnClickListener { showDialog(ManagerLanguageDialog()) }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val devSettings =
            getDefaultSharedPreferences(requireActivity()).getBoolean("devSettings", false)
        if (devSettings) {
            inflater.inflate(R.menu.dev_settings_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}