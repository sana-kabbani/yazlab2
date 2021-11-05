package com.malicelik.yazlabproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityAnaEkranBinding
import com.malicelik.yazlabproject.databinding.ActivityBasvuruDevamBinding
import java.lang.StringBuilder


class basvuruDevam : AppCompatActivity() {

    private lateinit var dbReferance: DatabaseReference
    private lateinit var pdfArrayList: ArrayList<Pdf>

    lateinit var binding: ActivityBasvuruDevamBinding
    var TaskList = ArrayList<Task>()

    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    var rol = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasvuruDevamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //veri Hazırlığı


        //binding.recylerView.layoutManager=LinearLayoutManager(this)
        //binding.recylerView.setHasFixedSize(true)
        //binding.recylerView.adapter=adapter

        pdfArrayList = arrayListOf<Pdf>()


        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//
        databaseReference=database?.reference!!.child("profile")
        var currentUser = auth.currentUser

        //realtime - database'deki id ye ulaşıp altındaki childların içindeki veriyi sayfaya aktarıyoruz.
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })












    }


}