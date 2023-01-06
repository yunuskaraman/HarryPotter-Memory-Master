package com.example.harrypottermemorymaster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView

class DetayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detay)

        val adi = intent.getStringExtra("adi")
        val ev = intent.getStringExtra("ev")
        val puan = intent.getStringExtra("puan")
        val resim = intent.getStringExtra("resim")
        println("resim : "+resim.toString())

        val img = findViewById<ImageView>(R.id.imageView2)
        val textAdi = findViewById<TextView>(R.id.textView8)
        val textEv = findViewById<TextView>(R.id.textView9)
        val textPuan = findViewById<TextView>(R.id.textView10)

        img.setImageBitmap(resim?.let { decodeImage(it) })
        textAdi.setText("adi : "+adi)
        textEv.setText("evi : "+ev)
        textPuan.setText("puan : "+puan)

    }

    private fun decodeImage(imgUrl:String): Bitmap? {
        val imageBytes = Base64.decode(imgUrl,0)
        var img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return img
    }
}