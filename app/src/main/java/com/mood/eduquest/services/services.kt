package com.mood.eduquest.services

import com.mood.eduquest.Quiz
import com.mood.eduquest.Question
import com.mood.eduquest.DifficultyLevel
import com.mood.eduquest.QuestionType
import com.mood.eduquest.data.repository.QuizRepository
import com.mood.eduquest.gamification.AchievementManager
import com.mood.eduquest.gamification.AchievementContext
import com.mood.eduquest.gamification.AchievementType

class QuizService(
    private val quizRepository: QuizRepository,
    private val achievementManager: AchievementManager
) {
    suspend fun createQuiz(
        creatorId: String,
        title: String,
        subject: String,
        questions: List<Question>,
        difficultyLevel: DifficultyLevel = DifficultyLevel.MEDIUM,
        timeLimit: Int = 30,
        maxAttempts: Int = 3
    ): String {
        val quiz = Quiz(
            quizId = "", // Firebase will generate
            creatorId = creatorId,
            title = title,
            description = "Quiz on $subject",
            subject = subject,
            difficultyLevel = difficultyLevel,
            questions = questions,
            timeLimit = timeLimit,
            maxAttempts = maxAttempts,
            scheduledDate = null,
            deadline = null
        )

        val quizId = quizRepository.createQuiz(quiz)

        // Check for quiz creation achievements
        achievementManager.checkAndUnlockAchievements(
            creatorId,
            AchievementContext(
                type = AchievementType.QUIZ_COMPLETION,
                quizCount = 1
            )
        )

        return quizId
    }

    suspend fun getTeacherQuizzes(teacherId: String): List<Quiz> {
        return quizRepository.getQuizzesByCreator(teacherId)
    }

    suspend fun generateAdaptiveQuiz(
        studentId: String,
        subject: String,
        previousPerformance: Double
    ): Quiz {
        // Logic to generate a quiz based on student's previous performance
        val difficulty = when {
            previousPerformance < 50.0 -> DifficultyLevel.EASY
            previousPerformance < 75.0 -> DifficultyLevel.MEDIUM
            else -> DifficultyLevel.HARD
        }

        // Sample adaptive quiz generation (you'd want more sophisticated logic)
        val questions = listOf(
            Question(
                questionId = "",
                type = QuestionType.MULTIPLE_CHOICE,
                text = "Adaptive question based on performance",
                options = listOf("Option A", "Option B", "Option C"),
                correctAnswer = "Option B",
                points = 10,
                explanation = "" ,
                mediaUrls = emptyList()
            )
        )

        return Quiz(
            quizId = "",
            creatorId = "system",
            title = "Adaptive $subject Quiz",
            description = "Personalized quiz based on your performance",
            subject = subject,
            difficultyLevel = difficulty,
            questions = questions,
            timeLimit = 20,
            maxAttempts = 1,
            scheduledDate = null,
            deadline = null
        )
    }
}