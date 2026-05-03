package com.example.taskspehere.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    // SIGNUP
    fun signup(email: String, password: String, callback: (Boolean, String?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(true, "Signup Successful")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }

    // LOGIN
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(true, "Login Successful")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }

    // FORGOT PASSWORD
    fun forgotPassword(email: String, callback: (Boolean, String?) -> Unit) {

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback(true, "Reset Email Sent")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }
}