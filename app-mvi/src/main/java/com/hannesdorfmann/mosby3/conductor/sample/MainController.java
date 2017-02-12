package com.hannesdorfmann.mosby3.conductor.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.RestoreViewOnCreateMviController;
import com.hannesdorfmann.mosby3.conductor.sample.databinding.ControllerMainBinding;
import com.hannesdorfmann.mosby3.mvi.MviPresenter;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Predicate;

/**
 * Created by Michael on 12.02.2017.
 */

public class MainController extends RestoreViewOnCreateMviController<MainController, MainPresenter>{

    private static final String TAG = MainController.class.getSimpleName();

    private ControllerMainBinding mMainBinding = null;
    private boolean mRestoringViewState = false;

    public MainController()
    {
        this(null);
    }

    public MainController(Bundle args)
    {
        super(args);
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        mMainBinding = DataBindingUtil.inflate(inflater, R.layout.controller_main, container, false);
        return mMainBinding.getRoot();
    }

    @NonNull
    @Override
    protected void onDestroyView(@NonNull View view) {
        mMainBinding.unbind();
        super.onDestroyView(view);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void setRestoringViewState(boolean restoringViewState) {
        mRestoringViewState = restoringViewState;
    }

    // -----------------------
    // Model Rendering
    // -----------------------

    public void render(ViewState viewState)
    {
        if (viewState.loadingData())
            mMainBinding.tvText.append("\nLoading...");
        else if (viewState.data() != null)
            mMainBinding.tvText.append("\n" + viewState.data());
    }

    // -----------------------
    // Intents
    // -----------------------

    public Observable<Boolean> loadData()
    {
        // this is a cold observable, we only want to emit the flag to start data loading, if we are NOT restoring the view state
        return Observable.just(!mRestoringViewState)
                .filter(value -> true)
                .doOnComplete(() -> Log.d(TAG, "loadDataIntent"));
    }
}