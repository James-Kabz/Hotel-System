package com.example.hotelloginsystem.nav


sealed class Screens(val route: String) {
    data object ForgotPasswordScreen:Screens(route = "reset_password")
    data object HotelSignUpScreen:Screens(route = "sign_up")
    data object LoginScreen:Screens(route = "login_screen")
    data object ProfileScreen:Screens(route = "profile_screen")
    data object DashboardScreen:Screens(route = "dashboard_screen")
    data object MainScreen:Screens(route = "main_screen")
    data object AddDataScreen:Screens(route = "add_data_screen")
    data object GetDataScreen:Screens(route = "get_data_screen")
}
