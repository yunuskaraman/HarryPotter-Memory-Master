package com.example.harrypottermemorymaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var  auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val btnGiris = findViewById<Button>(R.id.btnGiris)
        val textSifre = findViewById<TextView>(R.id.textSifre)
        val textKayit = findViewById<TextView>(R.id.textKayit)

        btnGiris.setOnClickListener {
            signin()
        }

        textSifre.setOnClickListener {
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }

        textKayit.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signin(){

        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)

        if(email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Lütfen boş yerleri doldurunuz",Toast.LENGTH_SHORT).show()
        }
        val inputEmail = email.text.toString()
        val inputSifre = password.text.toString()

        auth.signInWithEmailAndPassword(inputEmail, inputSifre)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = Firebase.auth.currentUser
                    if(user  != null){
                        if (user.isEmailVerified){
                            Toast.makeText(baseContext, "Giriş Başarılı.",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SecimActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(baseContext, "Lütfen Email Onaylayınız.",
                                Toast.LENGTH_SHORT).show()
                        }

                    }


                } else {

                    Toast.makeText(baseContext, "Giriş Başarısız.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}