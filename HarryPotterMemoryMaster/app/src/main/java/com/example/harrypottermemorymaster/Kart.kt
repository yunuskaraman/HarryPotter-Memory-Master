package com.example.harrypottermemorymaster

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.Decoder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Base64.getDecoder
import kotlin.reflect.typeOf

class Kart(context: Context, id:Int, resimId:String, ev:String, imgUrl:String, adi:String, puan:String) : AppCompatImageView(context!!) {


    var acikmi = false
    var resimId = 0
    var arkaResim = 0
    var imgUrl: String? = null
    var ev: String? = null
    var adi :String? = null
    var puan: Int? = null
    var cevrilebilir = true
    var katsayi = 0
    init {

        this.ev = ev
        this.imgUrl = imgUrl
        this.adi = adi
        this.puan = puan.toInt()
        this.resimId=resimId.toInt()
        setId(id)
        if(this.ev =="gryffindor" || this.ev == "slytherin"){
            this.katsayi = 2
        }else{
            this.katsayi = 1
        }

        arkaResim = R.drawable.arka
        this.setBackgroundResource(arkaResim)
    }
    public fun cevir(){
        if (!cevrilebilir) return
        if(!acikmi){ //arkası çevrili
            this.setImageBitmap(imgUrl?.let { decodeImage(it) })
            acikmi=true
        }
        else{
            arkaResim = R.drawable.arka
            this.setImageResource(arkaResim)
            acikmi=false

        }
    }
    private fun decodeImage(imgUrl:String): Bitmap? {
        val imageBytes = Base64.decode(imgUrl,0)
        var img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return img
    }


}