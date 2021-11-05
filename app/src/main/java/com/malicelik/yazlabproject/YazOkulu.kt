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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityYazOkuluBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class YazOkulu : AppCompatActivity() {
    lateinit var binding: ActivityYazOkuluBinding


    lateinit var dersadi: EditText
    lateinit var derskodu: EditText
    lateinit var derskredi: EditText
    lateinit var dersadi2: EditText
    lateinit var derskodu2: EditText
    lateinit var derskredi2: EditText
    var ogrno: String? = null
    var adisoyadi: String? = null
    var bolum: String? = null
    var fakulte: String? = null
    var sinif: String? = null
    var email: String? = null
    var selected: Int? = null
    var tc: String? = null

    val informationArray = arrayOf(
        "TC : ",
        "Öğrenci No : ",
        "Adı Soyadı : ",
        "Fakülte : ",
        "Bölüm : ",
        "Email : ",
        "Sınıf : "
    )

    lateinit var options: Spinner
    private lateinit var auth: FirebaseAuth
    var rol=0
    var databaseReference: DatabaseReference? = null //database refreanse almak için
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityYazOkuluBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //firebase
        auth = FirebaseAuth.getInstance()//konum belirleme
        database = FirebaseDatabase.getInstance()//
        databaseReference = database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        var userReference = databaseReference?.child(currentUser?.uid!!)
        email = currentUser?.email
        userReference?.addValueEventListener(object : ValueEventListener {

            override public fun onDataChange(snapshot: DataSnapshot) {


                fakulte = snapshot.child("fakulte").value.toString()
                bolum = snapshot.child("bolum").value.toString()

                sinif = snapshot.child("sinif").value.toString()
                adisoyadi = snapshot.child("adisoyadi").value.toString()
                ogrno = snapshot.child("ogrencino").value.toString()
                rol=snapshot.child("rol").value.toString().toInt()
                tc = snapshot.child("Tc").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


//spinner
        options = findViewById(R.id.spinner)
        var f = "Ders Sayısı"
        val fakulte = listOf("1 Ders ", "2 Ders ")
        var selectId = 0
        var date = ""

        val adaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, fakulte)
        options.adapter = adaptor
        options.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectId = options.selectedItemId.toInt()
                if (selectId == 0) {
                    binding.dersadi2.setVisibility(View.INVISIBLE)
                    binding.derskodu2.setVisibility(View.INVISIBLE)
                    binding.derskredi2.setVisibility(View.INVISIBLE)

                } else {
                    binding.dersadi2.setVisibility(View.VISIBLE)
                    binding.derskodu2.setVisibility(View.VISIBLE)
                    binding.derskredi2.setVisibility(View.VISIBLE)
                }
                selected = selectId

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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
    }//oncreate end





        fun generatePDF() {


            dersadi = findViewById(R.id.dersadi)
            derskodu = findViewById(R.id.derskodu)
            derskredi = findViewById(R.id.derskredi)
            dersadi2 = findViewById(R.id.dersadi2)
            derskodu2 = findViewById(R.id.derskodu2)
            derskredi2 = findViewById(R.id.derskredi2)


            if ( dersadi.text.toString().length == 0 || derskodu.text.toString().length == 0 || derskredi.text.toString().length == 0) {
                Toast.makeText(this, "some fields empty", Toast.LENGTH_LONG).show()


            } else {






                val pdfDocument = PdfDocument()

                val myPaint = Paint()
                val title = Paint()

                val mypageInfo = PdfDocument.PageInfo.Builder(250, 430, 1).create()

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
                myPaint.textSize = 6f
                myPaint.color = Color.rgb(122, 119, 119)
                canvas.drawText(
                    "YAZ OKUL BAŞVURU",
                    (mypageInfo.pageWidth / 2).toFloat(),
                    50F,
                    myPaint
                )
                myPaint.textAlign = Paint.Align.LEFT
                myPaint.textSize = 9f
                myPaint.color = Color.rgb(122, 119, 119)
                canvas.drawText("Öğrenci Bilgileri", 10.0F, 70.0F, myPaint)
                //
                myPaint.textAlign = Paint.Align.LEFT
                myPaint.textSize = 8.0f
                myPaint.color = Color.BLACK

                var startXPosition = 10
                var endXPosition = mypageInfo.pageWidth - 10
                var startYPosition = 100
                // print the rows
                for (i in 0..6) {



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
                    informationArray[0] + tc,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )

                startlPosition += 20
                canvas.drawText(
                    informationArray[1] + ogrno,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )
                startlPosition += 20
                canvas.drawText(
                    informationArray[2] + adisoyadi,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )
                startlPosition += 20
                canvas.drawText(
                    informationArray[3] + fakulte,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )
                startlPosition += 20
                canvas.drawText(
                    informationArray[4] + bolum,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )
                startlPosition += 20
                canvas.drawText(
                    informationArray[5] + email,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )
                startlPosition += 20
                canvas.drawText(
                    informationArray[6] + sinif,
                    startXPosition.toFloat(),
                    startlPosition.toFloat(),
                    myPaint
                )



                myPaint.style = Paint.Style.STROKE
                myPaint.strokeWidth = 1f
                canvas.drawRect(10f, 250f, (mypageInfo.pageWidth - 10).toFloat(), 270f, myPaint)
                myPaint.style = Paint.Style.FILL
                myPaint.textAlign = Paint.Align.LEFT
                canvas.drawText("No", 20f, 265f, myPaint)
                canvas.drawText("Ders Kodu", 45f, 265f, myPaint)
                canvas.drawText("Ders Adi", 120f, 265f, myPaint)
                canvas.drawText("Kredi", 190f, 265f, myPaint)



                canvas.drawLine(40F, 250F, 40F, 270F, myPaint)
                canvas.drawLine(118F, 250F, 118F, 270F, myPaint)
                canvas.drawLine(185F, 250F, 185F, 270F, myPaint)
                canvas.drawLine(10F, 295F, (mypageInfo.pageWidth - 10).toFloat(), 295F, myPaint)

                if (selected == 0) {
                    canvas.drawText("1", 25f, 290f, myPaint)
                    canvas.drawText(dersadi.text.toString(), 70f, 290f, myPaint)
                    canvas.drawText(derskodu.text.toString(), 120f, 290f, myPaint)
                    canvas.drawText(derskredi.text.toString(), 190f, 290f, myPaint)
                } else {
                    canvas.drawText("1", 25f, 290f, myPaint)
                    canvas.drawText(dersadi.text.toString(), 70f, 290f, myPaint)
                    canvas.drawText(derskodu.text.toString(), 120f, 290f, myPaint)
                    canvas.drawText(derskredi.text.toString(), 170f, 290f, myPaint)
                    canvas.drawLine(10F, 325F, (mypageInfo.pageWidth - 10).toFloat(), 325F, myPaint)
                    canvas.drawText("2", 25f, 320f, myPaint)
                    canvas.drawText(dersadi2.text.toString(), 70f, 320f, myPaint)
                    canvas.drawText(derskodu2.text.toString(), 120f, 320f, myPaint)
                    canvas.drawText(derskredi2.text.toString(), 170f, 320f, myPaint)

                }
                myPaint.textSize= 9f
                canvas.drawText("Öğrenci Imza ", 170f, 360f, myPaint)



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


        }



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

