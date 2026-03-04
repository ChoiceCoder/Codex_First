package com.govtprep.ui.navigation

sealed class Route(val value: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Signup : Route("signup")
    data object Home : Route("home")
    data object MockTests : Route("mock_tests")
    data object Test : Route("test/{testId}")
    data object Exam : Route("exam")
    data object Analysis : Route("analysis")
    data object Profile : Route("profile")
    data object Admin : Route("admin")
}
