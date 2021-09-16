package com.example.trakedemokotlin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.trakedemokotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LobbyActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    lateinit var button_enter: Button
    lateinit var textview_error_enter: TextView
    lateinit var edittext_roomid: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        button_enter = findViewById(R.id.button_enter)
        textview_error_enter = findViewById(R.id.textview_error_enter)
        edittext_roomid = findViewById(R.id.edittext_roomid)

        checkUser()
        setViewListeners()
    }

    private fun setViewListeners() {
        button_enter.setOnClickListener { enterRoom() }
    }

    private fun enterRoom() {
        button_enter.isEnabled = false
        val roomId = edittext_roomid.text.toString()

        if (roomId.isEmpty()) {
            showErrorMessage()
            return
        }

        firestore.collection("users").document(user!!.uid).collection("rooms")
            .document(roomId).set(mapOf(
                Pair("id", roomId)
            ))

        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra("INTENT_EXTRA_ROOMID", roomId)
        startActivity(intent)
    }

    private fun showErrorMessage() {
        textview_error_enter.visibility = View.VISIBLE
        button_enter.isEnabled = true
    }

    private fun checkUser() {
        if (user == null)
            launchLogin()
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun signOut() {
        auth.signOut()
        launchLogin()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.ic_sign_out) {
            signOut()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        button_enter.isEnabled = true
    }
}
