package com.conamobile.pdpfirebase1dars.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.conamobile.pdpfirebase1dars.R
import com.conamobile.pdpfirebase1dars.managers.AuthHandler
import com.conamobile.pdpfirebase1dars.managers.AuthManager
import com.conamobile.pdpfirebase1dars.utils.Extensions.toast

class SignInActivity : BaseActivity() {
    val TAG = SignInActivity::class.java.toString()
    lateinit var et_email: EditText
    lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener {
            firebaseSignIn(
                et_email.text.toString(),
                et_password.text.toString()
            )
        }
        val tv_signup = findViewById<TextView>(R.id.tv_signup)
        tv_signup.setOnClickListener { callSignUpActivity() }
    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun firebaseSignIn(email: String, password: String) {

        showLoading(this)

        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess() {
                dismissLoading()
                toast("Signed in successfully!")
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast("Signed in failed! ${exception.toString()}")
            }
        })
    }
}
