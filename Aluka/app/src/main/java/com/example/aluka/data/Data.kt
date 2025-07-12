package com.example.aluka.data

//User系
data class User (
    val username: String,
    val password: String,
    val role: String,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: String? = null
)

data class LoginUser (
    val username: String,
    val password: String
)


//Store系
data class Store (
    val id: Int? = null,
    val storename: String,
    val lat: Double,
    val lng: Double
)

data class Item (
    val item_number: String,
    val item_name: String? = null,
    val item_type: String? = null
)

data class Stock (
    val store: Store,
    val item: Item,
    val quantity: Int
)

data class AddStockRequest (
    val store: Int,
    val item: Item,
    val quantity: String
)

data class NearbyStoreSearchRequest (
    val item_name: String,
    val lat: Double,
    val lng: Double
)