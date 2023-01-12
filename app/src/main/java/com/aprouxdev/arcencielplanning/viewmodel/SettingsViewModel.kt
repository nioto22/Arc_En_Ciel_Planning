package com.aprouxdev.arcencielplanning.viewmodel

import androidx.lifecycle.ViewModel
import com.aprouxdev.arcencielplanning.data.services.local.UserService
import com.aprouxdev.arcencielplanning.data.services.local.database
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged


class SettingsViewModel: ViewModel() {

    val user: Flow<List<UserDb>> =
        database.userDbQueries
            .getAllUserDbs()
            .asFlow()
            .mapToList()
            .distinctUntilChanged()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading




}