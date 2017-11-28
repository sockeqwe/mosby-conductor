package com.hannesdorfmann.mosby.conductor

import android.util.Log
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * Created by hannes on 28.11.17.
 */
class MvpTestPresenter(private val presenterId : Int) : MvpBasePresenter<MvpView>(){

    private val TAG = "MvpTestPresenter"

    override fun attachView(view: MvpView) {
        super.attachView(view)
        Log.d(TAG, "Presenter${presenterId}.attachView()")
    }

    override fun detachView() {
        super.detachView()
        Log.d(TAG, "Presenter${presenterId}.detachView()")
    }

    override fun destroy() {
        super.destroy()
        Log.d(TAG, "Presenter${presenterId}.destroy()")
    }
}