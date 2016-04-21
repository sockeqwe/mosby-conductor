package com.hannesdorfmann.mosby.conductor.viewstate.lce;

import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;
import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorDelegateCallback;
import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorLifecycleListener;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.conductor.lce.MvpLceController;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

/**
 * {@link MvpLceController} with {@link ViewState} support
 *
 * @author Hannes Dorfmann
 * @see MvpLceController
 * @see MvpViewStateController
 * @since 1.0
 */
public abstract class MvpLceViewStateController<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpLceController<CV, M, V, P>
    implements MvpViewStateConductorDelegateCallback<V, P, LceViewState<M, V>> {

  protected LceViewState<M, V> viewState;
  protected boolean restoringViewState = false;

  @Override protected Controller.LifecycleListener getMosbyLifecycleListener() {
    return new MvpViewStateConductorLifecycleListener<>(this);
  }

  @Override public boolean isRestoringViewState() {
    return restoringViewState;
  }

  @Override public void setRestoringViewState(boolean restoringViewState) {
    this.restoringViewState = restoringViewState;
  }

  @Override public void setViewState(LceViewState<M, V> viewState) {
    this.viewState = viewState;
  }

  @Override public LceViewState<M, V> getViewState() {
    return viewState;
  }



  @Override public void showContent() {
    super.showContent();
    viewState.setStateShowContent(getData());
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    viewState.setStateShowError(e, pullToRefresh);
  }

  @Override public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    viewState.setStateShowLoading(pullToRefresh);
  }


  @Override protected void showLightError(String msg) {
    if (isRestoringViewState()) {
      return; // Do not display toast again while restoring viewstate
    }

    super.showLightError(msg);
  }

  @Override public void onViewStateInstanceRestored(boolean instanceStateRetained) {
    // Not needed in general. override it in subclass if you need this callback
  }

  @Override public void onNewViewStateInstance() {
    loadData(false);
  }

  /**
   * Get the data that has been set before in {@link #setData(Object)}
   * <p>
   * <b>It's necessary to return the same data as set before to ensure that {@link ViewState} works
   * correctly</b>
   * </p>
   *
   * @return The data
   */
  public abstract M getData();
}
