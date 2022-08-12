package com.example.chatappk

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog

class SignUpActivity : AppCompatActivity() {

    private lateinit var  edtName : EditText;
    private lateinit var edtEmail : EditText;
    private lateinit var edtPassword : EditText;
    private lateinit var btnLogin : Button;
    private lateinit var btnSignUp : Button;
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var alertDialog: AlertDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

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
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            //invoke signUpMethod
            signUpMethod(email, password)
        }


    }

    private fun signUpMethod(email: String, password: String) {
        //validate if inputs are empty or not
        if (TextUtils.isEmpty(edtName.text.toString())){
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
                    alertDialog.dismiss()
                    Toast.makeText(baseContext, "Created Account Successfully.", Toast.LENGTH_SHORT).show()

                    //intent to start activity Main
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // If sign in fails, display a message to the user.
                    alertDialog.dismiss()
                    Toast.makeText(this@SignUpActivity, "Authentication failed: "+task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }
}