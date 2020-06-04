package com.hannesdorfmann.mosby3.conductor.sample

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface StartView: MvpView {

    fun loadData():Observable<Boolean>

    fun render(viewState: ViewState)


}