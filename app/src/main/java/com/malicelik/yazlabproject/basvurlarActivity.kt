package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityAnaEkranBinding
import com.malicelik.yazlabproject.databinding.ActivityBasvurlarBinding

class basvurlarActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasvurlarBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    var rol = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasvurlarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//Email ve Paraol Authentication kayıt oluyor id'de realtime kayıt etmek için
        databaseReference=database?.reference!!.child("profile")
        var currentUser = auth.currentUser
        var userReference = databaseReference?.child(currentUser?.uid!!)


        binding.bottomNav.setOnNavigationItemReselectedListener {

            if(it.itemId==R.id.ic_home){
                startActivity(Intent(this,AnaEkran::class.java))
                onPause()
            }
            else if(it.itemId==R.id.ic_profile){
                startActivity(Intent(this,MainActivity::class.java))
                onPause()
            }else if(it.itemId==R.id.ic_exit){
                auth.signOut()
                startActivity(Intent(this,GirisActivity::class.java))
                onPause()
            }

        }


        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rol=snapshot.child("rol").value.toString().toInt()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        binding.yazbutton.setOnClickListener{
                when(rol){
                    1-> intent = Intent(applicationContext,YazOkulu::class.java)
                    2-> intent = Intent(applicationContext,YazOkuluList::class.java)
                }

                startActivity(intent)
                onPause()

            }
            binding.yataybutton.setOnClickListener{

                when(rol){
                    1-> intent = Intent(applicationContext,YatayGecic::class.java)
                    2-> intent = Intent(applicationContext,YatayGecicList::class.java)
                }

                startActivity(intent)
                onPause()

            }
            binding.dgsbutton.setOnClickListener{

                when(rol){
                    1-> intent = Intent(applicationContext,Dgs::class.java)
                    2-> intent = Intent(applicationContext,DgsList::class.java)
                }

                startActivity(intent)
                onPause()

            }
            binding.capbutton.setOnClickListener{

                when(rol){
                    1-> intent = Intent(applicationContext,Cap::class.java)
                    2-> intent = Intent(applicationContext,CapList::class.java)
                }

                startActivity(intent)
                onPause()
            }
            binding.dersintibakibutton.setOnClickListener{

                when(rol){
                    1-> intent = Intent(applicationContext,Dersintibaki::class.java)
                    2-> intent = Intent(applicationContext,DersintibakiList::class.java)
                }

                startActivity(intent)
                onPause()
            }
    }
}