package com.yuricfurusho.dateandtimedraganddrop.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.yuricfurusho.dateandtimedraganddrop.model.DateAndTimeJSON
import com.yuricfurusho.dateandtimedraganddrop.repository.DateAndTimeRepository
import com.yuricfurusho.dateandtimedraganddrop.ui.main.MainViewModel.ENVIRONMENT.LOCAL
import com.yuricfurusho.dateandtimedraganddrop.ui.main.MainViewModel.ENVIRONMENT.REMOTE
import com.yuricfurusho.dateandtimedraganddrop.utils.DateUtil.Companion.getCurrentDateTime

class MainViewModel : ViewModel() {
    enum class ENVIRONMENT { LOCAL, REMOTE }

    private lateinit var environment: MutableLiveData<ENVIRONMENT>
    private lateinit var loading: MutableLiveData<Boolean>
    private lateinit var mDateTime: MutableLiveData<String>
    private val repository = DateAndTimeRepository(this)

    fun getDateTime(): LiveData<String> {
        if (!::mDateTime.isInitialized) {
            mDateTime = MutableLiveData()
            loadDateAndTime()
        }
        return mDateTime
    }

    fun loadDateAndTime() {
        loading.value = true
        when (getEnvironment().value) {
            LOCAL -> loadLocalDateAndTime()
            REMOTE -> loadRemoteDateAndTime()
        }
    }

    private fun loadLocalDateAndTime() {
        val dateAndTimeJSON = DateAndTimeJSON(getCurrentDateTime())

        mDateTime.value = "LOCAL: " + dateAndTimeJSON.datetime
        loading.value = false
    }

    private fun loadRemoteDateAndTime() {
        repository.requestDateAndTime()
    }

    fun setResponse(dateAndTimeJSON: DateAndTimeJSON?) {
        mDateTime.value = dateAndTimeJSON?.datetime ?: DateAndTimeJSON("").datetime
        loading.value = false
    }

    fun setErrorResponse(t: Throwable?) {
        Log.d("onFailure", "t=" + t?.localizedMessage)
        loading.value = false
    }

    fun getEnvironment(): LiveData<ENVIRONMENT> {
        if (!::environment.isInitialized) {
            environment = MutableLiveData()
            environment.value = REMOTE
        }
        return environment
    }

    fun setEnvironment(value: ENVIRONMENT) {
        if (environment.value != value) {
            environment.value = value
            loadDateAndTime()
        }
    }

    fun getLoading(): LiveData<Boolean> {
        if (!::loading.isInitialized) {
            loading = MutableLiveData()
            loading.value = false
        }
        return loading
    }

}
