package com.theost.developerslife.network

object RetrofitHelper {
    private const val BASE_URL = "https://developerslife.ru/"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}