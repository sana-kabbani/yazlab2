package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.malicelik.yazlabproject.databinding.ActivityAnaEkranBinding
import com.malicelik.yazlabproject.databinding.ActivityBasvurlarBinding

class basvurlarActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasvurlarBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasvurlarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.setOnNavigationItemReselectedListener {

            if(it.itemId==R.id.ic_home){
                startActivity(Intent(this,AnaEkran::class.java))
                finish()
            }
            else if(it.itemId==R.id.ic_profile){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else if(it.itemId==R.id.ic_exit){
                auth.signOut()
                startActivity(Intent(this,GirisActivity::class.java))
                finish()
            }




        }

        binding.yazbutton.setOnClickListener{
            intent = Intent(applicationContext,YazOkulu::class.java)
            startActivity(intent)
            finish()

        }
        binding.yataybutton.setOnClickListener{
            intent = Intent(applicationContext,YatayGecic::class.java)
            startActivity(intent)
            finish()

        }
        binding.dgsbutton.setOnClickListener{
            intent = Intent(applicationContext,Dgs::class.java)
            startActivity(intent)
            finish()

        }
        binding.capbutton.setOnClickListener{
            intent = Intent(applicationContext,Cap::class.java)
            startActivity(intent)
            finish()

        }
        binding.dersintibakibutton.setOnClickListener{
            intent = Intent(applicationContext,Dersintibaki::class.java)
            startActivity(intent)
            finish()

        }

    }
}