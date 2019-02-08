package com.yuricfurusho.dateandtimedraganddrop.repository

import android.util.Log
import com.yuricfurusho.dateandtimedraganddrop.model.DateAndTimeJSON
import com.yuricfurusho.dateandtimedraganddrop.ui.main.MainViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DateAndTimeRepository(private val viewModel: MainViewModel) {
    private var compositeDisposable = CompositeDisposable()
    var countRequests = 0L

    fun requestDateAndTime() {
        Log.v("countRequests", "countRequests:$countRequests")
        val dateAndTimeApi: DateAndTimeApi = DateAndTimeApi.getInstance()

        val flowableDateAndTime: Flowable<DateAndTimeJSON> =
            dateAndTimeApi.getDateAndTime("")

        val disposable = flowableDateAndTime.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dateAndTimeJSON: DateAndTimeJSON? ->
                    viewModel.setResponse(dateAndTimeJSON)
                },
                { t: Throwable? ->
                    viewModel.setErrorResponse(t)
                })

        compositeDisposable.add(disposable)
    }

}
