package com.abz.agency.testtask.presentation.ui.elements

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abz.agency.testtask.presentation.ui.theme.InputFieldFocusedBorderColor
import com.abz.agency.testtask.presentation.ui.theme.InputFieldUnfocusedBorderColor

@Composable
fun PrimaryInputField(
    modifier: Modifier = Modifier,
    text: String,
    label: String = "",
    enabled: Boolean = true,
    supportingText: String = "",
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    trailingIcon: @Composable () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(text = label) },
        supportingText = { Text(text = supportingText) },
        isError = isError,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = InputFieldUnfocusedBorderColor,
            unfocusedBorderColor = InputFieldUnfocusedBorderColor,
            focusedBorderColor = InputFieldFocusedBorderColor,
            focusedLabelColor = InputFieldFocusedBorderColor,
            cursorColor = InputFieldFocusedBorderColor,
        )
    )
}