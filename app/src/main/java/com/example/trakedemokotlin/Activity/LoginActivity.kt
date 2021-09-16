package com.example.trakedemokotlin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.trakedemokotlin.R
import com.google.firebase.auth.FirebaseAuth

import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    var email: String? = null
    var password: String? = null
    lateinit var button_login: Button
    lateinit var textview_register: TextView
    lateinit var edittext_password: EditText
    lateinit var edittext_email: EditText
    lateinit var textview_error_login: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login = findViewById(R.id.button_login)
        textview_register = findViewById(R.id.textview_register)
        edittext_password = findViewById(R.id.edittext_password)
        edittext_email = findViewById(R.id.edittext_email)
        textview_error_login = findViewById(R.id.textview_error_login)
        setViewListeners()
    }

    private fun setViewListeners() {

        button_login.setOnClickListener { submit() }
        textview_register.setOnClickListener { launchRegister() }
    }

    private fun submit() {
        button_login.isEnabled = false

        email = edittext_email.text.toString()
        password = edittext_password.text.toString()

        if (validate()) {
            login()
        } else {
            showErrorMessage()
        }
    }

    private fun validate(): Boolean {
        return !email.isNullOrEmpty()
            && !password.isNullOrEmpty()
            && isEmailValid(email!!)
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun login() {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    launchLobby()
                } else {
                    showErrorMessage()
                }
            }
    }

    private fun launchLobby() {
        val intent = Intent(this, LobbyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun launchRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showErrorMessage() {
        textview_error_login.visibility = View.VISIBLE
        button_login.isEnabled = true
    }

}
