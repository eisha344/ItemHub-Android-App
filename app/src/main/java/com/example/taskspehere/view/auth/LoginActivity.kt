package com.example.taskspehere.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskspehere.R
import com.example.taskspehere.view.dashboard.DashboardActivity
import com.example.taskspehere.viewmodel.AuthResult
import com.example.taskspehere.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var loginBtn: MaterialButton
    private lateinit var tvSignup: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        loginBtn = findViewById(R.id.btn_login)
        tvSignup = findViewById(R.id.tv_signup)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    loginBtn.isEnabled = false
                    loginBtn.text = "Logging in..."
                }
                is AuthResult.Success -> {
                    loginBtn.isEnabled = true
                    loginBtn.text = "Login"
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is AuthResult.Error -> {
                    loginBtn.isEnabled = true
                    loginBtn.text = "Login"
                    val errorMsg = if (result.message.contains("password") ||
                        result.message.contains("email") ||
                        result.message.contains("credential")
                    ) "Invalid email or password" else result.message
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                }
                AuthResult.Idle -> {}
            }
        }

        loginBtn.setOnClickListener { performLogin() }
        tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun performLogin() {
        val emailText = email.text?.toString()?.trim() ?: ""
        val passText = password.text?.toString()?.trim() ?: ""

        emailLayout.error = null
        passwordLayout.error = null

        if (emailText.isEmpty()) {
            emailLayout.error = "Email required"
            return
        }
        if (passText.isEmpty()) {
            passwordLayout.error = "Password required"
            return
        }

        viewModel.login(emailText, passText)
    }
}