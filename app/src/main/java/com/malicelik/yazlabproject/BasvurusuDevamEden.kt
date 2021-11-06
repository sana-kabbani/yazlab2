package com.malicelik.yazlabproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.malicelik.yazlabproject.databinding.ActivityBasvurusuDevamEdenBinding



class BasvurusuDevamEden : AppCompatActivity(),RecyclerAdapter.MyOnclickListener {
    lateinit var binding: ActivityBasvurusuDevamEdenBinding
    private lateinit var dbref:DatabaseReference
    private  lateinit var pdfRecyclerView: RecyclerView
    private var PdfList = ArrayList<Pdf>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasvurusuDevamEdenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        pdfRecyclerView=binding.taskRv//tamam

        pdfRecyclerView.layoutManager=LinearLayoutManager(this)
        pdfRecyclerView.setHasFixedSize(true)//tamam

        PdfList= arrayListOf<Pdf>()

        getPdfData()
        /*val pdf1 = Pdf("Yatay Gecis","1")
        val pdf2 = Pdf("Dikey Gecis","2")
        val pdf3 = Pdf("Ortadan Gecis","1")
        val pdf4 = Pdf("Ortadan Gecis","3")
        val pdf5 = Pdf("Ortadan Gecis","1")*/

        /*PdfList.add(pdf1)
         PdfList.add(pdf2)
         PdfList.add(pdf3)
         PdfList.add(pdf4)
         PdfList.add(pdf5)*/

        val adapter = RecyclerAdapter(PdfList,this@BasvurusuDevamEden,1)
        binding?.taskRv.adapter=adapter
    }

    private fun getPdfData() {
        dbref=FirebaseDatabase.getInstance().getReference("FilePdf")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(pdfSnapshot in snapshot.children){
                        val pdf=pdfSnapshot.getValue(Pdf::class.java)
                        PdfList.add(pdf!!)
                    }
                    pdfRecyclerView.adapter=RecyclerAdapter(PdfList,this@BasvurusuDevamEden,1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun OnClick(position: Int) {

        val urlText=PdfList[position].url
        val webSite = Intent(Intent.ACTION_VIEW)
        webSite.data = Uri.parse(urlText.toString())//EditText'te buluann Url Adresini alıp açma isteğimizi programa yolluyoruz
        startActivity(webSite)

        Toast.makeText(this,PdfList[position].url,Toast.LENGTH_LONG).show()

    }


}


