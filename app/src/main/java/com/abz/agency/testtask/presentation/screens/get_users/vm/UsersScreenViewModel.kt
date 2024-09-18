package com.abz.agency.testtask.presentation.screens.get_users.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abz.agency.testtask.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersScreenViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase
): ViewModel() {

    val users = getUsersUseCase(viewModelScope)
}