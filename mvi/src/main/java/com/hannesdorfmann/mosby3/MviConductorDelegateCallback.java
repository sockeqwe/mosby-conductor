package com.hannesdorfmann.mosby3;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * The MvpDelegate callback that will be called from  {@link
 * MviConductorLifecycleListener}. This interface must be implemented by all
 * Conductor Controllers that you want to support mosbys mvp.
 *
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 * @author Hannes Dorfmann
 * @since 1.0
 */
public interface MviConductorDelegateCallback<V extends MvpView, P extends MvpPresenter<V>> {

  /**
   * Creates the presenter instance
   *
   * @return the created presenter instance
   */
  @NonNull public P createPresenter();

  /**
   * Get the presenter. If null is returned, then a internally a new presenter instance gets
   * created
   * by calling {@link #createPresenter()}
   *
   * @return the presenter instance. can be null.
   */
  @Nullable public P getPresenter();

  /**
   * Sets the presenter instance
   *
   * @param presenter The presenter instance
   */
  public void setPresenter(@NonNull P presenter);

  /**
   * Get the MvpView for the presenter
   *
   * @return The view associated with the presenter
   */
  @NonNull public V getMvpView();

  /**
   * This method will be called to inform that restoring
   * the view state is in progress (true as parameter value) and when restoring is finished (false
   * as parameter value). Typically this is set to true when the view is reattached to the
   * presenter
   * after orientation changes or when navigating back from backstack.
   * Usually this is useful if you want to know whether or not you should run certain animations
   * because of the state of the view has changed or the view has been reattached to the presenter
   * (i.e. orientation change, coming back from back stack) and therefore no animations should run.
   *
   * @param restoringViewState true, if restoring view state is in progress, otherwise false
   */
  public void setRestoringViewState(boolean restoringViewState);
}
