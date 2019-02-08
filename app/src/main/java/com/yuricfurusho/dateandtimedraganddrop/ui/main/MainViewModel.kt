package com.yuricfurusho.dateandtimedraganddrop.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
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

    private var mPreviousTime: Long = System.currentTimeMillis()
    private var mInterval: Long = 1000L
    private val mHandler: Handler? = Handler()

    fun getDateTime(): LiveData<String> {
        if (!::mDateTime.isInitialized) {
            mDateTime = MutableLiveData()
            loadDateAndTime()
        }
        return mDateTime
    }

    fun loadDateAndTime() {
        val currentTime = System.currentTimeMillis()
        mInterval = mPreviousTime - currentTime
        mPreviousTime = currentTime

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

    private var countRequests: Long = 0

    fun setResponse(dateAndTimeJSON: DateAndTimeJSON?) {
        Log.v("countRequests", "countRequestsENDED:$countRequests")

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
            environment.value = LOCAL
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

    val runnable = object : Runnable {

        override fun run() {
            try {
                loadDateAndTime()
            } finally {
                mHandler?.postDelayed(this, mInterval)
            }
        }
    }

    fun startRepeatingTask() {
        runnable.run()
    }

    fun stopRepeatingTask() {
        mHandler?.removeCallbacks(runnable)
    }
}
