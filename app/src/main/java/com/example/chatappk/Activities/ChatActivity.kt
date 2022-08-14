package com.example.chatappk.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappk.Adapters.AdapterMessage
import com.example.chatappk.Models.ModelMessage
import com.example.chatappk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chat_RV: RecyclerView
    private lateinit var msgBoxEdt: EditText
    private lateinit var sendMsgBtn: ImageView
    private lateinit var adapterMessage: AdapterMessage
    private lateinit var messageList: ArrayList<ModelMessage>
    private lateinit var reference: DatabaseReference

    //room is used to create a unique room for sender and receiver so that the message is pr
    //private and don't reflect on every user
    var receiverRoom: String? = null
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //get data from AdapterUser
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        //init database
        reference = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        //set name in action  bar
        supportActionBar?.title = name

        chat_RV = findViewById(R.id.chat_RV)
        msgBoxEdt = findViewById(R.id.msgBoxEdt)
        sendMsgBtn = findViewById(R.id.sendMsgBtn)

        messageList = ArrayList()
        adapterMessage = AdapterMessage(this, messageList)

        chat_RV.layoutManager = LinearLayoutManager(this) //no need, since already set in UI
        chat_RV.adapter = adapterMessage

        //get All messages
        getAllMessages()

        //handle sendBtn onclickListener
        sendMsgBtn.setOnClickListener{
            sendMessage(senderUid, senderRoom!!, receiverRoom!!)
        }

    }

    private fun getAllMessages() {
        //db path
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        databaseReference
            .child("Chats")
            .child(senderRoom!!)
            .child("Messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        //get messages
                        val message = postSnapshot.getValue(ModelMessage::class.java)
                        messageList.add(message!!)

                    }
                    adapterMessage.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    private fun sendMessage(senderUid: String?, senderRoom: String, receiverRoom: String) {
        //get current time
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(Date())

        val message = msgBoxEdt.text.toString()
        val messageObject = ModelMessage(message, senderUid, currentDate)

        //db path
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        databaseReference
            .child("Chats")
            .child(senderRoom!!)
            .child("Messages")
            .push() //push will create a unique key
            .setValue(messageObject)
            .addOnSuccessListener {
                Toast.makeText(this@ChatActivity, "Sent", Toast.LENGTH_SHORT).show()

                //also update receiver room
                databaseReference
                    .child("Chats")
                    .child(receiverRoom!!)
                    .child("Messages")
                    .push() //push will create a unique key
                    .setValue(messageObject)

            }.addOnFailureListener(this){ exception ->
                Toast.makeText(this@ChatActivity, "Error! Couldn't send \n"+exception.toString(), Toast.LENGTH_SHORT).show()
            }

        //after sending message, clear the message
        msgBoxEdt.setText("")

    }
}