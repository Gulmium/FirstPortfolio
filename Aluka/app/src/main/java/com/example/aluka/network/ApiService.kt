package com.example.aluka.network

import com.example.aluka.data.AddItemResponse
import com.example.aluka.data.AddStockRequest
import com.example.aluka.data.AddStockResponse
import com.example.aluka.data.AddStoreResponse
import com.example.aluka.data.CheckItemResponse
import com.example.aluka.data.Item
import com.example.aluka.data.User
import com.example.aluka.data.LoginUser
import com.example.aluka.data.NearbyStoreSearchRequest
import com.example.aluka.data.Stock
import com.example.aluka.data.Store
import com.example.aluka.data.StoreResponse
import com.example.aluka.data.UserSignupResponse
import com.example.aluka.data.UserLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @POST("accounts/signup/")
    fun registerUser(@Body user: User): Call<UserSignupResponse>

    @POST("accounts/login/")
    fun loginUser(@Body user: LoginUser): Call<UserLoginResponse>

    @GET("accounts/users/me/")
    fun getUserDetails(@Header("Authorization") authToken: String): Call<User>

    @GET("store/mine/")
    fun getStoreDetails(@Header("Authorization") authToken: String): Call<List<Store>>

    @GET("store/{id}/stock/")
    fun getStocks(
        @Path("id") storeId: Int,
        @Header("Authorization") authHeader: String): Call<List<Stock>>

    @POST("store/add/")
    fun addStore(
        @Header("Authorization") authToken: String,
        @Body store: Store): Call<AddStoreResponse>

    @GET("store/check_item/{jan_code}/")
    fun checkItem(
        @Path("jan_code") scannedValue: String,
        @Header("Authorization") authToken: String): Call<CheckItemResponse>

    @POST("store/add_stock/")
    fun addStock(
        @Header("Authorization") authToken: String,
        @Body addStockRequest: AddStockRequest): Call<AddStockResponse>

    @POST("store/search/item/")
    fun searchNearbyStore(
        @Header("Authorization") authToken: String,
        @Body nearbyStoreSearchRequest: NearbyStoreSearchRequest): Call<StoreResponse>


}
