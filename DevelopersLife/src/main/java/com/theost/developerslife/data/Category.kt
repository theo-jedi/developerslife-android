package com.theost.developerslife.data

enum class Category(val uiName: String, val apiName: String) {
    LATEST("Последние", "latest"), BEST("Лучшие", "top") , HOT("Горячие", "hot")
}