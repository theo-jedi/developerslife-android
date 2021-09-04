package com.theost.developerslife.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(@SerializedName("result") val list: List<Post>)