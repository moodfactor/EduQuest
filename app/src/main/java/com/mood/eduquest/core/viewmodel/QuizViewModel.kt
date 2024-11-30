package com.mood.eduquest.viewmodel

import com.mood.eduquest.Question
import com.mood.eduquest.Quiz
import com.mood.eduquest.core.AppDependencies
import com.mood.eduquest.core.viewmodel.BaseViewModel
import com.mood.eduquest.services.QuizService

// Sealed class for comprehensive state management
sealed class QuizState {
    object Idle : QuizState()
    object Loading : QuizState()
    data class QuizCreated(val quizId: String) : QuizState()
    data class QuizList(val quizzes: List<Quiz>) : QuizState()
    data class Error(val message: String) : QuizState()
}

class QuizViewModel(
    private val quizService: QuizService = AppDependencies.getInstance().quizService
) : BaseViewModel<QuizState>(QuizState.Idle) {

    fun createQuiz(
        creatorId: String,
        title: String,
        subject: String,
        questions: List<Question>
    ) {
        launchDataLoad {
            updateState { QuizState.Loading }
            try {
                val quizId = quizService.createQuiz(
                    creatorId = creatorId,
                    title = title,
                    subject = subject,
                    questions = questions
                )
                updateState { QuizState.QuizCreated(quizId) }
            } catch (e: Exception) {
                updateState { QuizState.Error(e.localizedMessage ?: "Quiz creation failed") }
            }
        }
    }

    fun fetchTeacherQuizzes(teacherId: String) {
        launchDataLoad {
            updateState { QuizState.Loading }
            try {
                val quizzes = quizService.getTeacherQuizzes(teacherId)
                updateState { QuizState.QuizList(quizzes) }
            } catch (e: Exception) {
                updateState { QuizState.Error(e.localizedMessage ?: "Failed to fetch quizzes") }
            }
        }
    }
}