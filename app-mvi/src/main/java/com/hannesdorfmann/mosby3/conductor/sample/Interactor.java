package com.hannesdorfmann.mosby3.conductor.sample;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Michael on 12.02.2017.
 */

public class Interactor {

    public static Observable<String> loadData()
    {
        return Observable.just("Data loaded!")
                .delay(2000, TimeUnit.MILLISECONDS);
    }
}
