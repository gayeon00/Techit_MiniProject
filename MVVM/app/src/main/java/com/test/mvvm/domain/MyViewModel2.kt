package com.test.mvvm.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mvvm.MainActivity
import com.test.mvvm.repository.Test1Repository

class MyViewModel2 : ViewModel() {
    var dataList = MutableLiveData<MutableList<Test>>()

    init {
        getAll()
    }

    fun getAll() {
        //repository로부터 데이터를 가져와 설정해줌
        dataList.value = Test1Repository.getAll(MainActivity.mainActivity)
    }
}

class Test(var index: Int, var data1: String, var data2: String)