package com.vanced.manager.origin.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.origin.core.ui.base.BindingFragment
import com.vanced.manager.origin.feature.home.databinding.FragmentHomeBinding
import com.vanced.manager.origin.feature.home.presentation.HomeViewModel
import com.vanced.manager.origin.feature.home.ui.bind.bindData
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData(binding, viewModel)
    }
}