package com.hannesdorfmann.mosby3.conductor.sample;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Michael on 12.02.2017.
 */

public class MainPresenter extends MviBasePresenter<MainController, ViewState> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Override
    protected void bindIntents() {

        ArrayList<Observable<PartialStateChanges>> observables = new ArrayList<>();

        observables.add(intent(view -> view.loadData())
                .doOnNext(ignored -> Log.d(TAG, "Intent: Load data..."))
                .flatMap(ignored -> Interactor.loadData()
                        .map(data -> (PartialStateChanges)new PartialStateChanges.DataLoaded(data))
                        .startWith(new PartialStateChanges.LoadingData())
                        .subscribeOn(Schedulers.io())
                )
        );

        Observable<PartialStateChanges> allIntents = Observable.merge(observables);
        ViewState initialState = ViewState.builder().build();
        Observable<ViewState> stateObservable = allIntents.scan(initialState, this::viewStateReducer)
                .observeOn(AndroidSchedulers.mainThread());
        subscribeViewState(stateObservable, MainController::render);
    }

    private final ViewState viewStateReducer(ViewState previousState, PartialStateChanges partialChanges)
    {
        return partialChanges.computeNewState(previousState);
    }
}
