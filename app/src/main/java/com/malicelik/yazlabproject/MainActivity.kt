package com.malicelik.yazlabproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    var rol = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//
        databaseReference=database?.reference!!.child("profile")
        var currentUser = auth.currentUser
        binding.UyeProfilEmail.text = "Email: "+currentUser?.email
        //realtime - database'deki id ye ulaşıp altındaki childların içindeki veriyi sayfaya aktarıyoruz.
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.uyeProfilAdSoyad.text = "Adınız: "+ snapshot.child("adisoyadi").value.toString()
                rol =  snapshot.child("rol").value.toString().toInt()
                if(rol==2){
                    binding.uyeProfilFakulte.setVisibility(View.INVISIBLE)
                    binding.uyeProfilDogum.setVisibility(View.INVISIBLE)
                    binding.uyeProfilSinif.setVisibility(View.INVISIBLE)
                    binding.uyeProfilBolum.setVisibility(View.INVISIBLE)
                    binding.uyeProfilNumara.setVisibility(View.INVISIBLE)
                }else{
                    binding.uyeProfilBolum.text = snapshot.child("bolum").value.toString()
                    binding.uyeProfilNumara.text = "Öğrenci Numaranız: "+ snapshot.child("ogrencino").value.toString()
                    binding.uyeProfilSinif.text = "Sınıfınız: "+snapshot.child("sinif").value.toString()
                    binding.uyeProfilDogum.text = "Dogum Tarihiniz: "+snapshot.child("tarih").value.toString()
                    binding.uyeProfilFakulte.text = snapshot.child("fakulte").value.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





        binding.bottomNav.setOnNavigationItemReselectedListener {

            if(it.itemId==R.id.ic_home){
                startActivity(Intent(this@MainActivity,AnaEkran::class.java))
                finish()
            }
            else if(it.itemId==R.id.ic_delete){
                currentUser?.delete()?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"Hesabınız Silindi",
                            Toast.LENGTH_LONG).show()
                        auth.signOut()
                        startActivity(Intent(this@MainActivity,GirisActivity::class.java))
                        finish()
                        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                        currentUserDb?.removeValue()

                    }
                }

            }
            else if(it.itemId==R.id.ic_exit){

                auth.signOut()
                startActivity(Intent(this@MainActivity,GirisActivity::class.java))
                finish()
            }


        }





    }
}