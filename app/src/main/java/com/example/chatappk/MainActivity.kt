package com.example.chatappk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var users_RV: RecyclerView
    private lateinit var adapterUser: AdapterUser
    private lateinit var userList: ArrayList<ModelUser>
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        users_RV = findViewById(R.id.users_RV)

        //invoke get all users method
        getALlUsers()
    }

    private fun getALlUsers(){
        //initialize arraylist
        userList = ArrayList()

        //initialize adapter
        adapterUser = AdapterUser(this, userList)

        //set layout manager
        users_RV.layoutManager = LinearLayoutManager(this) //no need since already set in the ui
        users_RV.adapter = adapterUser

        val reference : DatabaseReference
        reference = FirebaseDatabase.getInstance().getReference()
        reference.child("Users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //clear the previous list
                userList.clear()

                for (postSnapshot in snapshot.children){
                    //get one user
                    val currentUser = postSnapshot.getValue(ModelUser::class.java)

                    //show all users apart from me
                    if (firebaseAuth.currentUser?.uid != currentUser?.uid){

                        //add the current user to the user list
                        userList.add(currentUser!!)
                    }

                }

                //when a new user is add, reflect on the list
                adapterUser.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            //log out and proceed ti login activity
            firebaseAuth.signOut()

            //intent
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
            return true
        }
        return true
    }
}