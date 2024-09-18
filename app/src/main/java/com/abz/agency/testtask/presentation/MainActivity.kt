package com.abz.agency.testtask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abz.agency.testtask.core.ConnectionState
import com.abz.agency.testtask.presentation.screens.MainAppScreen
import com.abz.agency.testtask.presentation.screens.no_internet.NoConnectionScreen
import com.abz.agency.testtask.presentation.ui.theme.AbzTestTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { vm.isLoading.value }
        setContent {
            AbzTestTaskTheme {
                val networkStatus = vm.networkStatus.collectAsStateWithLifecycle()
                if (networkStatus.value == ConnectionState.Disconnected) {
                    NoConnectionScreen(Modifier.fillMaxSize()) {
                        recreate()
                    }
                } else {
                    MainAppScreen()
                }
            }
        }
    }
}