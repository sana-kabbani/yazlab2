package com.malicelik.yazlabproject

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private  val mStrorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mPrograssDialog: ProgressDialog
    private val TAG ="FirebaseStrageManger"
    fun uploadImage(mContext: Context, imageURI: Uri){
            mPrograssDialog = ProgressDialog(mContext)
            mPrograssDialog.setMessage("Lütfen Bekleyin..")
            val uploadTask = mStrorageRef.child("users/progilePic.png").putFile(imageURI)

            uploadTask.addOnSuccessListener {
                Log.e(TAG,"Yükleme Başarılı")
            }.addOnFailureListener {
                Log.e(TAG,"Yükleme başarısız ${it.printStackTrace()}")
            }
    }
}

