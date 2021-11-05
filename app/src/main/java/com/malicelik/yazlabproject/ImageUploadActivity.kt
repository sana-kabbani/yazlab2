package com.malicelik.yazlabproject

import android.app.Application
import android.app.Instrumentation
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.malicelik.yazlabproject.databinding.ActivityImageUploadBinding
import com.malicelik.yazlabproject.databinding.ActivityPdfUploadBinding

class ImageUploadActivity : AppCompatActivity() {
    //Activty bağlayıcı
    private lateinit var binding:ActivityImageUploadBinding

    //firebase yetkilendirme
    private  lateinit var firebaseAuth: FirebaseAuth

    //ilerleme iletişim kutusu (pdf yüklerken göster)
    private  lateinit var progressDialog: ProgressDialog

    //pdf kategorilerini tutmak için arrayList
    //private lateinit var categoryArrayList: ArrayList<ModelCategory>

    private  var pdfUri: Uri?= null

    //Tag
    private val TAG ="PDF_ADD_TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //init firebase auth
        firebaseAuth =  FirebaseAuth.getInstance()



        //progress dialog kurulumu
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Lütfen Bekleyin")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, goback


        //pdf secme durumu
        binding.btnSec.setOnClickListener {
            pdfPicIntent()
        }

        //pdf yukleme durumu
        binding.btnYukle.setOnClickListener {

            //1. Verileri Doğrula
            //2. pdf dosyasını firebase deposuna yükle
            //3. Yüklenen pdf'nin url'sini al
            //4. Firebase db'ye pdf bilgisi yükleyin
            validateData()
        }


    }
    private var pdfdurum = 1
    private var pdfbaslik="deneme"

    private fun validateData() {
        //1. Verileri Doğrula
        uploadPdfStorage()
        if(pdfUri==null){
            Toast.makeText(this,"Pdf Seçmediniz...",Toast.LENGTH_LONG).show()
        }

    }

    private fun uploadPdfStorage() {
        //2. pdf dosyasını firebase deposuna yükle
        Log.d(TAG,"loadPdfToStorage: uploading to storage...")

        //show progress dialog
        progressDialog.setMessage("Uploading PDF...")
        progressDialog.show()

        //timestamp
        val timestamp = System.currentTimeMillis()

        // path of pdf in firebase storage
        val filePathAndName = "Image/$timestamp"

        //storage referance
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG,"uploadPdfToStorage: PDF uploaded now getting url...")
                //3. Yüklenen pdf'nin url'sini al
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPdfUrl = "${uriTask.result}"

                uploadPdfInfoDb(uploadedPdfUrl,timestamp)



            }
            .addOnFailureListener { e->
                Log.d(TAG,"uploadPdfToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to upload due to ${e.message}",Toast.LENGTH_LONG).show()

            }
    }

    private fun uploadPdfInfoDb(uploadedPdfUrl: String, timestamp: Long) {
        //4. Firebase db'ye pdf bilgisi yükleyin
        Log.d(TAG,"uploadPdfInfoToDb: uploading to db")
        progressDialog.setMessage("Uploading pdf info..")
        //uid
        val uid=firebaseAuth.uid

        //setup data to upload
        val hashMap: HashMap <String,Any> = HashMap()
        hashMap["uid"]="$uid"
        hashMap["id"]="$timestamp"
        hashMap["durum"]= pdfdurum
        hashMap["url"]="$uploadedPdfUrl"
        hashMap["timestamp"]= timestamp

        //db reference DB >
        val ref = FirebaseDatabase.getInstance().getReference("image")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG,"uploadPdfInfoToDb:  uploadED TO DB ")
                progressDialog.dismiss()
                Toast.makeText(this,"Uploded...",Toast.LENGTH_LONG).show()
                pdfUri=null




            }
            .addOnFailureListener{ e->

                Log.d(TAG,"uploadPdfInfoToDb: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to upload due to ${e.message}",Toast.LENGTH_LONG).show()

            }




    }

    private fun pdfPicIntent(){

        Log.d(TAG,"pdfPickIntent: starting pdf pick intent")

        val intent = Intent()
        intent.type= "img/*"
        intent.action=Intent.ACTION_GET_CONTENT
        pdfActivityRestultLauncher.launch(intent)

    }

    val pdfActivityRestultLauncher= registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{ result ->
            if(result.resultCode== RESULT_OK){
                Log.d(TAG,"PDF SEÇİNİZ")
                pdfUri=result.data!!.data
            }
            else{
                Log.d(TAG,"PDF Pick cancelled")
                Toast.makeText(this,"Cancelled", Toast.LENGTH_LONG).show()
            }
        }


    )

}