package com.example.harrypottermemorymaster


import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import java.io.Serializable


class Muzik(context: Context,sesAcik:Boolean) {

    lateinit var playerOyun: MediaPlayer
    lateinit var playerEslesti:MediaPlayer
    lateinit var playerBitti:MediaPlayer
    lateinit var playerSureBitti:MediaPlayer
    var sesAcikMi:Boolean?
    init {
        sesAcikMi = sesAcik
        playerOyun =  MediaPlayer.create(context,R.raw.oyun)
        playerOyun.isLooping = true
        if (sesAcikMi == true){
            playerOyun.start()
        }


        playerEslesti =  MediaPlayer.create(context,R.raw.eslesme)
        playerEslesti.isLooping = true

        playerBitti =  MediaPlayer.create(context,R.raw.bitti)
        playerBitti.isLooping = true

        playerSureBitti =  MediaPlayer.create(context,R.raw.sure_bitti)
        playerSureBitti.isLooping = true

    }

    fun oyunMuzik(): Boolean? {

        if (sesAcikMi == true){
            playerOyun.pause()
            sesAcikMi = false
        }else{
            playerOyun.start()
            sesAcikMi = true
        }
        return sesAcikMi

    }
    fun eslestiMuzik(){
        if (sesAcikMi == true){
            val tm = object : CountDownTimer(2500,1000){
                override fun onTick(p0: Long) {
                    playerEslesti.start()
                }

                override fun onFinish() {
                    playerEslesti.pause()
                }

            }
            tm.start()
        }

    }
    fun sureBittiMuzik(){

        if (sesAcikMi == true){
            val tm = object : CountDownTimer(2000,1000){
                override fun onTick(p0: Long) {
                    playerSureBitti.start()
                }

                override fun onFinish() {
                    playerSureBitti.pause()
                }

            }
            tm.start()
        }

    }
    fun eslesmeBittiMuzik(){
        if (sesAcikMi == true)
        {
            val tm = object : CountDownTimer(9000,1000){
                override fun onTick(p0: Long) {
                    playerBitti.start()
                }

                override fun onFinish() {
                    playerBitti.pause()
                }

            }
            tm.start()
        }
    }
}