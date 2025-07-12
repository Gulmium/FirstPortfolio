package com.example.aluka

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aluka.data.CheckItemResponse
import com.example.aluka.data.Item
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.network.RetrofitClient
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeReaderActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcodereader)
        requestCameraPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()
        val previewView = findViewById<PreviewView>(R.id.previewView)
        startCamera(previewView)
    }

    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val token = getToken(this)
        val authHeader = "Bearer $token"
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val analyzer = BarcodeAnalyzer { scannedValue ->
                runOnUiThread {
                    if (!isProcessing && scannedValue.length == 13 && scannedValue.all { it.isDigit() }) {
                        isProcessing = true
                        RetrofitClient.apiService.checkItem(scannedValue, authHeader)
                            .enqueue(object : Callback<CheckItemResponse> {
                                override fun onResponse(
                                    call: Call<CheckItemResponse>,
                                    response: Response<CheckItemResponse>
                                ) {
                                    val isExist: Boolean = response.body()?.result == true
                                    val itemName: String? = response.body()?.itemName
                                    val itemType: String? = response.body()?.itemType

                                    val builder = AlertDialog.Builder(this@BarcodeReaderActivity)

                                    if (isExist) {
                                        builder.setTitle("商品はすでに存在します")
                                            .setMessage("$itemName\nを追加しますか？")
                                    } else {
                                        builder.setTitle("データベースにない商品です")
                                            .setMessage("JANコード: $scannedValue\nを登録しますか？")
                                    }

                                    builder.setPositiveButton("はい") { _, _ ->
                                        goToItemEntry(scannedValue, itemName, itemType, isExist)
                                    }
                                        .setNegativeButton("取り消し") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()

                                }

                                override fun onFailure(call: Call<CheckItemResponse>, t: Throwable) {
                                    Log.e("BarcodeReader", "API接続失敗: ${t.localizedMessage}")
                                }

                            })

                    } else {
                        Log.d("Barcode", "13桁のJANコードではありません: $scannedValue")
                    }
                }
            }

            imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun goToItemEntry(scannedValue: String, itemName: String?, itemType: String?, isExist: Boolean) {
        val storeId = intent.getIntExtra("storeId",-1)
        val intent = Intent(this, ItemEntryActivity::class.java).apply {
            putExtra("item_number", scannedValue)
            putExtra("item_name", itemName)
            putExtra("item_type", itemType)
            putExtra("isExist", isExist)
            putExtra("storeId", storeId)
        }
        startActivity(intent)
        finish()
    }


    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1001
            )
        }
    }
}