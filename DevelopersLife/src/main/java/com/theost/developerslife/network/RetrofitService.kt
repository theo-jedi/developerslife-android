package com.theost.developerslife.network

import com.theost.developerslife.data.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitService {
    @GET("{type}/{page}?json=true")
    suspend fun getPosts(
        @Path(value = "type", encoded = true) type: String,
        @Path(value = "page", encoded = true) page: Int
    ): Response<ApiResponse>
}