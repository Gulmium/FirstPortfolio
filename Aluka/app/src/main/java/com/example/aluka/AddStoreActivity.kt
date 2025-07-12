package com.example.aluka

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aluka.data.AddStoreResponse
import com.example.aluka.data.Store
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.data.User
import com.example.aluka.data.UserSignupResponse
import com.example.aluka.network.RetrofitClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header

class AddStoreActivity : AppCompatActivity(),OnMapReadyCallback {

    companion object {
        private const val REQUEST_CODE = 100
    }

    lateinit var authHeader: String

    var lat = 0.0
    var lng = 0.0

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isMapReady = false
    private var isLocationReady = false

    private fun tryDisplayLocation() {
        val imHere = LatLng(lat, lng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(imHere, 15f))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)
        val token = getToken(this)
        authHeader = "Bearer $token"


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        } else {
            getLocation() // 権限がすでにある場合は位置情報を取得
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.getlocation) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonMap = findViewById<Button>(R.id.back_main)

        buttonMap.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // ユーザーが許可した場合のみ位置情報を取得
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        println("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                    } else {
                        println("Location not found")
                    }
                }
        } else {
            println("位置情報の取得を拒否しました")
        }

    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                    val imHere = LatLng(lat, lng)
                    //表示を現在地に移動
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(imHere, 15f))
                } else {
                    println("Location not found")
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        tryDisplayLocation()

        val pinButton = findViewById<Button>(R.id.place_pin_button)
        pinButton.setOnClickListener {
            val center = mMap.cameraPosition.target
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(center).title("ここにピンを設置"))

            val editText = EditText(this).apply {
                hint = "店舗名を入力してください"
                inputType = InputType.TYPE_CLASS_TEXT
            }

            val dialog = AlertDialog.Builder(this)
                .setTitle("店舗情報の確認")
                .setMessage("この場所でいいですか？")
                .setView(editText) // 入力欄をダイアログに追加！
                .setPositiveButton("店舗を登録") { _, _ ->
                    val storeName = editText.text.toString()
                    val lat = center.latitude
                    val lng = center.longitude

                    val store = Store(null, storeName, lat, lng)

                    if (storeName.isBlank()) {
                        Toast.makeText(this, "店舗名を入力してください", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "「$storeName」の位置を確定しました", Toast.LENGTH_SHORT).show()
                        RetrofitClient.apiService.addStore(authHeader,store).enqueue(object : Callback<AddStoreResponse> {
                            override fun onResponse(call: Call<AddStoreResponse>, response: Response<AddStoreResponse>){
                                if (response.isSuccessful) {
                                    Toast.makeText(applicationContext, "登録成功", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(applicationContext, "登録失敗", Toast.LENGTH_LONG).show()
                                }
                            }
                            override fun onFailure(call: Call<AddStoreResponse>, t: Throwable) {
                                Log.e("Error", "APIリクエスト失敗")
                            }
                        })
                    }
                }
                .setNegativeButton("キャンセル") { d, _ -> d.dismiss() }
                .create()

            dialog.setOnShowListener {
                dialog.window?.apply {
                    setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
                    val params = attributes
                    params.y = 200
                    attributes = params
                }
            }
            if (!isFinishing && !isDestroyed) {
                dialog.show()
            }
        }

    }
}