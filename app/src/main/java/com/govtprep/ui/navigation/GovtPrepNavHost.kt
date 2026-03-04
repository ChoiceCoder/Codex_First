package com.govtprep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.govtprep.R
import com.govtprep.ui.screens.AdminScreen
import com.govtprep.ui.screens.AnalysisScreen
import com.govtprep.ui.screens.ExamScreen
import com.govtprep.ui.screens.HomeScreen
import com.govtprep.ui.screens.LoginScreen
import com.govtprep.ui.screens.MockTestsScreen
import com.govtprep.ui.screens.ProfileScreen
import com.govtprep.ui.screens.SignupScreen
import com.govtprep.ui.screens.SplashScreen
import com.govtprep.ui.screens.TestScreen
import com.govtprep.viewmodel.AppViewModel
import com.govtprep.viewmodel.AuthUiState
import com.govtprep.viewmodel.AuthViewModel
import com.govtprep.viewmodel.ExamViewModel
import com.govtprep.viewmodel.HomeUiState
import com.govtprep.viewmodel.HomeViewModel

@Composable
fun GovtPrepNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val examViewModel: ExamViewModel = hiltViewModel()
    val appViewModel: AppViewModel = hiltViewModel()

    val authState = authViewModel.state.collectAsState().value
    val homeState = homeViewModel.state.collectAsState().value
    val examState = examViewModel.state.collectAsState().value
    val selectedTest = appViewModel.selectedTest.collectAsState().value

    NavHost(navController = navController, startDestination = Route.Splash.value) {
        composable(Route.Splash.value) {
            SplashScreen {
                if (authViewModel.hasSession()) {
                    authViewModel.checkSession()
                    navController.navigate(Route.Home.value) { popUpTo(Route.Splash.value) { inclusive = true } }
                } else {
                    navController.navigate(Route.Login.value) { popUpTo(Route.Splash.value) { inclusive = true } }
                }
            }
        }
        composable(Route.Login.value) {
            LoginScreen(
                onLogin = { email, password -> authViewModel.login(email, password) },
                onSignup = { navController.navigate(Route.Signup.value) },
                error = (authState as? AuthUiState.Error)?.message
            )
            if (authState is AuthUiState.Success) {
                LaunchedEffect(authState) { navController.navigate(Route.Home.value) { popUpTo(Route.Login.value) { inclusive = true } } }
            }
        }
        composable(Route.Signup.value) {
            SignupScreen(
                onSignup = { name, email, password -> authViewModel.signup(name, email, password) },
                onBack = { navController.popBackStack() },
                error = (authState as? AuthUiState.Error)?.message
            )
            if (authState is AuthUiState.Success) {
                LaunchedEffect(authState) { navController.navigate(Route.Home.value) { popUpTo(Route.Signup.value) { inclusive = true } } }
            }
        }
        composable(Route.Home.value) {
            LaunchedEffect(Unit) { homeViewModel.load() }
            HomeScreen(
                state = homeState,
                onMockTests = { navController.navigate(Route.MockTests.value) },
                onAnalysis = { navController.navigate(Route.Analysis.value) },
                onProfile = { navController.navigate(Route.Profile.value) },
                onAdmin = { navController.navigate(Route.Admin.value) }
            )
        }
        composable(Route.MockTests.value) {
            val tests = (homeState as? HomeUiState.Ready)?.tests.orEmpty()
            MockTestsScreen(tests = tests, onSelect = {
                appViewModel.selectTest(it)
                navController.navigate("test/${it.id}")
            })
        }
        composable(Route.Test.value, arguments = listOf(navArgument("testId") { type = NavType.StringType })) {
            val test = selectedTest
            if (test != null) {
                TestScreen(test = test, onStart = {
                    examViewModel.startExam(test)
                    navController.navigate(Route.Exam.value)
                })
            }
        }
        composable(Route.Exam.value) {
            ExamScreen(
                state = examState,
                onSelect = examViewModel::selectAnswer,
                onNext = examViewModel::next,
                onPrev = examViewModel::previous,
                onSubmit = examViewModel::submit
            )
        }
        composable(Route.Analysis.value) { AnalysisScreen(message = androidx.compose.ui.res.stringResource(R.string.analysis_message)) }
        composable(Route.Profile.value) {
            val profileName = (homeState as? HomeUiState.Ready)?.profile?.name ?: ""
            ProfileScreen(
                message = androidx.compose.ui.res.stringResource(R.string.profile_message, profileName),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Route.Login.value) { popUpTo(Route.Home.value) { inclusive = true } }
                }
            )
        }
        composable(Route.Admin.value) { AdminScreen(message = androidx.compose.ui.res.stringResource(R.string.admin_message)) }
    }
}
