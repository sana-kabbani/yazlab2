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


    //Binding bağlantısı
    lateinit var binding: ActivityBasvuruDevamBinding

    //pdf tipinden bir array list oluşturma.
    private lateinit var pdfArrayList: ArrayList<Pdf>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasvuruDevamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //veri Hazırlığı
        binding




    }


}