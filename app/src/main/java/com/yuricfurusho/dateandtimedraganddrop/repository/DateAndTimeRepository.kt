package com.yuricfurusho.dateandtimedraganddrop.repository

import com.yuricfurusho.dateandtimedraganddrop.model.DateAndTimeJSON
import com.yuricfurusho.dateandtimedraganddrop.ui.main.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DateAndTimeRepository(private val viewModel: MainViewModel) {
    private var compositeDisposable = CompositeDisposable()

    fun requestDateAndTime() {
        val dateAndTimeApi: DateAndTimeApi = DateAndTimeApi.create()

        val observableDateAndTime: Observable<DateAndTimeJSON> =
            dateAndTimeApi.getDateAndTime("")

        val disposable = observableDateAndTime.subscribeOn(Schedulers.io())
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
