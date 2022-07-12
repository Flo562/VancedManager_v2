package com.vanced.manager.origin.feature.home.domain.repository

import com.vanced.manager.origin.feature.home.domain.entity.AppInfo

interface AppsRepository {

    suspend fun getAppsInfo(): List<AppInfo>
}