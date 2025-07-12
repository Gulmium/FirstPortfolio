package com.example.aluka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aluka.data.LoginUser
import com.example.aluka.data.UserLoginResponse
import com.example.aluka.data.TokenManager.saveToken
import com.example.aluka.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        val signupButton = findViewById<Button>(R.id.signupButton)
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val user = LoginUser(username, password)

            RetrofitClient.apiService.loginUser(user).enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.access
                        if(token != null){
                            val appContext = applicationContext
                            saveToken(appContext, token)
                            Toast.makeText(this@LoginActivity, "結果: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity,LoginSuccessActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Log.e("Error", "トークンが取得できません")
                        }

                    } else {
                        Toast.makeText(this@LoginActivity, "結果: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }


                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "エラー: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        }
    }
}