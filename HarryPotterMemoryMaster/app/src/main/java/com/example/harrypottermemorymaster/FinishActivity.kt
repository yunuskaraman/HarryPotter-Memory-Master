package com.example.harrypottermemorymaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.*

class FinishActivity : AppCompatActivity() {

    var liste = ArrayList<HashMap<String,String>>()
    var listeAdi = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        val textFinish = findViewById<TextView>(R.id.textView4)
        textFinish.setText("Oyun Bitti")
        var oyuncu = intent.getStringExtra("oyuncu")
        var textPuan = findViewById<TextView>(R.id.textPuan)

        if (oyuncu!!.toInt() == 1){
            var puan = intent.getStringExtra("skor1")
            textPuan.setText("Oyun Skorunuz : ${puan}")
        }else{
            var puan1 = intent.getStringExtra("skor1")
            var puan2 = intent.getStringExtra("skor2")
            textPuan.textSize= 18F
            textPuan.setText("1.Oyuncu Skor : ${puan1}  -  2.Oyuncu Skor : ${puan2}")
        }




        val listView = findViewById<ListView>(R.id.liste)



        val zorluk = intent.getStringExtra("zorluk")!!.toInt()
        if (zorluk != null){
            for (i in 1..zorluk){
                val ev = intent.getStringExtra("ev${i}")
                val resim = intent.getStringExtra("resim${i}")
                val adi = intent.getStringExtra("adi${i}")
                val puan = intent.getStringExtra("puan${i}")
                var map = hashMapOf<String,String>("ev" to "${ev}","adi" to "${adi}", "puan" to "${puan}","resim" to "${resim}")
                listeAdi.add(adi.toString())
                liste.add(map)
            }

        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listeAdi)
        listView.adapter = adapter
        listView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val intent = Intent(applicationContext,DetayActivity::class.java)
                intent.putExtra("ev",liste[p2]["ev"])
                intent.putExtra("adi",liste[p2]["adi"])
                intent.putExtra("puan",liste[p2]["puan"])
                intent.putExtra("resim",liste[p2]["resim"])
                startActivity(intent)
            }
        }



        val btnRestart = findViewById<Button>(R.id.btnRestart)
        btnRestart.setOnClickListener {
            val intent = Intent(this,SecimActivity::class.java)
            startActivity(intent)
        }
    }
}