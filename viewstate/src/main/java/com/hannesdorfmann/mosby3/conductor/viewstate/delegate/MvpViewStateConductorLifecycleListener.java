package com.hannesdorfmann.mosby3.conductor.viewstate.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.conductor.viewstate.MvpViewStateController;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.conductor.delegate.MvpConductorLifecycleListener;
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

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

  boolean changingConfigurations = false;

  public MvpViewStateConductorLifecycleListener(
      MvpViewStateConductorDelegateCallback<V, P, VS> callback) {
    super(callback);
  }

  @Override public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
    super.preDestroyView(controller, view);
    changingConfigurations = controller.getActivity().isChangingConfigurations();
  }

  @Override public void preAttach(@NonNull Controller controller, @NonNull View view) {
    MvpViewStateConductorDelegateCallback<V, P, VS> vsCallback =
        ((MvpViewStateConductorDelegateCallback<V, P, VS>) callback);

    if (vsCallback.getViewState() == null) {
      // Creating view for the first time, so onRestoreViewState() never has been called
      // Create ViewState
      VS viewState = vsCallback.createViewState();
      if (viewState == null) {
        throw new NullPointerException("ViewState from createViewState() is null in " + vsCallback);
      }

      vsCallback.setViewState(viewState);
      vsCallback.onNewViewStateInstance();
    }

    super.preAttach(controller, view);
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

      viewState = vsCallback.createViewState();
      if (viewState == null) {
        throw new NullPointerException("ViewState from createViewState() is null in " + vsCallback);
      }

      if (viewState instanceof RestorableViewState) {

        // Try to restore from bundle.
        VS restoredViewState =
            (VS) ((RestorableViewState) viewState).restoreInstanceState(savedViewState);

        if (restoredViewState != null) {
          // Restored from bundle
          vsCallback.setViewState(restoredViewState);

          vsCallback.setRestoringViewState(true);
          restoredViewState.apply(callback.getMvpView(), false);
          vsCallback.setRestoringViewState(false);

          vsCallback.onViewStateInstanceRestored(false);
        }
      }
    } else {
      // ViewState in memory retained
      // TODO Conductor ViewRetainType
      vsCallback.setRestoringViewState(true);
      viewState.apply(callback.getMvpView(), changingConfigurations);
      vsCallback.setRestoringViewState(false);
      vsCallback.onViewStateInstanceRestored(changingConfigurations);
    }
  }
}
