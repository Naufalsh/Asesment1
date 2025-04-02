package com.naufalmaulanaartocarpussavero607062300078.asesment1.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object History: Screen("historyScreen")
}