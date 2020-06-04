package com.hannesdorfmann.mosby3.conductor.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.hannesdorfmann.mosby3.MviController
import com.hannesdorfmann.mosby3.conductor.sample.databinding.ControllerStartBinding
import io.reactivex.Observable

class StartController:MviController<StartView, StartPresenter>,StartView {



    private val TAG = StartController::class.java.simpleName

    private var mainBinding:ControllerStartBinding? = null
    private var mRestoringViewState:Boolean = false

    constructor() : super()
    constructor(args: Bundle?) : super(args){

        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun createPresenter(): StartPresenter = StartPresenter()

    override fun setRestoringViewState(restoringViewState: Boolean) {
        mRestoringViewState = restoringViewState
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        mainBinding = DataBindingUtil.inflate(inflater,R.layout.controller_start,container,false)

        return mainBinding!!.root
    }

    /*
    * Intents
    */
    override fun loadData(): Observable<Boolean> {

        // this is a cold observable, we only want to emit the flag to start data loading, if we are NOT restoring the view state
        return Observable.just(!mRestoringViewState)
                .filter { it }
                .doOnComplete { Log.d(TAG, "loadDataIntent") }
    }

    /*
    * Rendering
    */
    override fun render(viewState: ViewState) {

        with(viewState){

            when{

                isLoading && data == null-> mainBinding!!.tvText.append("\nLoading...")
                data!=null-> mainBinding!!.tvText.append("\n${viewState.data}")


            }
        }
    }

    override fun onDestroyView(view: View) {
        mainBinding!!.unbind()
        mainBinding = null
        super.onDestroyView(view)
    }

}