package com.example.aluka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aluka.R.id.barcodeReaderButton

class CheckStockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_stock)

        val barcodeReaderButton = findViewById<Button>(barcodeReaderButton)

        barcodeReaderButton.setOnClickListener {
            val intent = Intent(this, BarcodeReaderActivity::class.java)
            startActivity(intent)
        }
    }
}