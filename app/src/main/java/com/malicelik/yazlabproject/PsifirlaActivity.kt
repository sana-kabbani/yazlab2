package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.malicelik.yazlabproject.databinding.ActivityPsifirlaBinding

class PsifirlaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPsifirlaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPsifirlaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()//konum belirleme

        binding.pSifirlaBtn.setOnClickListener {
            var psifirlaemail = binding.pSifirlaEmail.text.toString().trim()
            if (TextUtils.isEmpty(psifirlaemail)){
                binding.pSifirlaEmail.error = "Lütfen E-posta adresinizi yazınız."
            } else{
                auth.sendPasswordResetEmail(psifirlaemail)
                    .addOnCompleteListener(this){sifirlama ->
                        if (sifirlama.isSuccessful){
                            binding.pSifirlaMsj.text="E-mail adresinize sıfırlama bağlantısı gönderildi, Lütfen kontrol ediniz. "
                        } else{
                            binding.pSifirlaMsj.text="Sıfırlama işlemi başarısız."


                        }

                    }
            }
        }
        //Giriş Sayfasına yönlendirme
        binding.pSifirlaGiris.setOnClickListener {
            intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}