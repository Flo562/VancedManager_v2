package com.vanced.manager.origin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.origin.core.ui.base.BindingFragment
import com.vanced.manager.origin.databinding.FragmentWelcomeBinding
import com.vanced.manager.origin.ui.WelcomeActivity

class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWelcomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        binding.welcomeGetStarted.setOnClickListener {
            (requireActivity() as WelcomeActivity).navigateTo(1)
        }
    }
}