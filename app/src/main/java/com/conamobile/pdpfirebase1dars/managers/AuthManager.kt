package com.conamobile.pdpfirebase1dars.managers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager {
    companion object {
        private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        fun isSignedIn(): Boolean {
            return firebaseUser != null
        }

        fun signIn(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handler.onSuccess()
                } else {
                    handler.onError(task.exception)
                }
            }
        }

        fun signUp(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handler.onSuccess()
                    } else {
                        handler.onError(task.exception)
                    }
                }
        }

        fun signOut() {
            firebaseAuth.signOut()
        }
    }
}
