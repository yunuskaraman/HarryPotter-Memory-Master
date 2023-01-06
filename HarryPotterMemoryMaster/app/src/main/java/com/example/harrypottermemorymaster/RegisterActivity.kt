package com.example.harrypottermemorymaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = Firebase.auth

        val btnKayit = findViewById<Button>(R.id.btnKayit)
        btnKayit.setOnClickListener {
            signUp()
        }


    }



    private fun signUp(){
        val name = findViewById<EditText>(R.id.editTextName)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)

        val db = Firebase.firestore

        if(email.text.isEmpty() || password.text.isEmpty() || name.text.isEmpty()){
            Toast.makeText(this, "Lütfen Boşluları Doldurunuz.",Toast.LENGTH_SHORT).show()
        }
        val inputName = name.text.toString()
        val inputEmail = email.text.toString()
        val inputSifre = password.text.toString()

        var a = false


        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    var userName = document.data["KullaniciAdi"].toString()

                    if(userName == inputName){
                        a = true
                        Toast.makeText(this,"Girdiğiniz kullanıcı adı bulunmaktadır.",Toast.LENGTH_SHORT).show()
                        break
                    }
                }

                if (a == false) {
                    val kullanici = hashMapOf(
                        "KullaniciAdi" to "${inputName}",
                        "Email" to "${inputEmail}",
                        "Sifre" to "${inputSifre}"
                    )
                    auth.createUserWithEmailAndPassword(inputEmail, inputSifre)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                val user = Firebase.auth.currentUser


                                user!!.sendEmailVerification()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                baseContext,
                                                "Kayıt Başarılı, Onay Maili Gönderildi",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            db.collection("users")
                                                .add(kullanici)

                                            val intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    baseContext, "Kayıt Gerçekleşmedi.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                        .addOnCanceledListener {
                            Toast.makeText(
                                baseContext, "error ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Error getting documents: ${exception} ",Toast.LENGTH_SHORT ).show()
            }



    }
}