package com.example.chatappk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

    }

    override fun getItemCount(): Int {
        //return the count
        return userList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTvUR = itemView.findViewById<TextView>(R.id.nameTvRU)
    }
}