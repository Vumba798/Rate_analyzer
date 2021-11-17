package com.example.rateanalyzer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rateanalyzer.rateanalyzer.Rate

class RatesRecyclerAdapter(private val growing: List<Rate>) :
    RecyclerView.Adapter<RatesRecyclerAdapter.RatesViewHolder>() {

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var rate: TextView? = null

        init {
            name = itemView.findViewById(R.id.currencyName)
            rate = itemView.findViewById(R.id.rate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return RatesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.name?.text = growing[position].charCode
        holder.rate?.text =
            "Current rate: ${growing[position].cost}\nEnhancement in month: ${growing[position].enhancement}"
    }

    override fun getItemCount() = growing.size
}