package com.abz.agency.testtask.presentation.screens.no_internet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.abz.agency.testtask.R
import com.abz.agency.testtask.presentation.ui.elements.PrimaryButton

@Composable
fun NoConnectionScreen(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_internet_connection),
            modifier = Modifier.size(200.dp),
            contentDescription = stringResource(
                R.string.no_internet_connection
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.no_internet_connection),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(text = stringResource(R.string.try_again), enabled = true) {
            onRetry()
        }
    }
}