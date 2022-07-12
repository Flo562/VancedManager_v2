package com.vanced.manager.origin.feature.home.domain.usecase

import com.vanced.manager.origin.feature.home.domain.entity.AppInfo
import com.vanced.manager.origin.feature.home.domain.repository.AppsRepository

class GetAppsInfoUseCase(
    private val repository: AppsRepository
) {

    suspend operator fun invoke(): List<AppInfo> =
        repository.getAppsInfo()
}