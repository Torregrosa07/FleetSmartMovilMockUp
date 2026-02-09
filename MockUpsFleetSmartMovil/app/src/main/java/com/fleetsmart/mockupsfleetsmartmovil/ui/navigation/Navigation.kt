package com.fleetsmart.mockupsfleetsmartmovil.ui.navigation


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fleetsmart.mockupsfleetsmartmovil.ui.screens.*
import com.fleetsmart.mockupsfleetsmartmovil.ui.theme.AppColors
import androidx.compose.ui.unit.dp

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Routes : Screen("routes", "Rutas", Icons.Default.List)
    object ActiveRoute : Screen("active_route", "Ruta Activa", Icons.Default.Map)
    object Incidents : Screen("incidents", "Incidencias", Icons.Default.Warning)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetDriverApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route != Screen.ActiveRoute.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FleetDriver") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Card,
                    titleContentColor = AppColors.Foreground
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = AppColors.Primary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Text(
                                    text = "CM",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AppColors.PrimaryForeground
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = AppColors.Card,
                    contentColor = AppColors.Foreground
                ) {
                    val items = listOf(
                        Screen.Routes,
                        Screen.Incidents,
                        Screen.Profile
                    )

                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AppColors.Primary,
                                selectedTextColor = AppColors.Primary,
                                indicatorColor = AppColors.Primary.copy(alpha = 0.1f),
                                unselectedIconColor = AppColors.MutedForeground,
                                unselectedTextColor = AppColors.MutedForeground
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Routes.route,
        modifier = modifier
    ) {
        composable(Screen.Routes.route) {
            MyRoutesScreen(
                onStartRoute = { routeId ->
                    navController.navigate(Screen.ActiveRoute.route)
                }
            )
        }

        composable(Screen.ActiveRoute.route) {
            ActiveRouteScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Incidents.route) {
            ReportIncidentScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}