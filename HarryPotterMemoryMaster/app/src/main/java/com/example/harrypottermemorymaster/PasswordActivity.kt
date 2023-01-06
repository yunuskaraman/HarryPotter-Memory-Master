package com.example.harrypottermemorymaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        auth = Firebase.auth

        var btnGonder = findViewById<Button>(R.id.btnGonder)

        btnGonder.setOnClickListener {
            resetPassword()
        }

    }

    private fun resetPassword(){
        val emailAddress = findViewById<EditText>(R.id.editTextEmail)

        Firebase.auth.sendPasswordResetEmail(emailAddress.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Şifre yenileme Email adresinize gönderildi.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}