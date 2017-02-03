package com.hannesdorfmann.mosby3.conductor.viewstate.delegate;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.conductor.delegate.MvpConductorDelegateCallback;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

/**
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 * @param <VS> Thet type of the {@link ViewState}
 * @author Hannes Dorfmann
 * @see MvpConductorDelegateCallback
 * @since 1.0
 */
public interface MvpViewStateConductorDelegateCallback<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpConductorDelegateCallback<V, P> {

  /**
   * Get the viewState
   */
  public VS getViewState();

  /**
   * Set the viewstate.
   */
  public void setViewState(VS viewState);

  /**
   * Create the viewstate.
   */
  @NonNull public VS createViewState();

  /**
   * This method will be called by to inform that restoring
   * the view state is in progress.
   *
   * @param restoringViewState true, if restoring viewstate is in progress, otherwise false
   */
  public void setRestoringViewState(boolean restoringViewState);

  /**
   * @return true if the viewstate is restoring right now (not finished yet). Otherwise false.
   */
  public boolean isRestoringViewState();

  /**
   * Called if the {@link ViewState} instance has been restored successfully.
   * <p>
   * In this method you have to restore the viewstate by reading the view state properties and
   * setup
   * the view to be on the same state as before.
   * </p>
   *
   * @param instanceStateRetained true, if the viewstate has been retained (i.e. during screen
   * orientation changes), otherwise false (i.e. restored from bundle after activity process has
   * been killed).
   */
  public void onViewStateInstanceRestored(boolean instanceStateRetained);

  /**
   * Called if a new {@link ViewState} has been created because no viewstate previousely has been
   * found.
   * <p><b>Typically this is called the first time the <i>Controller</i> starts and therefore
   * no previous view state exists</b></p>
   */
  public void onNewViewStateInstance();
}
