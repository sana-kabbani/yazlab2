package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malicelik.yazlabproject.databinding.ActivityAnaEkranBinding
import com.malicelik.yazlabproject.databinding.ActivityFormlarBinding


class formlarActivity : AppCompatActivity() {
    lateinit var binding: ActivityFormlarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormlarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.basvuruBtnDevam.setOnClickListener {
            intent = Intent(applicationContext,BasvurusuDevamEden::class.java)
            startActivity(intent)
            onPause()

        }

        binding.basvuruBtnSonuc.setOnClickListener{
            intent = Intent(applicationContext,BasvuruBitenActivity::class.java)
            startActivity(intent)
            onPause()

        }
    }
}