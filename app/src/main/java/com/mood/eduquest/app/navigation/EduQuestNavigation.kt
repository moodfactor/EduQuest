package com.mood.eduquest.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mood.eduquest.ui.screens.auth.LoginScreen
import com.mood.eduquest.ui.screens.auth.RegisterScreen
import com.mood.eduquest.ui.screens.dashboard.StudentDashboardScreen
import com.mood.eduquest.ui.screens.dashboard.TeacherDashboardScreen
import com.mood.eduquest.ui.screens.quiz.QuizCreationScreen
import com.mood.eduquest.ui.screens.quiz.QuizListScreen
import com.mood.eduquest.ui.screens.profile.UserProfileScreen

// Sealed class for route management
sealed class AppRoute(val route: String) {
    object Login : AppRoute("login")
    object Register : AppRoute("register")
    object StudentDashboard : AppRoute("student_dashboard")
    object TeacherDashboard : AppRoute("teacher_dashboard")
    object QuizCreation : AppRoute("quiz_creation")
    object QuizList : AppRoute("quiz_list")
    object UserProfile : AppRoute("user_profile")
}

@Composable
fun EduQuestNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoute.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                onLoginSuccess = { userType ->
                    val destination = when (userType) {
                        UserType.STUDENT -> AppRoute.StudentDashboard.route
                        UserType.TEACHER -> AppRoute.TeacherDashboard.route
                        UserType.PARENT -> AppRoute.StudentDashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppRoute.Register.route)
                }
            )
        }

        composable(AppRoute.Register.route) {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate(AppRoute.Login.route)
                }
            )
        }

        composable(AppRoute.StudentDashboard.route) {
            StudentDashboardScreen(
                onQuizSelect = { quizId ->
                    // Navigate to quiz attempt screen
                },
                onProfileClick = {
                    navController.navigate(AppRoute.UserProfile.route)
                }
            )
        }

        composable(AppRoute.TeacherDashboard.route) {
            TeacherDashboardScreen(
                onCreateQuizClick = {
                    navController.navigate(AppRoute.QuizCreation.route)
                },
                onQuizListClick = {
                    navController.navigate(AppRoute.QuizList.route)
                }
            )
        }

        composable(AppRoute.QuizCreation.route) {
            QuizCreationScreen(
                onQuizCreated = {
                    navController.navigateUp()
                }
            )
        }

        composable(AppRoute.QuizList.route) {
            QuizListScreen(
                onQuizSelect = { quizId ->
                    // Navigate to quiz details
                }
            )
        }

        composable(AppRoute.UserProfile.route) {
            UserProfileScreen(
                onLogout = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}