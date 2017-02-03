package com.hannesdorfmann.mosby3.conductor.viewstate;

import android.os.Bundle;
import com.hannesdorfmann.mosby3.conductor.viewstate.delegate.MvpViewStateConductorDelegateCallback;
import com.hannesdorfmann.mosby3.conductor.viewstate.delegate.MvpViewStateConductorLifecycleListener;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby3.conductor.viewstate.delegate.MvpViewStateConductorDelegateCallback;
import com.hannesdorfmann.mosby3.conductor.viewstate.delegate.MvpViewStateConductorLifecycleListener;
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController;

/**
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class MvpViewStateController<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpController<V, P> implements MvpViewStateConductorDelegateCallback<V, P, VS> {

  protected VS viewState;
  protected boolean restoringViewState = false;

  public MvpViewStateController() {
  }

  public MvpViewStateController(Bundle args) {
    super(args);
  }

  @Override protected LifecycleListener getMosbyLifecycleListener() {
    return new MvpViewStateConductorLifecycleListener<>(this);
  }

  @Override public boolean isRestoringViewState() {
    return restoringViewState;
  }

  @Override public void setRestoringViewState(boolean restoringViewState) {
    this.restoringViewState = restoringViewState;
  }

  @Override public void setViewState(VS viewState) {
    this.viewState = viewState;
  }

  @Override public VS getViewState() {
    return viewState;
  }
}
