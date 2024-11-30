package com.mood.eduquest.core

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mood.eduquest.AuthenticationManager
import com.mood.eduquest.data.repository.QuizRepository
import com.mood.eduquest.data.repository.UserRepository
import com.mood.eduquest.gamification.AchievementManager
import com.mood.eduquest.services.QuizService

class AppDependencies(context: Context) {
    // Firebase Instances
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    // Repositories
    val userRepository = UserRepository(firebaseFirestore)
    val quizRepository = QuizRepository(firebaseFirestore)

    // Managers
    val achievementManager = AchievementManager(userRepository)

    // Services
    val quizService = QuizService(quizRepository, achievementManager)

    // Authentication Manager
    val authenticationManager = AuthenticationManager()

    // Companion object for global access (use sparingly)
    companion object {
        private var instance: AppDependencies? = null

        fun initialize(context: Context): AppDependencies {
            if (instance == null) {
                instance = AppDependencies(context)
            }
            return instance!!
        }

        fun getInstance(): AppDependencies {
            return instance ?: throw IllegalStateException("AppDependencies not initialized")
        }
    }
}