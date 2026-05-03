package com.example.taskspehere.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskspehere.R
import com.example.taskspehere.viewmodel.AuthResult
import com.example.taskspehere.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var emailLayout: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var resetBtn: MaterialButton
    private lateinit var backLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        // Initialize views
        emailLayout = findViewById(R.id.email_layout)
        email = findViewById(R.id.et_email)
        resetBtn = findViewById(R.id.btn_reset)
        backLogin = findViewById(R.id.tv_back_login)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Observe reset result
        viewModel.forgotResult.observe(this) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    resetBtn.isEnabled = false
                    resetBtn.text = "Sending..."
                }
                is AuthResult.Success -> {
                    resetBtn.isEnabled = true
                    resetBtn.text = "Reset Password"
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                    // Navigate back to login screen
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is AuthResult.Error -> {
                    resetBtn.isEnabled = true
                    resetBtn.text = "Reset Password"
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
                AuthResult.Idle -> { /* do nothing */ }
            }
        }

        // Button click
        resetBtn.setOnClickListener {
            performReset()
        }

        // Back to login click
        backLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performReset() {
        val emailText = email.text?.toString()?.trim() ?: ""

        emailLayout.error = null

        if (emailText.isEmpty()) {
            emailLayout.error = "Email is required"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailLayout.error = "Enter a valid email address"
            email.requestFocus()
            return
        }

        // Call ViewModel to send reset email
        viewModel.forgotPassword(emailText)
    }
}