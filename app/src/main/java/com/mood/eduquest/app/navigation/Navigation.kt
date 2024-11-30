package com.mood.eduquest.app.navigation


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Sealed class for navigation routes
sealed class NavigationRoute(val route: String) {
    object Dashboard : NavigationRoute("dashboard")
    object Learning : NavigationRoute("learning")
    object Achievements : NavigationRoute("achievements")
    object Messages : NavigationRoute("messages")
    object Profile : NavigationRoute("profile")

    // Teacher-specific routes
    object QuizCreation : NavigationRoute("quiz_creation")
    object Analytics : NavigationRoute("analytics")
    object Classroom : NavigationRoute("classroom")
}

// User Type Enum
enum class UserType {
    STUDENT,
    TEACHER,
    PARENT
}

// Navigation Item Data Class
data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val userType: UserType
)

// Navigation Configuration Object
object NavigationConfig {
    val navigationItems = listOf(
        // Student Navigation Items
        NavigationItem(
            route = NavigationRoute.Dashboard.route,
            label = "Home",
            icon = Icons.Default.Home,
            userType = UserType.STUDENT
        ),
        NavigationItem(
            route = NavigationRoute.Learning.route,
            label = "Learning",
            icon = Icons.Default.Book,
            userType = UserType.STUDENT
        ),
        NavigationItem(
            route = NavigationRoute.Achievements.route,
            label = "Achievements",
            icon = Icons.Default.Star,
            userType = UserType.STUDENT
        ),
        NavigationItem(
            route = NavigationRoute.Messages.route,
            label = "Messages",
            icon = Icons.Default.Message,
            userType = UserType.STUDENT
        ),
        NavigationItem(
            route = NavigationRoute.Profile.route,
            label = "Profile",
            icon = Icons.Default.Person,
            userType = UserType.STUDENT
        ),

        // Teacher Navigation Items
        NavigationItem(
            route = NavigationRoute.Dashboard.route,
            label = "Dashboard",
            icon = Icons.Default.Home,
            userType = UserType.TEACHER
        ),
        NavigationItem(
            route = NavigationRoute.QuizCreation.route,
            label = "Create Quiz",
            icon = Icons.Default.Add,
            userType = UserType.TEACHER
        ),
        NavigationItem(
            route = NavigationRoute.Analytics.route,
            label = "Analytics",
            icon = Icons.Default.AddCircle,
            userType = UserType.TEACHER
        ),
        NavigationItem(
            route = NavigationRoute.Classroom.route,
            label = "Classroom",
            icon = Icons.Default.Group,
            userType = UserType.TEACHER
        ),
        NavigationItem(
            route = NavigationRoute.Profile.route,
            label = "Profile",
            icon = Icons.Default.Person,
            userType = UserType.TEACHER
        )
    )

    // Get navigation items based on user type
    fun getNavigationItemsForUser(userType: UserType): List<NavigationItem> {
        return navigationItems.filter { it.userType == userType }
    }
}

// Bottom Navigation Composable
@Composable
fun EduQuestBottomNavigation(
    navController: NavController,
    userType: UserType
) {
    val navItems = NavigationConfig.getNavigationItemsForUser(userType)

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = false, // Implement active route detection logic
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId)
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// Main Navigation Host
@Composable
fun EduQuestNavHost(
    navController: NavHostController,
    startDestination: String,
    userType: UserType
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Define composable destinations for different routes
        composable(NavigationRoute.Dashboard.route) {
            // Dashboard screen content
        }
        composable(NavigationRoute.Learning.route) {
            // Learning screen content
        }
        composable(NavigationRoute.Achievements.route) {
            // Achievements screen content
        }
        composable(NavigationRoute.Messages.route) {
            // Messages screen content
        }
        composable(NavigationRoute.Profile.route) {
            // Profile screen content
        }

        // Teacher-specific routes
        composable(NavigationRoute.QuizCreation.route) {
            // Quiz Creation screen content
        }
        composable(NavigationRoute.Analytics.route) {
            // Analytics screen content
        }
        composable(NavigationRoute.Classroom.route) {
            // Classroom screen content
        }
    }
}

// Sample Main Activity Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EduQuestApp() {
    // Remember navigation controller
    val navController = rememberNavController()

    // Remember user type (would typically come from authentication)
    var userType by remember { mutableStateOf(UserType.STUDENT) }

    Scaffold(
        bottomBar = {
            EduQuestBottomNavigation(
                navController = navController,
                userType = userType
            )
        }
    ) {  innerPadding ->
        EduQuestNavHost(
            navController = navController,
            startDestination = NavigationRoute.Dashboard.route,
            userType = userType
        )
    }
}