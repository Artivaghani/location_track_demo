package com.example.trakedemokotlin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trakedemokotlin.Adapter.ChatAdapter
import com.example.trakedemokotlin.Model.ChatMessage
import com.example.trakedemokotlin.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

import java.util.*
import kotlin.collections.ArrayList

class RoomActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()

    val chatMessages = ArrayList<ChatMessage>()
    var chatRegistration: ListenerRegistration? = null
    var roomId: String? = null

    lateinit var button_send: ImageView
    lateinit var list_chat: RecyclerView
    lateinit var edittext_chat: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fbchat__acitvity)

        button_send = findViewById(R.id.button_send)
        list_chat=findViewById(R.id.list_chat)
        edittext_chat=findViewById(R.id.edittext_chat)

        checkUser()
        initList()
        setViewListeners()
    }

    private fun setViewListeners() {

        button_send.setOnClickListener {
            sendChatMessage()
        }
    }

    private fun initList() {
        if (user == null)
            return

        list_chat.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(chatMessages, user.uid)
        list_chat.adapter = adapter
        listenForChatMessages()
    }

    private fun listenForChatMessages() {
        roomId = intent.getStringExtra("INTENT_EXTRA_ROOMID")
        if (roomId == null) {
            finish()
            return
        }

        chatRegistration = firestore.collection("rooms")
            .document(roomId!!)
            .collection("messages")
            .addSnapshotListener { messageSnapshot, exception ->

                if (messageSnapshot == null || messageSnapshot.isEmpty)
                    return@addSnapshotListener

                chatMessages.clear()

                for (messageDocument in messageSnapshot.documents) {

                    chatMessages.add(
                        ChatMessage(
                            messageDocument["text"] as String,
                            messageDocument["user"] as? String,
                            messageDocument["timestamp"] as? Date
                        ))
                }


                chatMessages.sortBy { it.timestamp }
                list_chat.adapter?.notifyDataSetChanged()
            }
    }

    private fun sendChatMessage() {
        val message = edittext_chat.text.toString()
        edittext_chat.setText("")

        firestore.collection("rooms").document(roomId!!).collection("messages")
            .add(mapOf(
                Pair("text", message),
                Pair("user", user?.uid),
                Pair("timestamp", Timestamp.now())
            ))
        initList()
    }

    private fun checkUser() {
        if (user == null)
            launchLogin()
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

//        var email: String = "vaghaniruhi1@gmail.com"
//        var password: String = "aru9925369535"
//        auth.signInWithEmailAndPassword(email!!, password!!)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "oops", Toast.LENGTH_SHORT).show()
//                }
//            }
    }

    private fun signOut() {
        auth.signOut()
        launchLogin()
    }


    override fun onDestroy() {
        chatRegistration?.remove()
        super.onDestroy()
    }
}
