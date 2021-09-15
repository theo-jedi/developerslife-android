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

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> = _currentPosition

    private var data = mutableListOf<Post>()
    private var currentPage = -1

    private suspend fun loadData(category: Category, direction: Direction) {
        try {
            val response = RetrofitHelper.retrofitService.getPosts(category.apiName, currentPage + 1)
            if (response.body()!!.list.isNotEmpty()) {
                data.addAll(response.body()!!.list)
                currentPage += 1
                loadPost(category, direction)
            } else {
                _currentPosition.value = _currentPosition.value?.plus(direction.value) ?: 0
                _loadingStatus.postValue(Status.EMPTY)
            }
        } catch (e: Exception) {
            _loadingStatus.postValue(Status.ERROR)
        }
    }

    suspend fun loadPost(category: Category, direction: Direction) {
        _loadingStatus.postValue(Status.LOADING)
        if (_currentPosition.value?.plus(direction.value) ?: 0 >= data.size) {
            loadData(category, direction)
        } else {
            _currentPosition.value = _currentPosition.value?.plus(direction.value) ?: 0
            _allData.postValue(data[currentPosition.value!!])
        }
    }

    fun setStatus(status: Status) {
        _loadingStatus.value = status
    }

}