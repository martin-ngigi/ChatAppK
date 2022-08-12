package com.example.chatappk

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText;
    private lateinit var edtPassword: EditText;
    private lateinit var btnLogin: Button;
    private lateinit var btnSignUp: Button;
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var alertDialog: AlertDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

        firebaseAuth = FirebaseAuth.getInstance()

        //alert dialog
        alertDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Please wait")
            .setCancelable(false)
            .setTheme(R.style.DialogCustomTheme)
            .build()


        //intent to go to SignUpActivity
        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
        }

        //handle login btn click listener
        btnLogin.setOnClickListener {

            //get data from UI
            var email = edtEmail.text.toString()
            var password = edtPassword.text.toString()

            //invoke login function
            loginMethod(email, password)
        }
    }

    private fun loginMethod(email : String, password : String) {

        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Email Field is empty")
            return
        }
        else if (TextUtils.isEmpty(password)){
            edtPassword.setError("Password Field is empty")
            return
        }


        alertDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity){ task ->
                if (task.isSuccessful){
                    //Login success
                    alertDialog.dismiss()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    //login failed
                    alertDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Error Login in: "+task.exception, Toast.LENGTH_SHORT).show()

                }
            }

    }
}