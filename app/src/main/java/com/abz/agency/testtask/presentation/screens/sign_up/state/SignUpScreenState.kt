package com.abz.agency.testtask.presentation.screens.sign_up.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.abz.agency.testtask.R
import com.abz.agency.testtask.domain.entity.PositionEntity

data class SignUpScreenState(
    val positions: List<PositionEntity> = listOf(),
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val positionId: Int = 1,
    val photoPath: String = "",
    val photoName: String = "",
    val nameErrorMessage: String = "",
    val emailErrorMessage: String = "",
    val phoneErrorMessage: String = "",
    val photoErrorMessage: String = "",
    val registrationDialogState: RegistrationDialogState = RegistrationDialogState(),
)

data class RegistrationDialogState(
    val shouldShowDialog: Boolean = false,
    val type: RegistrationDialogType = RegistrationDialogType.NONE,
)

sealed class RegistrationDialogType(
    @StringRes val messageResId: Int,
    @StringRes val btnTextResId: Int,
    @DrawableRes val iconResId: Int,
) {

    data object NONE : RegistrationDialogType(0, 0, 0)
    data object SUCCESS : RegistrationDialogType(
        R.string.user_successfully_registered,
        R.string.got_it,
        R.drawable.register_success
    )

    data object FAILURE : RegistrationDialogType(
        R.string.unexpected_error,
        R.string.try_again,
        R.drawable.register_failure
    )

    data object USER_ALREADY_EXISTS : RegistrationDialogType(
        R.string.that_email_is_already_registered,
        R.string.try_again,
        R.drawable.register_failure
    )
}