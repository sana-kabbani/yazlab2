package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityAnaEkranBinding



class AnaEkran : AppCompatActivity() {

    lateinit var binding: ActivityAnaEkranBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    var rol=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnaEkranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//Email ve Paraol Authentication kayıt oluyor id'de realtime kayıt etmek için
        databaseReference=database?.reference!!.child("profile")

        // var rol=0
        var currentUser = auth.currentUser
        //rdb'de bulunan kullanıcı idsine erişme
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Rol Aldık
                //rol=snapshot.child("rol").value.toString().toInt()
                rol=snapshot.child("rol").value.toString().toInt()
                if(rol==2){
                    binding.formBtn.text="Raporlar"
                    binding.formBtn.setOnClickListener {
                        intent = Intent(applicationContext,raporlarActivity::class.java)
                        startActivity(intent)
                        onPause()
                    }
                }else{
                    binding.formBtn.text="Gönderilen Başvurular"
                    binding.formBtn.setOnClickListener {
                        intent = Intent(applicationContext,formlarActivity::class.java)
                        startActivity(intent)
                        onPause()
                    }




                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.basvuruBtn.setOnClickListener{
            intent = Intent(applicationContext,basvurlarActivity::class.java)
            startActivity(intent)
            onPause()

        }

        binding.bottomNav.setOnNavigationItemReselectedListener {

            if(it.itemId==R.id.ic_home){
                startActivity(Intent(this@AnaEkran,AnaEkran::class.java))
                onPause()
            }
            else if(it.itemId==R.id.ic_profile){
                startActivity(Intent(this@AnaEkran,MainActivity::class.java))
                onPause()
            }else if(it.itemId==R.id.ic_exit){
                auth.signOut()
                startActivity(Intent(this@AnaEkran,GirisActivity::class.java))
                onPause()
            }



        }


    }
}