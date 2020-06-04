package com.hannesdorfmann.mosby3.conductor.sample

import com.hannesdorfmann.mosby3.conductor.sample.Interactor.Companion.loadData
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Michael on 12.02.2017.
 * Modified to Kotlin by Paul-E on 03.06.2020.
 */
class StartPresenter : MviBasePresenter<StartView, ViewState>() {

    override fun bindIntents() {

        val loadDataPartial:Observable<PartialState> = intent(StartView::loadData)
                .flatMap { loadData() }
                .observeOn(AndroidSchedulers.mainThread())

        val initialState = ViewState(isLoading = false,data = null)

        subscribeViewState(loadDataPartial.scan(initialState,this::stateReducer).distinctUntilChanged(),StartView::render)
    }

    private fun stateReducer(
            previousState: ViewState,
            interState: PartialState
    ): ViewState{

        when(interState){

            is PartialState.LoadingDataPartial-> return previousState.copy(

                    isLoading = true,
                    data = null
            )

            is PartialState.DataPartial-> return previousState.copy(

                    isLoading = false,
                    data = interState.data

            )


        }


    }

}