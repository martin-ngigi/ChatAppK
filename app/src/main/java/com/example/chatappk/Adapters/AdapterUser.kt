package com.example.chatappk.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappk.Activities.ChatActivity
import com.example.chatappk.Models.ModelUser
import com.example.chatappk.R
import com.google.firebase.auth.FirebaseAuth

class AdapterUser(val context: Context, val userList: ArrayList<ModelUser>):
    RecyclerView.Adapter<AdapterUser.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        //inititalize View
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //get data
        val currentUser = userList[position]

        //set data
        holder.nameTvUR.text = currentUser.name

        //handle onclick listener
        //move to chat activity
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            //pass name
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        //return the count
        return userList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTvUR = itemView.findViewById<TextView>(R.id.nameTvRU)
    }
}