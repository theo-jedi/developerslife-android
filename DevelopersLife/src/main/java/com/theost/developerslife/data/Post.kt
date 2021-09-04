package com.theost.developerslife.data

import com.google.gson.annotations.SerializedName

data class Post(@SerializedName("gifURL") val imageLink: String, @SerializedName("description") val description: String)