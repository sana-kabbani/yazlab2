package com.malicelik.yazlabproject

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.malicelik.yazlabproject.databinding.ActivityUyeBinding
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class UyeActivity : AppCompatActivity() {

    lateinit var binding: ActivityUyeBinding

    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null //database refreanse almak için
    var database: FirebaseDatabase?=null
    //Realtime ve autantication bölümü için gerekli
    //Spinner
    lateinit var options: Spinner
    lateinit var options2: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityUyeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//Email ve Paraol Authentication kayıt oluyor id'de realtime kayıt etmek için
        databaseReference=database?.reference!!.child("profile")







        options=findViewById(R.id.spinner)
        var f="Fakültesi"
        val fakulte= listOf("Teknoloji ${f}","Mühendislik ${f}","Eğitim ${f}","Spor Bilimleri ${f}","İletişim ${f}")
        var selectId=-1
        var date = ""

        val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,fakulte)
        options.adapter=adaptor
        options.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectId=options.selectedItemId.toInt()
                sec(selectId)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            }

        //Kaydet Butonu ile kaydetme adımları
        binding.btnDate.setOnClickListener {
            val locale = Locale("TR")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            applicationContext.resources.updateConfiguration(config, null)

            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.show(supportFragmentManager,"DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DatePicker",datePicker.headerText)
                date=datePicker.headerText.toString()
            }
            datePicker.addOnNegativeButtonClickListener{
              Log.d("DatePicker",datePicker.headerText)

            }
            datePicker.addOnCancelListener{
                Log.d("DatePicker","İptal Edildi")

            }
        }


        binding.UyeButtonKayit.setOnClickListener {
            var UyeAdsoyad = binding.UyeAdSoyad.text.toString()
            var UyeEmail = binding.UyeEmail.text.toString()
            var UyeSifre = binding.UyePass.text.toString()
            var UyeOgrNo = binding.UyeOgrNo.text.toString().toIntOrNull()
            var UyeAdres = binding.UyeAdres.text.toString()
            var UyeSinif = binding.UyeSinif.text.toString().toIntOrNull()


            if(TextUtils.isEmpty(UyeEmail)){
                binding.UyeEmail.error = "Lütfen mail adresinizi girin!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            else if(UyeEmail.contains('@')==false){
                binding.UyeEmail.error = "Lütfen mail adresinizi doğru girin!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            else if(UyeEmail.contains('.')==false){
                binding.UyeEmail.error = "Lütfen mail adresinizi doğru girin!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            if(UyeOgrNo==null){
                binding.UyeOgrNo.error = "Öğrenci numaranızı girmeyi unutmayın!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.

            }
            else{
                if(UyeOgrNo.toString().length!=9){
                    binding.UyeOgrNo.error = "Öğrenci Numaranız 9 karakter olmalı!"
                    return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
                }
            }
            if(TextUtils.isEmpty(UyeAdsoyad)){
                binding.UyeAdSoyad.error = "Lütfen adınızı ve soyadınızı giriniz."
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
             if(TextUtils.isEmpty(UyeSifre)){
                binding.UyePass.error = "Şifre girmeyi unutmayın!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            else if(UyeSifre.length<6){
                binding.UyePass.error = "Şifre 6 Karakterden küçük olamaz!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            if(TextUtils.isEmpty(UyeAdres)){
                binding.UyeAdres.error = "Adresinizi girmeyi unutmayın!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
            }
            if(UyeSinif==null){
                binding.UyeSinif.error = "Sınıf Boş Bırakılamaz!"
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.

            }
            else {

                if(UyeSinif!!.toInt()>4){
                    binding.UyeSinif.error = "Lisans Öğrencisi en fazla 4. yılında olabilir."
                    return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
                }
                else if(UyeSinif!!.toInt()<1){
                    binding.UyeSinif.error = "Lisans Öğrencisi en az 1. yılında olabilir."
                    return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.
                }

            }

             if(date==""){

                 Toast.makeText(this@UyeActivity,"Tarih Seçiniz!",Toast.LENGTH_LONG).show()
                Log.e("tarih",date)
                return@setOnClickListener //Diğerlerini de tek tek kontrol etmek için return kullanıyoruz.

            }

            //Email, paralo ve kullanıcı bilgilerini veritabanına ekleme

            auth.createUserWithEmailAndPassword(binding.UyeEmail.text.toString(),binding.UyePass.text.toString())
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        //Şuanki kullanıcı bilgilerini alıcağız ve id kullancağız.
                        var currentUser = auth.currentUser
                        //Id göre kayıt işlemi
                        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                        currentUserDb?.child("adisoyadi")?.setValue(binding.UyeAdSoyad.text.toString())
                        currentUserDb?.child("ogrencino")?.setValue(binding.UyeOgrNo.text.toString())
                        currentUserDb?.child("sinif")?.setValue(binding.UyeSinif.text.toString().toInt())

                        currentUserDb?.child("tarih")?.setValue(date)
                        currentUserDb?.child("adress")?.setValue(binding.UyeAdres.text.toString())
                        currentUserDb?.child("fakulte")?.setValue(binding.spinner.selectedItem.toString())
                        currentUserDb?.child("bolum")?.setValue(binding.spinner2.selectedItem.toString())

                        currentUserDb?.child("rol")?.setValue(1)
                        Toast.makeText(this@UyeActivity,"Kayıt Basarılı",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@UyeActivity,MainActivity::class.java))
                        finish()

                    }else{
                        Toast.makeText(this@UyeActivity,"Kayıt Hatalı! aynı e-posta ile kayıt olamazsınız!",Toast.LENGTH_LONG).show()
                    }

                }

        }


        binding.UyeBtnPicture.setOnClickListener {
           // startFileChooser()
        }
        //Giriş sayfasına yönlendirme
        binding.uyeGirisBtn.setOnClickListener {
           // uploadImage()
            intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun sec(selectId:Int){
        options2=findViewById(R.id.spinner2)

        var b="Bölümü"
        val teknoloji= listOf(
            "Bilişim Sistemleri Mühendisliği ${b}",
            "Otomativ Mühendisliği ${b}",
            "Enerji Sistemleri Mühendisliği ${b}",
            "Biyomedikal Mühendisliği ${b}",
            "Bilişim ${b}"
        )

        val muhendislik= listOf(
            "Bilgisyar Mühendisliği ${b}",
            "Yazılım Mühendisliği ${b}",
            "Elektirik Sistemleri Mühendisliği ${b}",
            "Makine Mühendisliği ${b}",
            "Mekatronik Mühendisliği ${b}"
        )

        val egitim= listOf(
            "Fizik ${b}",
            "Kimya ${b}",
            "Okul Öncesi ${b}",
            "Biyoloji ${b}",
            "Piskoloji ${b}"
        )
        val spor= listOf(
            "Antrenörlük Eğitimi ${b}",
            "Spor Öğretmenliği ${b}",
            "Spor Yöneticiliği ${b}",
            "Rekreasyon ${b}",
            "Beden Eğitimi Öğretmenliği ${b}"
        )
        val iletisim= listOf(
            "Radyo Televizyon ve Sinema ${b}",
            "Gazetecilik ${b}",
            "Halkla İlişkiler ve Tanıtım ${b}",
            "Reklamcılık ${b}",
            "Görsel İletişim Tasarımı ${b}"
        )

        if(selectId==0){
            val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,teknoloji)
            options2.adapter=adaptor
        }else if(selectId==1)
        {
            val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,muhendislik)
            options2.adapter=adaptor
        }else if(selectId==2){
            val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,egitim)
            options2.adapter=adaptor
        }else if(selectId==3){
            val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,spor)
            options2.adapter=adaptor
        }else if(selectId==4){
            val adaptor=ArrayAdapter(this,android.R.layout.simple_spinner_item,iletisim)
            options2.adapter=adaptor
        }
        else if(selectId==-1){
            Toast.makeText(this@UyeActivity,"Kayıt Basarılı",Toast.LENGTH_LONG).show()
        }
    }

   /* private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now= Date()
        val filename= formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(ImageUri).
                addOnSuccessListener {

                    binding.imageView.setImageURI(null)
                    Toast.makeText(this@UyeActivity,"Resim Yükleme Başarılı",Toast.LENGTH_LONG).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()

                }.addOnFailureListener {
                    if(progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this@UyeActivity,"Resim Yükeleme Başarısız",Toast.LENGTH_LONG).show()

        }

    }*/

    //private fun startFileChooser() {


      //  var intent = Intent()
       // intent.type= "image/*"
       // intent.action=Intent.ACTION_GET_CONTENT

        //startActivityForResult(intent,100)
    //}

    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        //if(requestCode==100 && resultCode== Activity.RESULT_OK && data != null && data.data !=null) {
          //  ImageUri =data?.data!!
        //    binding.imageView.setImageURI(ImageUri)
            //var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,ImageUri)

      //  }
    //}
}