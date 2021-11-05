package com.malicelik.yazlabproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityDgsBinding
import com.malicelik.yazlabproject.databinding.ActivityYatayGecicBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Dgs : AppCompatActivity() {
    lateinit var binding: ActivityDgsBinding
    lateinit var tc: EditText

    lateinit var uyruk: EditText
    lateinit var tel: EditText
    lateinit var program: EditText
    lateinit var deplomaNot: EditText
    lateinit var univadi: EditText

    lateinit var mezunyil: EditText
    lateinit var sinav: EditText

    var ogrno: String? = null
    var adisoyadi: String? = null
    var email: String? = null
    var dogtarihi: String? = null

    val informationArray = arrayOf(
        "TC : ",
        "Adı Soyadı : ",
        "Doğum Trihi : ",
        "Uyruğu : ",
        "Email : ",
        "Gsm Tel :"
    )
    val euniArray = arrayOf(
        "Üniversite Adi : ",
        "Programı : ",
        "Mezun Yılı : ",
        "Deploma Notu",

        )




    private lateinit var auth: FirebaseAuth

    var databaseReference: DatabaseReference? = null //database refreanse almak için
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDgsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //firebase
        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//
        databaseReference = database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        var userReference = databaseReference?.child(currentUser?.uid!!)
        email = currentUser?.email
        userReference?.addValueEventListener(object : ValueEventListener {

            override  fun onDataChange(snapshot: DataSnapshot) {



                adisoyadi = snapshot.child("adisoyadi").value.toString()
                dogtarihi = snapshot.child("tarih").value.toString()
                ogrno = snapshot.child("ogrencino").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


//spinner

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
        binding.btnPdf.setOnClickListener(View.OnClickListener { // calling method to

            generatePDF()
        })

        binding.btnPdfUpload.setOnClickListener {
            val namedata= binding.title.text.toString()
            val intent = Intent(this,PdfUploadActivity::class.java)
            intent.putExtra("name",namedata)
            startActivity(intent)
            onPause()
        }
    }
    fun generatePDF() {
        tc = findViewById(R.id.dgsTc)
        program = findViewById(R.id.program)



        uyruk = findViewById(R.id.dgsuyuruk)
        tel = findViewById(R.id.dgsphone)
        mezunyil = findViewById(R.id.meztarihi)

        deplomaNot = findViewById(R.id.dgsnot)
        sinav= findViewById(R.id.sinav)
       univadi= findViewById(R.id.unadi)





        //    if (tc.text.toString().length == 0 || uyruk.text.toString().length == 0 || tel.text.toString().length == 0 || euniv.text.toString().length == 0) {
        // Toast.makeText(this, "some fields empty", Toast.LENGTH_LONG).show()


        //  } else {


        val pdfDocument = PdfDocument()

        val myPaint = Paint()
        val title = Paint()

        val mypageInfo = PdfDocument.PageInfo.Builder(250, 550, 1).create()

        val myPage = pdfDocument.startPage(mypageInfo)
        val canvas = myPage.canvas



        //removing weird spaces
        myPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));



        myPaint.textAlign = Paint.Align.CENTER
        myPaint.textSize = 12f
        canvas.drawText(
            "KOCAELI UNIVERSITESI",
            (mypageInfo.pageWidth / 2).toFloat(),
            30F,
            myPaint
        )
        myPaint.textSize = 8f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText(
            "DGS BAŞVURUSU",
            (mypageInfo.pageWidth / 2).toFloat(),
            50F,
            myPaint
        )
        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 9f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("Kişisel Bilgileri", 10.0F, 70.0F, myPaint)
        //
        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 8.0f
        myPaint.color = Color.BLACK

        var startXPosition = 10
        var endXPosition = mypageInfo.pageWidth - 10
        var startYPosition = 100
        // print the rows
        for (i in 0..5) {



            canvas.drawLine(
                startXPosition.toFloat(),
                startYPosition + 3.toFloat(),
                endXPosition.toFloat(),
                startYPosition + 3.toFloat(),
                myPaint
            )
            startYPosition += 20

        }
        var startlPosition = 100




        canvas.drawText(
            informationArray[0] + tc.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )

        startlPosition += 20
        canvas.drawText(
            informationArray[1] + adisoyadi,
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            informationArray[2] + dogtarihi,
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )


        startlPosition += 20
        canvas.drawText(
            informationArray[3] + uyruk.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            informationArray[4] + email,
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            informationArray[5] + tel.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )




        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 9f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("Eğitim Bilgileri", 10.0F, 220.0F, myPaint)


        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 8.0f
        myPaint.color = Color.BLACK
        startYPosition=240
        for (i in 0..3) {



            canvas.drawLine(
                startXPosition.toFloat(),
                startYPosition + 3.toFloat(),
                endXPosition.toFloat(),
                startYPosition + 3.toFloat(),
                myPaint
            )
            startYPosition += 20

        }
        startlPosition = 240
        canvas.drawText(
            euniArray[0] + univadi.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            euniArray[1] + program.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            euniArray[2] + mezunyil.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )
        startlPosition += 20
        canvas.drawText(
            euniArray[3] + deplomaNot.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )


        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 9f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("Sınav Merkez Tercihi", 10.0F, 330.0F, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 8.0f
        myPaint.color = Color.BLACK
        startYPosition=350
        canvas.drawLine(
            startXPosition.toFloat(),
            startYPosition + 3.toFloat(),
            endXPosition.toFloat(),
            startYPosition + 3.toFloat(),
            myPaint
        )
        startlPosition = 350
        canvas.drawText(
            "Merkez Adı : " + sinav.text.toString(),
            startXPosition.toFloat(),
            startlPosition.toFloat(),
            myPaint
        )




        myPaint.textSize= 9f
        canvas.drawText("Öğrenci Imza ", 170f, 460f, myPaint)



        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file = File(Environment.getExternalStorageDirectory(), "$ogrno-$adisoyadi.pdf")
        try {

            pdfDocument.writeTo(FileOutputStream(file))


            Toast.makeText(
                this,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
        }

        pdfDocument.close()
    }

// }


    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val permission2 =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    companion object {
        // constant code for runtime permissions
        private const val PERMISSION_REQUEST_CODE = 200
    }

}
