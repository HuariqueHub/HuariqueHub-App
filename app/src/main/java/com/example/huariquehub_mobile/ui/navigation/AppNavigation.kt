package com.example.huariquehub_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.huariquehub_mobile.ui.screens.auth.LoginScreen
import com.example.huariquehub_mobile.ui.screens.auth.RegisterScreen
import com.example.huariquehub_mobile.ui.screens.home.HomeScreen
import com.example.huariquehub_mobile.ui.screens.home.HuariqueDetailScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val HUARIQUE_DETAIL = "huarique_detail/{huariqueId}"
    fun huariqueDetail(id: Int) = "huarique_detail/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onHuariqueClick = { huariqueId ->
                    navController.navigate(Routes.huariqueDetail(huariqueId))
                },
                onProfileClick = {}
            )
        }

        composable(
            route = Routes.HUARIQUE_DETAIL,
            arguments = listOf(navArgument("huariqueId") { type = NavType.IntType })
        ) { backStackEntry ->
            val huariqueId = backStackEntry.arguments?.getInt("huariqueId") ?: 1
            HuariqueDetailScreen(
                huariqueId = huariqueId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
