package com.mood.eduquest

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

// Authentication Manager
class AuthenticationManager {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun getCurrentUserType(callback: (UserType?) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    val userType = document.getString("userType")?.let {
                        UserType.valueOf(it.uppercase())
                    }
                    callback(userType)
                }
                .addOnFailureListener {
                    callback(null)
                }
        } ?: callback(null)
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signIn(email: String, password: String, callback: (UserType?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                getCurrentUserType(callback)
            }
            .addOnFailureListener {
                callback(null)
            }
            }

    fun signOut() {
        auth.signOut()
    }
}