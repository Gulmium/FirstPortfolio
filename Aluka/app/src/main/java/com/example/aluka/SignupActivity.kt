package com.example.aluka

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aluka.data.User
import com.example.aluka.data.UserSignupResponse
import com.example.aluka.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val selectRadio = findViewById<RadioGroup>(R.id.selectRadio)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val selectedId = selectRadio.checkedRadioButtonId
            val role = if (selectedId == R.id.sellerRadio) "seller" else "buyer"

            val is_active = true
            val is_staff = false
            val is_superuser = false
            val last_login = null

            val user = User(username, password, role, is_active, is_staff, is_superuser, last_login)

            // Retrofit を使って API にデータを送信
            RetrofitClient.apiService.registerUser(user).enqueue(object : Callback<UserSignupResponse> {
                override fun onResponse(call: Call<UserSignupResponse>, response: Response<UserSignupResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "登録成功: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "登録失敗: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserSignupResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "エラー: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}