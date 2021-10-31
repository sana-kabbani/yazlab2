package com.malicelik.yazlabproject

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.malicelik.yazlabproject.databinding.ActivityImageUploadBinding
import com.malicelik.yazlabproject.databinding.ActivityMainBinding
import android.graphics.Bitmap

import android.R.attr.data
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ImageUploadActivity : AppCompatActivity() {


    lateinit var binding: ActivityImageUploadBinding
    lateinit var ImageUri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSec.setOnClickListener {
            selectImage()
        }

        binding.btnYukle.setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(ImageUri)
            .addOnSuccessListener {
                binding.ImagePicture.setImageURI(null)
                Toast.makeText(this@ImageUploadActivity,"Başarılı",
                Toast.LENGTH_LONG).show()
                if (progressDialog.isShowing) progressDialog.dismiss()

            }.addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@ImageUploadActivity,"Başarısız...",Toast.LENGTH_LONG).show()



            }

    }

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),101)
        } else{
            val intent = Intent()
            intent.type="images/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,100)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.ImagePicture.setImageURI(ImageUri)
        }
    }

}