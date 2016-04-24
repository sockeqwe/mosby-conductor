package com.hannesdorfmann.mosby.conductor.viewstate.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.conductor.delegate.MvpConductorLifecycleListener;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * /**
 * This {@link Controller.LifecycleListener} extends from {@link MvpConductorLifecycleListener} to
 * add ViewState support.
 * This class uses {@link MvpViewStateConductorDelegateCallback} to call Mosby's methods like
 * {@code
 * createViewState()} according to the {@link Controller} lifecycle events.
 *
 * <p>
 * {@link MvpViewStateController} already implements {@link MvpViewStateConductorDelegateCallback}
 * and registers this
 * class as listener.
 * </p>
 *
 * @author Hannes Dorfmann
 * @see MvpViewStateConductorDelegateCallback
 * @see MvpViewStateController
 * @since 1.0
 */
public class MvpViewStateConductorLifecycleListener<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpConductorLifecycleListener<V, P> {

  private boolean retainingViewState = false;

  public MvpViewStateConductorLifecycleListener(
      MvpViewStateConductorDelegateCallback<V, P, VS> callback) {
    super(callback);
  }

  @Override public void preAttach(@NonNull Controller controller, @NonNull View view) {
    super.preAttach(controller, view);

    MvpViewStateConductorDelegateCallback<V, P, VS> viewStateCallback =
        ((MvpViewStateConductorDelegateCallback<V, P, VS>) callback);

    // Create viewstate instance if needed

    VS viewState = viewStateCallback.getViewState();
    if (viewState == null) {
      viewState = viewStateCallback.createViewState();
      if (viewState == null) {
        throw new NullPointerException(
            "ViewState from createViewState() is null in " + viewStateCallback);
      }
      viewStateCallback.setViewState(viewState);
      viewStateCallback.onNewViewStateInstance();
    } else {

      viewStateCallback.setRestoringViewState(true);
    //  viewState.apply(callback.getMvpView(), retainingViewState);
      viewStateCallback.setRestoringViewState(false);

      viewStateCallback.onViewStateInstanceRestored(retainingViewState);
    }

  }

  @Override public void postDetach(@NonNull Controller controller, @NonNull View view) {
    super.postDetach(controller, view);

    // Reset for next time controller gets instantiated after screen orientation changes
    retainingViewState = false;
  }

  @Override public void onSaveViewState(@NonNull Controller controller, @NonNull Bundle outState) {

    MvpViewStateConductorDelegateCallback<V, P, VS> vsCallback =
        ((MvpViewStateConductorDelegateCallback<V, P, VS>) callback);

    VS viewState = vsCallback.getViewState();
    if (viewState == null) {
      throw new NullPointerException("ViewState from getViewState() is null in " + callback);
    }

    if (viewState instanceof RestorableViewState) {
      ((RestorableViewState) viewState).saveInstanceState(outState);
    }
  }

  @Override
  public void onRestoreViewState(@NonNull Controller controller, @NonNull Bundle savedViewState) {

    MvpViewStateConductorDelegateCallback<V, P, VS> vsCallback =
        ((MvpViewStateConductorDelegateCallback<V, P, VS>) callback);

    VS viewState = vsCallback.getViewState();

    if (viewState == null) {
      retainingViewState = false;

      // Try to restore from bundle.
      viewState = vsCallback.createViewState();
      if (viewState instanceof RestorableViewState) {
        VS restoredViewState =
            (VS) ((RestorableViewState) viewState).restoreInstanceState(savedViewState);

        if (restoredViewState != null) {
          vsCallback.setViewState(restoredViewState);
        }
      }
    } else {
      // ViewState in memory retained
      retainingViewState = true;
    }
  }
}
