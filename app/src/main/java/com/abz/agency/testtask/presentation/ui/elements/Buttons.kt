package com.abz.agency.testtask.presentation.ui.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.abz.agency.testtask.presentation.ui.theme.PrimaryButtonContainerColor
import com.abz.agency.testtask.presentation.ui.theme.PrimaryButtonContainerDisabledColor
import com.abz.agency.testtask.presentation.ui.theme.PrimaryButtonContentColor
import com.abz.agency.testtask.presentation.ui.theme.PrimaryButtonDisabledContentColor
import com.abz.agency.testtask.presentation.ui.theme.PrimaryButtonPressedContainerColor
import com.abz.agency.testtask.presentation.ui.theme.SecondaryButtonContainerColor
import com.abz.agency.testtask.presentation.ui.theme.SecondaryButtonContainerDisabledColor
import com.abz.agency.testtask.presentation.ui.theme.SecondaryButtonContentColor
import com.abz.agency.testtask.presentation.ui.theme.SecondaryButtonDisabledContentColor
import com.abz.agency.testtask.presentation.ui.theme.SecondaryButtonPressedContainerColor

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) PrimaryButtonPressedContainerColor else PrimaryButtonContainerColor,
            contentColor = PrimaryButtonContentColor,
            disabledContainerColor = PrimaryButtonContainerDisabledColor,
            disabledContentColor = PrimaryButtonDisabledContentColor,
        ), modifier = modifier, enabled = enabled, interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) SecondaryButtonPressedContainerColor else SecondaryButtonContainerColor,
            contentColor = SecondaryButtonContentColor,
            disabledContainerColor = SecondaryButtonContainerDisabledColor,
            disabledContentColor = SecondaryButtonDisabledContentColor,
        ), modifier = modifier, enabled = enabled, interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}