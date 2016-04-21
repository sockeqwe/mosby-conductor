package com.hannesdorfmann.mosby.conductor.viewstate;

import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorDelegateCallback;
import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorLifecycleListener;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 *
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class MvpViewStateController<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpController<V, P> implements MvpViewStateConductorDelegateCallback<V, P, VS> {

  protected VS viewState;
  protected boolean restoringViewState = false;

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
