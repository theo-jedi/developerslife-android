package com.theost.developerslife.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.theost.developerslife.data.Category
import com.theost.developerslife.data.Direction
import com.theost.developerslife.data.Post
import com.theost.developerslife.data.Status
import com.theost.developerslife.network.RetrofitHelper
import java.lang.Exception

class PostViewModel : ViewModel() {

    private val _allData = MutableLiveData<Post>()
    val allData: LiveData<Post> = _allData

    private val _loadingStatus = MutableLiveData<Status>()
    val loadingStatus: LiveData<Status> = _loadingStatus

    private var data = mutableListOf<Post>()
    private var currentPage = -1

    private suspend fun loadData(category: Category, position: Int) {
        try {
            val response = RetrofitHelper.retrofitService.getPosts(category.apiName, currentPage)
            if (response.body()!!.list.isNotEmpty()) {
                data.addAll(response.body()!!.list)
                loadPost(category, position)
            } else {
                _loadingStatus.postValue(Status.EMPTY)
            }
        } catch (e: Exception) {
            _loadingStatus.postValue(Status.ERROR)
        }
    }

    suspend fun loadPost(category: Category, position: Int) {
        _loadingStatus.postValue(Status.LOADING)
        if (position >= data.size) {
            currentPage += 1
            loadData(category, position)
        } else {
            _allData.postValue(data[position])
        }
    }

    fun setStatus(status: Status) {
        _loadingStatus.value = status
    }

}