package com.hannesdorfmann.mosby3.conductor.sample

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Interactor {

    companion object {

        fun loadData(): Observable<PartialState> {

            return Observable.just("Data loaded!")
                    .delay(2000, TimeUnit.MILLISECONDS)
                    .map<PartialState> { PartialState.DataPartial(it) }
                    .startWith(PartialState.LoadingDataPartial)
                    .subscribeOn(Schedulers.io())

        }

    }


}