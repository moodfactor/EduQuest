package com.mood.eduquest

// User Profiles
data class UserProfile(
    val userId: String,
    val email: String,
    val name: String,
    val userType: UserType, // TEACHER, STUDENT, PARENT
    val profileImage: String?,
    val institutionId: String?,
    val languagePreference: String,
    val registrationDate: Long,
    val lastLoginDate: Long
)

// Enum for User Types
enum class UserType {
    TEACHER, STUDENT, PARENT
}

// Quiz Model
data class Quiz(
    val quizId: String,
    val creatorId: String,
    val title: String,
    val description: String,
    val subject: String,
    val difficultyLevel: DifficultyLevel,
    val questions: List<Question>,
    val timeLimit: Int, // in minutes
    val maxAttempts: Int,
    val scheduledDate: Long?,
    val deadline: Long?
)

// Question Model
data class Question(
    val questionId: String,
    val type: QuestionType,
    val text: String,
    val options: List<String>?,
    val correctAnswer: String,
    val explanation: String?,
    val mediaUrls: List<String>?,
    val points: Int
)

// Enum for Question Types
enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    SHORT_ANSWER,
    MATCHING
}

// Enum for Difficulty Levels
enum class DifficultyLevel {
    EASY, MEDIUM, HARD, ADVANCED
}

// Quiz Attempt Model
data class QuizAttempt(
    val attemptId: String,
    val userId: String,
    val quizId: String,
    val startTime: Long,
    val endTime: Long?,
    val score: Float,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val status: AttemptStatus
)

enum class AttemptStatus {
    IN_PROGRESS, COMPLETED, ABANDONED
}

// Gamification Models
data class UserAchievement(
    val userId: String,
    val achievementId: String,
    val name: String,
    val description: String,
    val xpPoints: Int,
    val unlockedDate: Long
)

data class LeaderboardEntry(
    val userId: String,
    val username: String,
    val totalXp: Int,
    val rank: Int,
    val profileImage: String?
)

// Chat and Communication Models
data class ChatMessage(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val messageType: MessageType
)

enum class MessageType {
    TEXT, IMAGE, SYSTEM_ANNOUNCEMENT
}