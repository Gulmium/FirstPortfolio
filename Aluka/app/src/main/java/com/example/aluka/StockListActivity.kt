package com.example.aluka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aluka.data.Stock
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class StockListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storeId = intent.getIntExtra("store_id", -1)
        val token = getToken(this)
        val authHeader = "Bearer $token"
        val addItemButton = findViewById<Button>(R.id.addItemButton)

        addItemButton.setOnClickListener{
            val intent = Intent(this@StockListActivity, BarcodeReaderActivity::class.java)
            intent.putExtra("storeId",storeId)
            startActivity(intent)
        }



        if (storeId != -1 && !token.isNullOrEmpty()) {
            fetchStocksForStore(storeId, authHeader)
        } else {
            Log.e("StockList", "storeId または token が無効です")
        }
    }

    private fun fetchStocksForStore(storeId: Int, authHeader: String) {

        var stockList: List<Stock> = emptyList()
        val stockListView = findViewById<ListView>(R.id.stockListView)

        RetrofitClient.apiService.getStocks(storeId, authHeader).enqueue(object :
            Callback<List<Stock>> {
            override fun onResponse(call: Call<List<Stock>>, response: Response<List<Stock>>) {
                if (response.isSuccessful) {
                    stockList = response.body() ?: emptyList()
                    val displayList = stockList.map {
                        String.format(Locale.getDefault(), "%-20s %3d個", it.item.item_name, it.quantity)
                    }


                    val adapter = ArrayAdapter(
                        this@StockListActivity,
                        android.R.layout.simple_list_item_1,
                        displayList
                    )

                    stockListView.adapter = adapter

                    if (stockList.isEmpty()) {
                        Log.i("StockList", "在庫はありません")
                        // 空リスト用のUI表示もできるとベター
                    }
                } else {
                    Log.e("StockList", "レスポンス失敗: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Stock>>, t: Throwable) {
                Log.e("StockList", "在庫取得エラー: ${t.localizedMessage}")
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}