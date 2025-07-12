package com.example.aluka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aluka.data.TokenManager.getToken
import com.example.aluka.data.User
import com.example.aluka.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginsuccess)

        val token = getToken(this)
        val authHeader = "Bearer $token"
        val loginButton = findViewById<Button>(R.id.loginButton)
        val loginMessageView = findViewById<TextView>(R.id.login_success_message)
        val myPageButton = findViewById<Button>(R.id.myPageButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

       if(!token.isNullOrEmpty()){
           RetrofitClient.apiService.getUserDetails(authHeader).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val userData = response.body()
                        val role = userData?.role

                        val msg = "こんにちは、${userData?.username}さん"
                        loginMessageView.text = msg

                        if(role == "seller"){
                            myPageButton.setOnClickListener{
                                val intent =
                                    Intent(this@LoginSuccessActivity, SellerActivity::class.java)
                                startActivity(intent)
                            }
                        }else if(role == "buyer") {
                            myPageButton.setOnClickListener {
                                val intent =
                                    Intent(this@LoginSuccessActivity, BuyerActivity::class.java)
                                startActivity(intent)
                            }
                        }else{
                            Log.e("Error", "sellerでもbuyerでもありません")
                        }

                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("Error", "APIリクエスト失敗")
                }
            })


       }else {
           //tokenがないときログイン画面に戻る
           val intent = Intent(this@LoginSuccessActivity,LoginActivity::class.java)
           startActivity(intent)
           finish()
           Log.e("Error", "データ取得失敗,tokenがnullかemptyです")
       }


    }

}