package com.example.food_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food_app.data.Role
import com.example.food_app.ui.auth.AuthViewModel
import com.example.food_app.ui.auth.ForgotPasswordScreen
import com.example.food_app.ui.auth.LoginScreen
import com.example.food_app.ui.auth.RegisterScreen
import com.example.food_app.ui.cart.CartScreen
import com.example.food_app.ui.cart.CheckoutScreen
import com.example.food_app.ui.cart.OrderHistoryScreen
import com.example.food_app.ui.cart.ShippingInfoScreen
import com.example.food_app.ui.customer.CustomerMenuScreen
import com.example.food_app.ui.detail.FoodDetailScreen
import com.example.food_app.ui.menu.AddFoodItemScreen
import com.example.food_app.ui.menu.AdminOrderHistoryScreen
import com.example.food_app.ui.menu.CategoryManagementScreen
import com.example.food_app.ui.menu.EditFoodItemScreen
import com.example.food_app.ui.menu.FoodMenuScreen
import com.example.food_app.ui.statistics.StatisticsScreen

object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val FORGOT_PASSWORD_ROUTE = "forgot_password"
    const val ADMIN_HOME_ROUTE = "admin_home"
    const val CUSTOMER_HOME_ROUTE = "customer_home"
    const val ADD_ITEM_ROUTE = "add_item"
    const val EDIT_ITEM_ROUTE = "edit_item"
    const val CATEGORY_MANAGEMENT_ROUTE = "category_management"
    const val FOOD_DETAIL_ROUTE = "food_detail"
    const val CART_ROUTE = "cart"
    const val SHIPPING_INFO_ROUTE = "shipping_info" // Tuyến đường mới
    const val CHECKOUT_ROUTE = "checkout"
    const val ORDER_HISTORY_ROUTE = "order_history"
    const val ADMIN_ORDER_HISTORY_ROUTE = "admin_order_history"
    const val STATISTICS_ROUTE = "statistics"
    const val ITEM_ID_ARG = "itemId"
    // Các key cho argument
    const val NAME_ARG = "name"
    const val PHONE_ARG = "phone"
    const val ADDRESS_ARG = "address"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AppDestinations.LOGIN_ROUTE) {

        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { role ->
                    val route = if (role == Role.ADMIN) AppDestinations.ADMIN_HOME_ROUTE else AppDestinations.CUSTOMER_HOME_ROUTE
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                onNavigateToForgotPassword = { navController.navigate(AppDestinations.FORGOT_PASSWORD_ROUTE) }
            )
        }

        composable(AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { role ->
                    val route = if (role == Role.ADMIN) AppDestinations.ADMIN_HOME_ROUTE else AppDestinations.CUSTOMER_HOME_ROUTE
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateUp = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.ADMIN_HOME_ROUTE) {
            FoodMenuScreen(
                onAddItemClick = { navController.navigate(AppDestinations.ADD_ITEM_ROUTE) },
                onItemClick = { itemId ->
                    navController.navigate("${AppDestinations.EDIT_ITEM_ROUTE}/$itemId")
                },
                onManageCategoriesClick = { navController.navigate(AppDestinations.CATEGORY_MANAGEMENT_ROUTE) },
                onHistoryClick = { navController.navigate(AppDestinations.ADMIN_ORDER_HISTORY_ROUTE) },
                onStatisticsClick = { navController.navigate(AppDestinations.STATISTICS_ROUTE) },
                onLogoutClick = { 
                    authViewModel.logout()
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                 }
            )
        }

        composable(AppDestinations.CATEGORY_MANAGEMENT_ROUTE) {
            CategoryManagementScreen(onNavigateUp = { navController.popBackStack() })
        }
        
        composable(AppDestinations.ADMIN_ORDER_HISTORY_ROUTE) {
            AdminOrderHistoryScreen(onNavigateUp = { navController.popBackStack() })
        }

        composable(AppDestinations.STATISTICS_ROUTE) {
            StatisticsScreen(onNavigateUp = { navController.popBackStack() })
        }

        composable(AppDestinations.CUSTOMER_HOME_ROUTE) {
            CustomerMenuScreen(
                onItemClick = { itemId ->
                    navController.navigate("${AppDestinations.FOOD_DETAIL_ROUTE}/$itemId")
                },
                onCartClick = { navController.navigate(AppDestinations.CART_ROUTE) },
                onHistoryClick = { navController.navigate(AppDestinations.ORDER_HISTORY_ROUTE) },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.CART_ROUTE) {
            CartScreen(
                onNavigateUp = { navController.popBackStack() },
                onCheckoutClick = { navController.navigate(AppDestinations.SHIPPING_INFO_ROUTE) } // -> Đi đến trang nhập thông tin
            )
        }

        composable(AppDestinations.SHIPPING_INFO_ROUTE) {
            ShippingInfoScreen(
                onNavigateUp = { navController.popBackStack() },
                onContinueClick = { name, phone, address ->
                    // Truyền dữ liệu qua trang thanh toán
                    navController.navigate("${AppDestinations.CHECKOUT_ROUTE}/$name/$phone/$address")
                }
            )
        }

        composable(
            route = "${AppDestinations.CHECKOUT_ROUTE}/{${AppDestinations.NAME_ARG}}/{${AppDestinations.PHONE_ARG}}/{${AppDestinations.ADDRESS_ARG}}",
            arguments = listOf(
                navArgument(AppDestinations.NAME_ARG) { type = NavType.StringType },
                navArgument(AppDestinations.PHONE_ARG) { type = NavType.StringType },
                navArgument(AppDestinations.ADDRESS_ARG) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(AppDestinations.NAME_ARG) ?: ""
            val phone = backStackEntry.arguments?.getString(AppDestinations.PHONE_ARG) ?: ""
            val address = backStackEntry.arguments?.getString(AppDestinations.ADDRESS_ARG) ?: ""
            CheckoutScreen(
                name = name,
                phone = phone,
                address = address,
                onNavigateUp = { navController.popBackStack() },
                onOrderSuccess = {
                    navController.navigate(AppDestinations.CUSTOMER_HOME_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.ORDER_HISTORY_ROUTE) {
            OrderHistoryScreen(onNavigateUp = { navController.popBackStack() })
        }

        composable(AppDestinations.ADD_ITEM_ROUTE) {
            AddFoodItemScreen(onNavigateUp = { navController.popBackStack() })
        }

        composable(
            route = "${AppDestinations.EDIT_ITEM_ROUTE}/{${AppDestinations.ITEM_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.ITEM_ID_ARG) { type = NavType.IntType })
        ) {
            EditFoodItemScreen(onNavigateUp = { navController.popBackStack() })
        }
        
        composable(
            route = "${AppDestinations.FOOD_DETAIL_ROUTE}/{${AppDestinations.ITEM_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.ITEM_ID_ARG) { type = NavType.IntType })
        ) {
            FoodDetailScreen(onNavigateUp = { navController.popBackStack() })
        }
    }
}
