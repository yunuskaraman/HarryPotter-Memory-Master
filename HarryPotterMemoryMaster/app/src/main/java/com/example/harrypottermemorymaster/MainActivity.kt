package com.example.harrypottermemorymaster

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.Base64.getDecoder
import kotlin.random.Random
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    lateinit var  muzik :Muzik


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnPlay = findViewById<Button>(R.id.btnSes)

        var isMuzik = intent.getStringExtra("isMuzik")
        println("ses açık mı : "+isMuzik.toBoolean())
        if (isMuzik.toBoolean() == false){
            btnPlay.setBackgroundResource(R.drawable.ses_kapat)
        }
        muzik = Muzik(this,isMuzik.toBoolean())

        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        var kullaniciAdi :String? = null
        var k_email:String? = null
        user?.let {
            k_email = user.email.toString()
        }
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.data["Email"] == k_email){
                        kullaniciAdi = document.data["KullaniciAdi"].toString()
                    }
                }
                var textKullanici = findViewById<TextView>(R.id.textKullanici)
                textKullanici.setText("Hoş Geldin, ${kullaniciAdi}")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Error getting documents: ${exception} ",Toast.LENGTH_SHORT ).show()
            }


        var oyunSure = 60
        val zorluk = intent.getStringExtra("zorluk")?.toInt()
        var oyuncu = intent.getStringExtra("oyuncu")?.toInt()
        if (oyuncu == 1){
            val textView3 = findViewById<TextView>(R.id.textView3)
            textView3.alpha = 0.0F
            val puan2 = findViewById<TextView>(R.id.textPuan2)
            puan2.alpha = 0.0F
            oyunSure = 45
        }
        val lnKartlar = findViewById<LinearLayout>(R.id.lnKartlar)
        val textPuan1 = findViewById<TextView>(R.id.textPuan1)
        val textPuan2 = findViewById<TextView>(R.id.textPuan2)
        val intent = Intent(this,FinishActivity::class.java)
        var puan1 = 0
        var puan2 = 0
        var bilgiler = HashMap<Int,HashMap<String,String>>()
        var randomBilgiler = HashMap<Int,HashMap<String,String>>()
        var kartListe = ArrayList<Kart>()
        var kartId = 0




        db.collection("resimler")
            .get()
            .addOnSuccessListener { result ->
                var i:Int = 0
                for (document in result) {
                    var resimBilgileri = HashMap<String,String>()
                    var imgUrl = document.data["image"].toString()
                    resimBilgileri.put("adi","${document.data["name"].toString()}")
                    resimBilgileri.put("ev","${document.data["house"].toString()}")
                    resimBilgileri.put("puan","${document.data["point"].toString()}")
                    resimBilgileri.put("resim","${document.data["image"].toString()}")
                    //println("name  => "+document.data["name"] +" point  => "+document.data["point"])
                    i+=1
                    bilgiler.put(i,resimBilgileri)

                    //println("name  => "+bilgiler[i]!!["ad"]+" resim  => "+bilgiler[i]!!["resim"])

                }
                var randomDizi = ArrayList<Int>()

                var j =1
                while(j<=(zorluk!! * zorluk/2)){
                    var randSayi = Random.nextInt(1,45)
                    //println("rand Sayi "+randSayi)
                    if(randomDizi.contains(randSayi) == false){
                        randomDizi.add(randSayi)
                        j++
                    }
                }

                j=1
                for (i in randomDizi){
                    //println("rand Sayı : "+i)
                    randomBilgiler.put(j,bilgiler[i]!!)
                    j++
                }
                /*while(j<=8){

                    var randSayi = Random.nextInt(45)
                    println("Random Sayi => "+randSayi)
                    if(randomDizi.isEmpty()){
                        randomDizi.add(randSayi)
                        randomBilgiler.put(j,bilgiler[randSayi]!!)
                        j++
                        continue
                    }

                    if(randomDizi.contains(randSayi) == false){
                        randomDizi.add(randSayi)
                        randomBilgiler.put(j,bilgiler[randSayi]!!)
                        println("random adi =>"+bilgiler[randSayi]!!["ad"])
                        j++
                    }



                }*/

                var resimTekrar = 1
                for (i in 1..(zorluk!! * zorluk)){

                    if (i == (zorluk!! * zorluk/2)+1){
                        resimTekrar=1
                    }
                    kartListe.add(Kart(this,i,resimTekrar.toString(),randomBilgiler[resimTekrar]!!["ev"].toString(),randomBilgiler[resimTekrar]!!["resim"].toString(),randomBilgiler[resimTekrar]!!["adi"].toString(),randomBilgiler[resimTekrar]!!["puan"].toString()))
                    resimTekrar++



                }

                for (i in 1..(zorluk!! * zorluk)){
                    var randSayi = Random.nextInt(0,(zorluk!! * zorluk))
                    var k:Kart = kartListe[randSayi]
                    kartListe[randSayi] = kartListe[i-1]
                    kartListe[i-1] = k
                }
                for(i in 1..(zorluk*zorluk/2)){
                    intent.putExtra("ev${i}",randomBilgiler[i]!!["ev"].toString())
                    intent.putExtra("resim${i}",randomBilgiler[i]!!["resim"].toString())
                    intent.putExtra("adi${i}",randomBilgiler[i]!!["adi"].toString())
                    intent.putExtra("puan${i}",randomBilgiler[i]!!["puan"].toString())
                }

                var sonKart = 0

                var eslesti = 0
                var oyuncu1 = true
                for(i in 1..(zorluk!! * zorluk)){
                    kartListe[i-1].setOnClickListener {
                        println("Kart adi: "+kartListe[i-1].adi)
                        kartListe[i-1].cevir()
                        if (sonKart>0){

                            var kart2:Kart = findViewById(sonKart)
                            sonKart=0
                            val myTimer = object : CountDownTimer(500,100){

                                override fun onTick(p0: Long) {
                                    if(kart2.resimId == kartListe[i-1].resimId && kart2.id != kartListe[i-1].id){
                                        muzik.eslestiMuzik()
                                    }
                                }
                                override fun onFinish() {

                                    if(kart2.resimId == kartListe[i-1].resimId && kart2.id != kartListe[i-1].id){
                                        //esleştiler

                                        kart2.cevrilebilir=false
                                        kartListe[i-1].cevrilebilir=false
                                        if (oyuncu == 2){ // iki kişilik
                                            if (oyuncu1 == true){ //birinci oyunuc
                                                puan1 += (2 * kart2.puan!!*kart2.katsayi)
                                                textPuan1.setText(puan1.toString())

                                            }else{ // ikinci oyuncu
                                                puan2 += (2 * kart2.puan!!*kart2.katsayi)
                                                textPuan2.setText(puan2.toString())

                                            }

                                        }else{ //tek kişilik
                                            puan1 += (2 * kart2.puan!!*kart2.katsayi)*(oyunSure/10)
                                                    textPuan1.setText(puan1.toString())
                                        }


                                        eslesti++
                                        if(eslesti == (zorluk*zorluk/2)){
                                            muzik.eslesmeBittiMuzik()
                                            muzik.playerOyun.pause()

                                            intent.putExtra("oyuncu",oyuncu.toString())
                                            if (oyuncu == 1){
                                                intent.putExtra("skor1",puan1.toString())
                                            }else{
                                                intent.putExtra("skor1",puan1.toString())
                                                intent.putExtra("skor2",puan2.toString())
                                            }
                                            intent.putExtra("zorluk",(zorluk*zorluk/2).toString())
                                            startActivity(intent)
                                        }

                                    }
                                    else{//eşleşmediler
                                        if(oyuncu == 2){
                                            if(oyuncu1 == true){
                                                if(kart2.ev == kartListe[i-1].ev){
                                                    puan1 -= ((kart2.puan!! + kartListe[i-1].puan!!)/kart2.katsayi)

                                                }else{
                                                    puan1 -= ((kart2.puan!! + kartListe[i-1].puan!!)/2)*kart2.katsayi * kartListe[i-1].katsayi
                                                }
                                                textPuan1.setText(puan1.toString())
                                                oyuncu1 = false
                                            }else{
                                                if(kart2.ev == kartListe[i-1].ev){
                                                    puan2 -= ((kart2.puan!! + kartListe[i-1].puan!!)/kart2.katsayi)

                                                }else{
                                                    puan2 -= ((kart2.puan!! + kartListe[i-1].puan!!)/2)*kart2.katsayi * kartListe[i-1].katsayi
                                                }
                                                textPuan2.setText(puan2.toString())
                                                oyuncu1 = true
                                            }
                                            kart2.cevir()
                                            kartListe[i-1].cevir()
                                        }
                                        else{
                                            if(kart2.ev == kartListe[i-1].ev){
                                                puan1 -= ((kart2.puan!! + kartListe[i-1].puan!!)/kart2.katsayi) * (oyunSure/10)
                                            }else{
                                                puan1 -= (((kart2.puan!! + kartListe[i-1].puan!!)/2)*kart2.katsayi * kartListe[i-1].katsayi) * (oyunSure/10)
                                            }
                                            kart2.cevir()
                                            kartListe[i-1].cevir()
                                            textPuan1.setText(puan1.toString())
                                        }


                                    }
                                }
                            }

                            myTimer.start()

                        }else{
                            sonKart = kartListe[i-1].id
                        }

                    }
                }
                /*
                birinci oyuncu puan hesaplamarı
                 puan1 += (2 * kart2.puan!!*kart2.katsayi)*(oyunSure/10) kazandı
                  textPuan1.setText(puan1.toString())

                 evleri aynı olan kartlar kaybetti
                 puan1 -= ((kart2.puan!! + kartListe[i-1].puan!!)/kart2.katsayi) * (oyunSure/10)

                 evleri fakrlı olan kartlar kaybetti
                 puan1 -= (((kart2.puan!! + kartListe[i-1].puan!!)/2)*kart2.katsayi * kartListe[i-1].katsayi) * (oyunSure/10)
                 */
                println("seviye "+zorluk)
                for(i in 1..zorluk){


                    val ln_row=LinearLayout(this)
                    ln_row.orientation = LinearLayout.HORIZONTAL
                    lnKartlar.addView(ln_row)

                    for (j in 1..zorluk){

                        //var k = Kart(this)
                        val parms: LinearLayout.LayoutParams = LinearLayout.LayoutParams((1080/zorluk), (1200/zorluk))
                        kartListe[kartId].layoutParams = parms
                        ln_row.addView(kartListe[kartId])
                        //kartListe[kartId].setImageBitmap(decodeImage(kartListe[kartId].imgUrl.toString()))
                        kartId++
                        //btn_new.setOnClickListener { buttonListener }
                    }

                }


                val sure = object :CountDownTimer(oyunSure.toLong()*1000,1000){
                    override fun onTick(p0: Long) {
                        oyunSure--
                        val textSure = findViewById<TextView>(R.id.textSure)
                        textSure.setText(oyunSure.toString())

                    }

                    override fun onFinish() {
                        if (eslesti != (zorluk*zorluk/2) ){
                            muzik.sureBittiMuzik()
                            muzik.playerOyun.pause()

                            intent.putExtra("oyuncu",oyuncu.toString())
                            if (oyuncu == 1){
                                intent.putExtra("skor1",puan1.toString())
                            }else{
                                intent.putExtra("skor1",puan1.toString())
                                intent.putExtra("skor2",puan2.toString())
                            }

                            intent.putExtra("zorluk",(zorluk*zorluk/2).toString())
                            startActivity(intent)
                        }

                    }

                }
                sure.start()



            }
            .addOnFailureListener { exception ->
                println("hata")
            }

    }

    fun btnSesClick(v:View){
        val btnSes = findViewById<Button>(R.id.btnSes)
        var isMuzik = muzik.oyunMuzik()
        if (isMuzik == false){
            btnSes.setBackgroundResource(R.drawable.ses_kapat)
        }else{
            btnSes.setBackgroundResource(R.drawable.ses_ac)
        }
    }

   /* private fun decodeImage(imgUrl:String): Bitmap? {
        val imageBytes = Base64.decode(imgUrl,0)
        var img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return img
    }*/

}

