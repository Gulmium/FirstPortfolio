package com.example.aluka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aluka.data.AddItemResponse
import com.example.aluka.data.AddStockRequest
import com.example.aluka.data.AddStockResponse
import com.example.aluka.data.AddStoreResponse
import com.example.aluka.data.CheckItemResponse
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.network.RetrofitClient
import retrofit2.Callback
import com.example.aluka.data.Item
import com.example.aluka.data.Stock
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class ItemEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_entry)

        val token = getToken(this)
        val authHeader = "Bearer $token"

        val itemNumber = requireNotNull(intent.getStringExtra("item_number")) {
            "item_number must not be null"
        }

        var itemName: String? = null
        var itemType: String? = null
        val isExist = intent.getBooleanExtra("isExist", false)

        val itemNumberEditText = findViewById<EditText>(R.id.itemNumberEditText)
        val itemNameEditText = findViewById<EditText>(R.id.itemNameEditText)
        val itemQuantityEditText = findViewById<EditText>(R.id.itemQuantityEditText)
        val selectRadio = findViewById<RadioGroup>(R.id.selectRadio)
        val submitButton = findViewById<Button>(R.id.submitButton)

        itemNumberEditText.setText(itemNumber)

        // 商品が存在する場合の画面設定
        if (isExist) {
            itemName = intent.getStringExtra("item_name")
            itemType = intent.getStringExtra("item_type")
            itemNameEditText.setText(itemName)
            itemNumberEditText.isEnabled = false
            itemNameEditText.isEnabled = false
            selectRadio.visibility = View.GONE
        } else {
            itemNumberEditText.isEnabled = false
            itemNameEditText.isEnabled = true
            selectRadio.visibility = View.VISIBLE
        }

        val store = intent.getIntExtra("storeId", -1)

        submitButton.setOnClickListener {
            itemName = itemNameEditText.text.toString()
            val quantity = itemQuantityEditText.text.toString()

            if (isExist) {
                //
            } else {
                val checkedId = selectRadio.checkedRadioButtonId
                if (checkedId == -1) {
                    Toast.makeText(this, "商品タイプを選択してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                itemType = selectedRadioButton.tag?.toString() ?: "unknown"
            }

            val item = Item(itemNumber, itemName!!, itemType)
            val addStockRequest = AddStockRequest(store, item, quantity)
            Log.d("AddStockRequest", Gson().toJson(addStockRequest))

            RetrofitClient.apiService.addStock(authHeader, addStockRequest)
                .enqueue(object : Callback<AddStockResponse> {
                    override fun onResponse(call: Call<AddStockResponse>, response: Response<AddStockResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ItemEntryActivity, "登録成功: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@ItemEntryActivity, StockListActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ItemEntryActivity, "登録失敗: ${response.message()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<AddStockResponse>, t: Throwable) {
                        Log.e("Error", "APIリクエスト失敗: ${t.localizedMessage}")
                        Toast.makeText(this@ItemEntryActivity, "通信エラーが発生しました", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}