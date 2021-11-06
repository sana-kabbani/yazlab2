package com.malicelik.yazlabproject


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat.startActivity


class RecyclerAdapter(private val pdfList: ArrayList<Pdf>,val listener:MyOnclickListener,val page:Int,val basvurutipi:String="") :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val basvurutipi:TextView = view.findViewById(R.id.basvurutipi)
        val durum:TextView = view.findViewById(R.id.durum)

        init {
            itemView.setOnClickListener{
                val position= adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    listener.OnClick(position)
                }

            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_row, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if(page==1){
            if(pdfList[position].durum.toString().toInt()==1){
                viewHolder.basvurutipi.text=pdfList[position].basvurutipi
                viewHolder.durum.text="Kontrol Ediliyor"
            }else{
                viewHolder.basvurutipi.setVisibility(View.GONE)
                viewHolder.durum.setVisibility(View.GONE)
            }
        }else if(page==2){
            if(pdfList[position].durum.toString().toInt()!=1){
                var durumtext=""
                viewHolder.basvurutipi.text=pdfList[position].basvurutipi
                when(pdfList[position].durum.toString().toInt()){
                    2->durumtext="Onaylandı"
                    3->durumtext="Rededildi"
                }
                viewHolder.durum.text=durumtext

            }else{
                viewHolder.basvurutipi.setVisibility(View.GONE)
                viewHolder.durum.setVisibility(View.GONE)
            }

        }
        else if(page==3){





        }
        else if(page==4){


        }


    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = pdfList.size

    interface MyOnclickListener{
        fun OnClick(position: Int)
    }



}