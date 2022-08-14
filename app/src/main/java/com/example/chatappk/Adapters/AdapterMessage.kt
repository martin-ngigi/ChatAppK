package com.example.chatappk.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappk.Models.ModelMessage
import com.example.chatappk.R
import com.google.firebase.auth.FirebaseAuth

class AdapterMessage(val context: Context, val messageList: ArrayList<ModelMessage>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //help im returning the two views
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.row_receive_message, parent, false)
            return ReceiveMessageViewHolder(view)
        }
        else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.row_sent_message, parent, false)
            return SentMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //get current message
        val currentMessage = messageList[position]

        //sent viewHolder
        if (holder.javaClass == SentMessageViewHolder::class.java){
            //typecast viewholder to SentMessageViewHolder
            val viewHolder = holder as SentMessageViewHolder

            //set message and time to the ui
            holder.sent_msgTv.text = currentMessage.message
            holder.timeSentTv.text = currentMessage.time
        }
        //Receive view Holder
        else{
            //type cast viewholder ReceiveMessageViewHolder
            val viewHolder = holder as ReceiveMessageViewHolder

            //set message and time to the ui
            holder.receive_msgTV.text = currentMessage.message
            holder.timeReceivedTv.text = currentMessage.time

        }
    }

    //return either sentView or Receive view
    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        //if the uid of current user  matches with the sender id of currentMessage,, then inflate send view holder
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid
        if (uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sent_msgTv = itemView.findViewById<TextView>(R.id.sent_msgTV)
        val timeSentTv = itemView.findViewById<TextView>(R.id.timeSentTv)
    }

    class ReceiveMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receive_msgTV = itemView.findViewById<TextView>(R.id.receive_msgTV)
        val timeReceivedTv = itemView.findViewById<TextView>(R.id.timeReceivedTv)
    }
}