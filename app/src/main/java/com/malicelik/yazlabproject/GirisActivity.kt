package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.malicelik.yazlabproject.databinding.ActivityGirisBinding

class GirisActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding : ActivityGirisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        //oturum açık mı kontrolü
        var currentUser = auth.currentUser
       if (currentUser!= null){
            startActivity(Intent(this@GirisActivity,MainActivity::class.java))
            finish()
        }
        // giriş yap butona tıklandığında
        binding.UyeGirisButton.setOnClickListener {
            var girisemail = binding.UyeGirisEmail.text.toString()
            var girisparola = binding.UyeGirisPass.text.toString()
            if (TextUtils.isEmpty(girisemail)){
                binding.UyeGirisEmail.error = "Lütfen E-postanızı Giriniz"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(girisemail)){
                binding.UyeGirisPass.error = "Lütfen Parolanızı Giriniz"
                return@setOnClickListener
            }
            // Giriş bilgilerini doğrulama
            auth.signInWithEmailAndPassword(girisemail,girisparola)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        intent = Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(applicationContext,"Giriş Hatalı, Tekrar deneyiniz."
                        ,Toast.LENGTH_LONG).show()
                    }
                }
        }
        //Yeni Üyelik sayfasına yönlendirme
        binding.UyeGirisButtonKayit.setOnClickListener {
            intent = Intent(applicationContext,UyeActivity::class.java)
            startActivity(intent)
            finish()
        }
        //Parolamı unuttum
        binding.UyeGirisUnutPass.setOnClickListener {
            intent = Intent(applicationContext,PsifirlaActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}