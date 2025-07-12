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
import com.example.aluka.data.Store
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerActivity : AppCompatActivity() {

    private var storeList: List<Store> = emptyList()  // 安全な初期化

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val addStoreButton = findViewById<Button>(R.id.addStoreButton)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val storeListView = findViewById<ListView>(R.id.storeListView)

        val token = getToken(this)
        val authHeader = "Bearer $token"

        addStoreButton.setOnClickListener {
            val intent = Intent(this, AddStoreActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        if (!token.isNullOrEmpty()) {
            RetrofitClient.apiService.getStoreDetails(authHeader).enqueue(object :
                Callback<List<Store>> {
                override fun onResponse(
                    call: Call<List<Store>>,
                    response: Response<List<Store>>
                ) {
                    if (response.isSuccessful) {
                        storeList = response.body() ?: emptyList()
                        val storeNames = storeList.map { it.storename }
                        val adapter = ArrayAdapter(
                            this@SellerActivity,
                            android.R.layout.simple_list_item_1,
                            storeNames
                        )
                        storeListView.adapter = adapter

                        storeListView.setOnItemClickListener { _, _, position, _ ->
                            val selectedStore = storeList[position]
                            val intent = Intent(this@SellerActivity, StockListActivity::class.java)
                            intent.putExtra("store_id", selectedStore.id)
                            startActivity(intent)
                        }
                    } else {
                        Log.e("API", "レスポンスエラー: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                    Log.e("API", "APIリクエスト失敗: ${t.localizedMessage}")
                    t.printStackTrace()
                }
            })
        } else {
            val intent = Intent(this@SellerActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Log.e("認証", "トークンが無効または存在しません")
        }
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