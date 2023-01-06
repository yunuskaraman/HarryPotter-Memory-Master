package com.example.harrypottermemorymaster

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class SecimActivity : AppCompatActivity() {


    lateinit var  muzik :Muzik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secim)

         muzik = Muzik(this,true)


        var oyunZorluk = 2
        var oyuncu = 1

            val rgZorluk = findViewById<RadioGroup>(R.id.rgZorluk)
            rgZorluk.setOnCheckedChangeListener { radioGroup, checkId ->
                if (R.id.rbKolay == checkId){
                    oyunZorluk = 2
                }
                if (R.id.rbOrta == checkId){
                    oyunZorluk = 4
                }
                if (R.id.rbZor == checkId){
                    oyunZorluk = 6
                }
            }

            var rgOyuncu = findViewById<RadioGroup>(R.id.rgOyuncu)
            rgOyuncu.setOnCheckedChangeListener { radioGroup, checkId ->
                if (R.id.rbTekOyuncu == checkId){
                    oyuncu = 1
                }
                if (R.id.rbIkiOyuncu == checkId){
                    oyuncu = 2
                }
            }

        val oyunuBaslat = findViewById<Button>(R.id.btnBaslat)
        oyunuBaslat.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("zorluk",oyunZorluk.toString())
            intent.putExtra("oyuncu",oyuncu.toString())
            var isMuzik = muzik.sesAcikMi
            if (isMuzik == true){
                intent.putExtra("isMuzik","true")
                muzik.oyunMuzik()
            }else{
                intent.putExtra("isMuzik","false")
            }
            startActivity(intent)
        }

    }

   fun btnPlayClick(v:View){
        val btnPlay = findViewById<Button>(R.id.btnPlay)

        var isMuzik = muzik.oyunMuzik()
        if (isMuzik == false){
            btnPlay.setBackgroundResource(R.drawable.ses_kapat)
        }else{
            btnPlay.setBackgroundResource(R.drawable.ses_ac)
        }
    }
}