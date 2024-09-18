package com.abz.agency.testtask.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavItem(
    @StringRes val screenNameId: Int,
    @DrawableRes val selectedIconId: Int,
    val route: String,
)