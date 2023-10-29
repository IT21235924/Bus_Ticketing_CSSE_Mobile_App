package com.example.cssenew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csse_mobile.R

class TopUpRecordAdapter(private val topUpRecords: List<TopUpRecord>) :
    RecyclerView.Adapter<TopUpRecordAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amount: TextView = itemView.findViewById(R.id.textAmount)
        val method: TextView = itemView.findViewById(R.id.textMethod)
        val dateTime: TextView = itemView.findViewById(R.id.textDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_up_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecord = topUpRecords[position]
        holder.amount.text = "Amount: ${currentRecord.topUpAmount}"
        holder.method.text = "Method: ${currentRecord.paymentMethod}"
        holder.dateTime.text = "Date & Time: ${currentRecord.dateTime}"
    }

    override fun getItemCount(): Int {
        return topUpRecords.size
    }
}
