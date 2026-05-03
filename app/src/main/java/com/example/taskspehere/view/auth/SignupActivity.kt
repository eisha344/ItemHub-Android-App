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

class SignupActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmLayout: TextInputLayout
    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirm: TextInputEditText
    private lateinit var signupBtn: MaterialButton
    private lateinit var tvLogin: TextView   // ← Login link

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameLayout = findViewById(R.id.name_layout)
        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)
        confirmLayout = findViewById(R.id.confirm_password_layout)
        name = findViewById(R.id.name_edit_text)
        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)
        confirm = findViewById(R.id.confirm_password_edit_text)
        signupBtn = findViewById(R.id.btn_signup)
        tvLogin = findViewById(R.id.tv_login)   // The "Login" text

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.signupResult.observe(this) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    signupBtn.isEnabled = false
                    signupBtn.text = "Creating account..."
                }
                is AuthResult.Success -> {
                    signupBtn.isEnabled = true
                    signupBtn.text = "Sign Up"
                    Toast.makeText(this, "Signup successful! Please log in.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is AuthResult.Error -> {
                    signupBtn.isEnabled = true
                    signupBtn.text = "Sign Up"
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
                AuthResult.Idle -> {}
            }
        }

        signupBtn.setOnClickListener { performSignup() }

        // ✅ ADD THIS – make the "Login" link clickable
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performSignup() {
        val nameText = name.text?.toString()?.trim() ?: ""
        val emailText = email.text?.toString()?.trim() ?: ""
        val passText = password.text?.toString()?.trim() ?: ""
        val confirmText = confirm.text?.toString()?.trim() ?: ""

        nameLayout.error = null
        emailLayout.error = null
        passwordLayout.error = null
        confirmLayout.error = null

        if (nameText.isEmpty()) {
            nameLayout.error = "Name is required"
            name.requestFocus()
            return
        }
        if (emailText.isEmpty()) {
            emailLayout.error = "Email is required"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailLayout.error = "Enter valid email"
            email.requestFocus()
            return
        }
        if (passText.isEmpty()) {
            passwordLayout.error = "Password is required"
            password.requestFocus()
            return
        }
        if (passText.length < 6) {
            passwordLayout.error = "Password must be at least 6 characters"
            password.requestFocus()
            return
        }
        if (confirmText.isEmpty()) {
            confirmLayout.error = "Confirm password"
            confirm.requestFocus()
            return
        }
        if (passText != confirmText) {
            confirmLayout.error = "Passwords do not match"
            confirm.requestFocus()
            return
        }

        viewModel.signup(emailText, passText)
    }
}