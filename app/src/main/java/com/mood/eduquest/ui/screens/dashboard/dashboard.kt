package com.mood.eduquest.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mood.eduquest.Quiz
import com.mood.eduquest.UserProfile
import com.mood.eduquest.core.AppDependencies
import com.mood.eduquest.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onQuizSelect: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val quizViewModel = remember { QuizViewModel() }
    val userRepository = AppDependencies.getInstance().userRepository

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var availableQuizzes by remember { mutableStateOf<List<Quiz>>(emptyList()) }

    // Fetch user profile and quizzes
    LaunchedEffect(Unit) {
        scope.launch {
            val currentUser = AppDependencies.getInstance().authenticationManager.getCurrentUser()
            currentUser?.let { user ->
                userProfile = userRepository.getUserProfile(user.uid)
                // TODO: Implement method to fetch available quizzes for student
                // availableQuizzes = quizViewModel.fetchAvailableQuizzes()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Dashboard") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.PersonOutline, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // User Greeting
            userProfile?.let { profile ->
                Text(
                    text = "Welcome, ${profile.name}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Achievements Overview
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Achievements",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // TODO: Implement achievement display
                    Text("Achievements will be shown here")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Available Quizzes
            Text(
                text = "Available Quizzes",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn {
                items(availableQuizzes.size) { index ->
                    val quiz = availableQuizzes[index]
                    QuizListItem(
                        quiz = quiz,
                        onQuizSelect = onQuizSelect
                    )
                }
            }
        }
    }
}

@Composable
fun QuizListItem(
    quiz: Quiz,
    onQuizSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { onQuizSelect(quiz.quizId) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = quiz.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Subject: ${quiz.subject}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Difficulty: ${quiz.difficultyLevel}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}