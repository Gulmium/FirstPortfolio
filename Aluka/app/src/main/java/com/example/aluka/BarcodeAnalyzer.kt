package com.example.aluka

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage



class BarcodeAnalyzer(
    private val onScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8
                )
                .build()

            val scanner = BarcodeScanning.getClient(options) // ✅ ここでオプションを適用

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isEmpty()) {
                        Log.d("Barcode", "バーコードが検出されませんでした")
                    } else {
                        barcodes.forEach { barcode ->
                            barcode.rawValue?.let { rawValue ->
                                Log.d("Barcode", "読み取ったデータ: $rawValue")
                                onScanned(rawValue)
                                return@forEach
                            }
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Barcode", "スキャンエラー: ${exception.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}