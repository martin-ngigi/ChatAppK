package com.example.chatappk.Activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatappk.Models.ModelUser
import com.example.chatappk.R
//import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog

class SignUpActivity : AppCompatActivity() {

    private lateinit var  edtName : EditText // lateinit var means it will be initialized later
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var alertDialog: AlertDialog
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

        //hide action bar
        supportActionBar?.hide()

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()

        alertDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Please wait")
            .setCancelable(false)
            .setTheme(R.style.DialogCustomTheme)
            .build()



        //intent to go to LoginActivity
        btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity :: class.java)
            startActivity(intent);
        }

        btnSignUp.setOnClickListener{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            //invoke signUpMethod
            signUpMethod(name, email, password)
        }


    }

    private fun signUpMethod(name: String, email: String, password: String) {
        //validate if inputs are empty or not
        if (TextUtils.isEmpty(name)){
            edtName.setError("Name Field is empty")
            return
        }
        else if (TextUtils.isEmpty(email)){
            edtEmail.setError("Email Field is empty")
            return
        }
        else if (TextUtils.isEmpty(password)){
            edtPassword.setError("Password Field is empty")
            return
        }


        alertDialog.show()

        //creating new user account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    saveUserDataToDB(name, email, firebaseAuth.currentUser?.uid!!) // !! means non-null, ? means safe
                    Toast.makeText(baseContext, "Created Account Successfully.", Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.
                    alertDialog.dismiss()
                    Toast.makeText(this@SignUpActivity, "Authentication failed: "+task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToDB(name: String, email: String, uid: String) {
        alertDialog.setMessage("Saving User's data ")

        //val reference : DatabaseReference
        reference = FirebaseDatabase.getInstance().getReference()

//        val hashMap = HashMap<String, String>()
//        hashMap.put("name", name)
//        hashMap.put("email", email)
//        hashMap.put("uid", uid)

        val modelUser = ModelUser(name, email, uid) //or use above hashmap

        reference.child("Users").child(uid).setValue(modelUser)
            .addOnSuccessListener {
                // saved data successfully
                alertDialog.dismiss()
                Toast.makeText(this@SignUpActivity, name+" Date Saved Successfully", Toast.LENGTH_SHORT).show()

                //intent to start activity Main
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener(this){exception ->
                //failed saving data
                alertDialog.dismiss()
                Toast.makeText(this@SignUpActivity, "Error: "+exception.toString(), Toast.LENGTH_SHORT).show()
            }


    }
}