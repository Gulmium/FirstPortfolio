package com.example.aluka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aluka.data.Stock

class StockAdapter(private val stockList: List<Stock>) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockNameTextView: TextView = itemView.findViewById(R.id.stockNameTextView)
        // 他のビュー（例：在庫数など）もここで取得できます
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock, parent, false)  // ← 1行分のレイアウトファイルが必要
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.stockNameTextView.text = stock.item.item_name
    }

    override fun getItemCount(): Int = stockList.size
}