package com.abz.agency.testtask.presentation.navigation

sealed class AppScreens(val route: String) {

    data object UsersScreen: AppScreens("users")
    data object SignUpScreen: AppScreens("sign_up")
}