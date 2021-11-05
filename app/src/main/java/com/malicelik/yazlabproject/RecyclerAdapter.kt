package com.malicelik.yazlabproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malicelik.yazlabproject.databinding.RecyclerRowBinding




class RecyclerAdapter(val liste: ArrayList<Pdf>): RecyclerView.Adapter<RecyclerAdapter.ListeViewHolder>(){

    inner class ListeViewHolder(val itemBinding: RecyclerRowBinding)
        :RecyclerView.ViewHolder(itemBinding.root){

            fun bindItem(pdf: Pdf){
                itemBinding.recyclerViewTextView.text= pdf.basvurutipi.toString()
                var durumtxt : String =""
                when(pdf.durum){
                    1->durumtxt="Beklemede"
                    2->durumtxt="Kabul edildi"
                    3->durumtxt="Kabul edilmedi"
                }
                itemBinding.recyclerViewTextView.text= durumtxt
                itemBinding.btnGor.setText("İncele")
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeViewHolder {
        return ListeViewHolder(RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ListeViewHolder, position: Int) {
        val pdf=liste[position]
        holder.bindItem(pdf)
    }

    override fun getItemCount(): Int {
        return liste.size
    }


}