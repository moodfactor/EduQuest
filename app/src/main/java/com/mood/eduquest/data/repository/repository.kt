package com.mood.eduquest.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.mood.eduquest.Quiz
import com.mood.eduquest.QuizAttempt
import kotlinx.coroutines.tasks.await
import com.mood.eduquest.UserProfile
import com.mood.eduquest.UserAchievement
import com.mood.eduquest.LeaderboardEntry
import kotlinx.coroutines.tasks.await


class QuizRepository(private val firestore: FirebaseFirestore) {
    private val quizzesCollection = firestore.collection("quizzes")
    private val attemptsCollection = firestore.collection("quiz_attempts")

    suspend fun createQuiz(quiz: Quiz): String {
        val quizRef = quizzesCollection.add(quiz).await()
        return quizRef.id
    }

    suspend fun getQuizzesByCreator(creatorId: String): List<Quiz> {
        return quizzesCollection
            .whereEqualTo("creatorId", creatorId)
            .get()
            .await()
            .toObjects(Quiz::class.java)
    }

    suspend fun recordQuizAttempt(attempt: QuizAttempt): String {
        val attemptRef = attemptsCollection.add(attempt).await()
        return attemptRef.id
    }

    suspend fun getStudentQuizAttempts(userId: String): List<QuizAttempt> {
        return attemptsCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(QuizAttempt::class.java)
    }
}

class UserRepository(private val firestore: FirebaseFirestore) {
    private val usersCollection = firestore.collection("users")
    private val achievementsCollection = firestore.collection("achievements")

    suspend fun createUserProfile(userProfile: UserProfile) {
        usersCollection.document(userProfile.userId).set(userProfile).await()
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return usersCollection.document(userId)
            .get()
            .await()
            .toObject(UserProfile::class.java)
    }

    suspend fun updateUserXP(userId: String, xpPoints: Int) {
        usersCollection.document(userId)
            .update("xpPoints", xpPoints)
            .await()
    }

    suspend fun unlockAchievement(achievement: UserAchievement) {
        achievementsCollection.add(achievement).await()
    }

    suspend fun getLeaderboard(): List<LeaderboardEntry> {
        return usersCollection
            .orderBy("xpPoints", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .await()
            .toObjects(LeaderboardEntry::class.java)
    }
}