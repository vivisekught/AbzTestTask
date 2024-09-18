package com.abz.agency.testtask.presentation.screens.sign_up.vm

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abz.agency.testtask.core.api.Resource
import com.abz.agency.testtask.core.api.UploadUserErrorType
import com.abz.agency.testtask.domain.entity.UserEntity
import com.abz.agency.testtask.domain.usecases.GetPositionsUseCase
import com.abz.agency.testtask.domain.usecases.UploadUserUseCase
import com.abz.agency.testtask.presentation.screens.sign_up.state.RegistrationDialogType
import com.abz.agency.testtask.presentation.screens.sign_up.state.SignUpScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignUpScreenEvent {

    data class OnNameChange(val name: String) : SignUpScreenEvent()

    data class OnEmailChange(val email: String) : SignUpScreenEvent()

    data class OnPhoneChange(val phone: String) : SignUpScreenEvent()

    data class OnPositionChange(val positionId: Int) : SignUpScreenEvent()

    data class OnPhotoChange(val photoPath: String) : SignUpScreenEvent()

    data object OnSignUpClick : SignUpScreenEvent()

    data object OnCloseDialogClick : SignUpScreenEvent()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getPositionsUseCase: GetPositionsUseCase,
    private val uploadUserUseCase: UploadUserUseCase,
) : ViewModel() {

    var state by mutableStateOf(SignUpScreenState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(positions = getPositionsUseCase())
        }
    }

    fun onEvent(event: SignUpScreenEvent) {
        when (event) {
            is SignUpScreenEvent.OnEmailChange -> {
                changeEmail(event.email)
            }

            is SignUpScreenEvent.OnNameChange -> {
                changeName(event.name)
            }

            SignUpScreenEvent.OnSignUpClick -> {
                signUp()
            }

            is SignUpScreenEvent.OnPhoneChange -> {
                changePhone(event.phone)
            }

            is SignUpScreenEvent.OnPhotoChange -> {
                changePhoto(event.photoPath)
            }

            is SignUpScreenEvent.OnPositionChange -> {
                changePosition(event.positionId)
            }

            SignUpScreenEvent.OnCloseDialogClick -> {
                closeRegistrationDialog()
            }
        }
    }

    private fun changeName(name: String) {
        state = state.copy(name = name, nameErrorMessage = "")
    }

    private fun changeEmail(email: String) {
        state = state.copy(email = email, emailErrorMessage = "")
    }

    private fun changePhone(phone: String) {
        state = state.copy(phone = phone, phoneErrorMessage = "")
    }

    private fun changePosition(positionId: Int) {
        state = state.copy(positionId = positionId)
    }

    private fun changePhoto(photoPath: String) {
        val photoName = photoPath.substringAfterLast("/")
        state = state.copy(photoPath = photoPath, photoErrorMessage = "", photoName = photoName)
    }

    private fun signUp() {
        // Check if the form is valid
        if (!validateForm()) {
            return
        }
        // Upload the user
        viewModelScope.launch {
            val result = uploadUserUseCase(
                UserEntity(
                    name = state.name,
                    email = state.email,
                    phone = state.phone,
                    positionId = state.positionId,
                    photoPath = state.photoPath
                )
            )
            result.collect {
                when (it) {
                    Resource.Loading -> Unit
                    is Resource.Failure<*> -> {
                        state = when (it.errorType as UploadUserErrorType) {
                            UploadUserErrorType.OtherError -> {
                                state.copy(
                                    registrationDialogState = state.registrationDialogState.copy(
                                        shouldShowDialog = true,
                                        type = RegistrationDialogType.FAILURE
                                    )
                                )
                            }

                            UploadUserErrorType.UserAlreadyExists -> {
                                state.copy(
                                    registrationDialogState = state.registrationDialogState.copy(
                                        shouldShowDialog = true,
                                        type = RegistrationDialogType.USER_ALREADY_EXISTS
                                    )
                                )
                            }
                        }
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            registrationDialogState = state.registrationDialogState.copy(
                                shouldShowDialog = true,
                                type = RegistrationDialogType.SUCCESS
                            )
                        )
                    }
                }
            }
        }
    }

    private fun closeRegistrationDialog() {
        state =
            state.copy(
                registrationDialogState = state.registrationDialogState.copy(
                    shouldShowDialog = false,
                    type = RegistrationDialogType.NONE
                )
            )
    }

    private fun validateForm(): Boolean {
        val isNameCorrect = isNameValid()
        val isEmailCorrect = isEmailValid()
        val isPhoneCorrect = isPhoneNumberValid()
        val isPhotoCorrect = isPhotoValid()
        // Check if all fields are valid
        return isNameCorrect && isEmailCorrect && isPhoneCorrect && isPhotoCorrect
    }

    private fun isNameValid(): Boolean {
        // Check if the name field is empty
        if (state.name.isBlank()) {
            state = state.copy(nameErrorMessage = "Required field")
            return false
        }
        // Check if the name is between 2 and 60 characters
        if (state.name.length !in 2..60) {
            state = state.copy(nameErrorMessage = "Name must be between 2 and 60 characters")
            return false
        }
        return true
    }

    private fun isEmailValid(): Boolean {
        // Check if the email field is empty
        if (state.email.isBlank()) {
            state = state.copy(emailErrorMessage = "Required field")
            return false
        }
        // Check if the email address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            state = state.copy(emailErrorMessage = "Invalid email address")
            return false
        }
        return true
    }

    private fun isPhoneNumberValid(): Boolean {
        // Check if the phone field is empty
        if (state.phone.isBlank()) {
            state = state.copy(phoneErrorMessage = "Required field")
            return false
        }
        // Define a regex pattern for Ukrainian phone numbers
        val phonePattern = "^\\+380\\d{9}$".toRegex()
        // Check if the phone number matches the pattern
        val isValidPhoneNumber = phonePattern.matches(state.phone)
        if (!isValidPhoneNumber) {
            state = state.copy(phoneErrorMessage = "Invalid phone number")
            return false
        }
        return true
    }

    private fun isPhotoValid(): Boolean = if (state.photoPath.isBlank()) {
        state = state.copy(photoErrorMessage = "Required field")
        false
    } else {
        true
    }
}