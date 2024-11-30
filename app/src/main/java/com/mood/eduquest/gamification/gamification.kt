package com.mood.eduquest.gamification

import com.mood.eduquest.UserAchievement
import com.mood.eduquest.data.repository.UserRepository

class AchievementManager(private val userRepository: UserRepository) {

    // Predefined achievements
    object Achievements {
        const val FIRST_QUIZ = "FIRST_QUIZ"
        const val STREAK_7_DAYS = "STREAK_7_DAYS"
        const val TOP_PERFORMER = "TOP_PERFORMER"
    }

    suspend fun checkAndUnlockAchievements(userId: String, context: AchievementContext) {
        when (context.type) {
            AchievementType.QUIZ_COMPLETION -> checkQuizCompletionAchievements(userId, context)
            AchievementType.DAILY_STREAK -> checkDailyStreakAchievements(userId, context)
            AchievementType.PERFORMANCE -> checkPerformanceAchievements(userId, context)
        }
    }

    private suspend fun checkQuizCompletionAchievements(userId: String, context: AchievementContext) {
        // Example achievement logic
        if (context.quizCount == 1) {
            val achievement = UserAchievement(
                userId = userId,
                achievementId = Achievements.FIRST_QUIZ,
                name = "First Quiz Completed",
                description = "Congratulations on completing your first quiz!",
                xpPoints = 50,
                unlockedDate = System.currentTimeMillis()
            )
            userRepository.unlockAchievement(achievement)
        }
    }

    private suspend fun checkDailyStreakAchievements(userId: String, context: AchievementContext) {
        if (context.streakDays >= 7) {
            val achievement = UserAchievement(
                userId = userId,
                achievementId = Achievements.STREAK_7_DAYS,
                name = "7-Day Learning Streak",
                description = "Keep up the great learning momentum!",
                xpPoints = 100,
                unlockedDate = System.currentTimeMillis()
            )
            userRepository.unlockAchievement(achievement)
        }
    }

    private suspend fun checkPerformanceAchievements(userId: String, context: AchievementContext) {
        if (context.averageScore >= 90.0) {
            val achievement = UserAchievement(
                userId = userId,
                achievementId = Achievements.TOP_PERFORMER,
                name = "Top Performer",
                description = "Exceptional performance across quizzes!",
                xpPoints = 200,
                unlockedDate = System.currentTimeMillis()
            )
            userRepository.unlockAchievement(achievement)
        }
    }
}

// Supporting classes for achievement context
enum class AchievementType {
    QUIZ_COMPLETION,
    DAILY_STREAK,
    PERFORMANCE
}

data class AchievementContext(
    val type: AchievementType,
    val quizCount: Int = 0,
    val streakDays: Int = 0,
    val averageScore: Double = 0.0
)