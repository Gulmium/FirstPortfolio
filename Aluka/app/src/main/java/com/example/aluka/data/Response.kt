package com.example.aluka.data

import android.text.InputType
import com.google.gson.annotations.SerializedName


//User系
data class UserSignupResponse (
    val message: String
)

data class UserLoginResponse (
    val message: String,
    val access: String,
    val refresh: String
)

data class UserDetailResponse (
    val message: String
)

data class AddStoreResponse (
    val message: String
)

data class CheckItemResponse(
    @SerializedName("result") val result: Boolean,
    @SerializedName("item_name") val itemName: String?,
    @SerializedName("item_type") val itemType: String?
)


data class AddItemResponse (
    val message: String
)

data class AddStockResponse (
    val message: String
)

data class StoreResponse (
    val store: Store
)