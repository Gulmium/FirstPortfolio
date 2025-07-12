package com.example.aluka

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.data.User
import com.example.aluka.network.RetrofitClient
import com.example.aluka.data.NearbyStoreSearchRequest
import com.example.aluka.data.StoreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission

class BuyerActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST_CODE = 200
    }

    private var lat = 0.0
    private var lng = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentItemName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)

        val token = getToken(this)
        val authHeader = "Bearer $token"

        val itemSearchEditText = findViewById<EditText>(R.id.searchItemEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        searchButton.setOnClickListener {
            currentItemName = itemSearchEditText.text.toString()

            if (currentItemName.isEmpty()) {
                Toast.makeText(this, "商品名を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                getLocation(authHeader)
            }


        }

        if (!token.isNullOrEmpty()) {
            RetrofitClient.apiService.getUserDetails(authHeader)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val userData = response.body()
                            val role = userData?.role
                            val loginMessageView = findViewById<TextView>(R.id.message)

                            when (role) {
                                "buyer" -> loginMessageView.text = "買い手ページ"
                                "seller" -> startActivity(Intent(this@BuyerActivity, SellerActivity::class.java))
                                else -> startActivity(Intent(this@BuyerActivity, LoginActivity::class.java))
                            }
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("Error", "APIリクエスト失敗: ${t.message}")
                    }
                })
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Log.e("Error", "tokenがnullまたは空です")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getLocation(authHeader: String) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude

                    val request = NearbyStoreSearchRequest(currentItemName, lat, lng)

                    RetrofitClient.apiService.searchNearbyStore(authHeader, request)
                        .enqueue(object : Callback<StoreResponse> {
                            override fun onResponse(call: Call<StoreResponse>, response: Response<StoreResponse>) {
                                if (response.isSuccessful) {
                                    val store = response.body()?.store
                                    val storename = store?.storename
                                    val storelat = store?.lat
                                    val storelng = store?.lng

                                    Toast.makeText(applicationContext, "${storename}", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@BuyerActivity, SearchResultActivity::class.java)
                                    intent.putExtra("latitude", storelat ?: 0.0)
                                    intent.putExtra("longitude", storelng ?: 0.0)
                                    intent.putExtra("storeName", storename ?: "")
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@BuyerActivity, "商品が見つかりませんでした", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<StoreResponse>, t: Throwable) {
                                Log.e("SearchAPI", "検索失敗: ${t.message}")
                            }
                        })
                } else {
                    Toast.makeText(this, "現在地を取得できませんでした", Toast.LENGTH_SHORT).show()
                }
            }
    }
}