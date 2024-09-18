package com.abz.agency.testtask.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.abz.agency.testtask.R
import com.abz.agency.testtask.presentation.navigation.AppScreens
import com.abz.agency.testtask.presentation.navigation.BottomNavItem
import com.abz.agency.testtask.presentation.screens.get_users.UsersScreen
import com.abz.agency.testtask.presentation.screens.get_users.vm.UsersScreenViewModel
import com.abz.agency.testtask.presentation.screens.sign_up.SignUpScreen
import com.abz.agency.testtask.presentation.screens.sign_up.vm.SignUpViewModel
import com.abz.agency.testtask.presentation.ui.theme.NavBarColor

private val listOfScreens = listOf(
    BottomNavItem(
        screenNameId = R.string.users,
        selectedIconId = R.drawable.ic_users,
        route = AppScreens.UsersScreen.route
    ),
    BottomNavItem(
        screenNameId = R.string.sign_up,
        selectedIconId = R.drawable.ic_sign_up,
        route = AppScreens.SignUpScreen.route
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    // Obtain the current back stack entry from the navigation controller
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Get the current destination from the back stack entry
    val currentDestination = navBackStackEntry?.destination
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = NavBarColor) {
                // Iterate over a list of screens to create navigation bar items
                listOfScreens.forEach { screen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = Color.Transparent,
                        ),
                        // Determine if the current screen is selected by checking if its route matches the current destination
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        // Define the action to perform when this item is clicked
                        onClick = {
                            // Navigate to the selected screen route
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the navigation graph
                                popUpTo(navController.graph.findStartDestination().id) {
                                    // Save the state of the navigation graph
                                    saveState = true
                                }
                                // Use single top to avoid multiple instances of the same screen
                                launchSingleTop = true
                                // Restore the state of the destination screen if it exists
                                restoreState = true
                            }
                        },
                        icon = {
                            // Use a Row to align the text to the right of the icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing between icon and text
                            ) {
                                Icon(
                                    painter = painterResource(id = screen.selectedIconId),
                                    contentDescription = stringResource(id = screen.screenNameId)
                                )
                                Text(
                                    text = stringResource(id = screen.screenNameId),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        label = null
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    val title = when (currentDestination?.route) {
                        AppScreens.UsersScreen.route -> stringResource(R.string.working_with_get_request)
                        AppScreens.SignUpScreen.route -> stringResource(R.string.working_with_post_request)
                        else -> ""
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center
                    )
                })
        }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.UsersScreen.route
        ) {
            composable(AppScreens.UsersScreen.route) {
                val vm = hiltViewModel<UsersScreenViewModel>()
                UsersScreen(vm.users.collectAsLazyPagingItems())
            }
            composable(AppScreens.SignUpScreen.route) {
                val vm = hiltViewModel<SignUpViewModel>()
                SignUpScreen(vm.state, onEvent = vm::onEvent) {
                    navController.navigate(AppScreens.UsersScreen.route)
                }
            }
        }
    }
}