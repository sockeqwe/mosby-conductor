package com.hannesdorfmann.mosby3;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.mvi.MviPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * This {@link Controller.LifecycleListener} has to added to your Controller to integrate Mosby.
 * This class uses {@link MviConductorDelegateCallback} to call Mosby's methods like {@code
 * createPresenter()} according to the {@link Controller} lifecycle events.
 *
 * <p>
 * {@link MviController} already implements {@link MviConductorDelegateCallback} and registers this
 * class as listener.
 * </p>
 *
 * @author Hannes Dorfmann
 * @see MviConductorDelegateCallback
 * @see MviController
 * @since 3.0
 */
public class MviConductorLifecycleListener<V extends MvpView, P extends MviPresenter<V, ?>>
    extends Controller.LifecycleListener {

  private static final boolean DEBUG = false;
  private static final String DEBUG_TAG = "MviLifecycleListener";

  protected MviConductorDelegateCallback<V, P> callback;
  private boolean keepPresenterInstance;

  private P presenter;

  /**
   * Instantiate a new Mosby MVP Listener
   *
   * @param callback {@link MviConductorDelegateCallback} to set presenter. Typically the
   * controller
   * himself.
   */
  public MviConductorLifecycleListener(MviConductorDelegateCallback<V, P> callback,
      boolean keepPresenterInstance) {
    this.callback = callback;
    this.keepPresenterInstance = keepPresenterInstance;
  }

  /**
   * Instantiate a new Mosby MVP Listener
   *
   * @param callback {@link MviConductorDelegateCallback} to set presenter. Typically the
   * controller
   * himself.
   */
  public MviConductorLifecycleListener(MviConductorDelegateCallback<V, P> callback) {
    this(callback, true);
  }

  @Override public void postCreateView(@NonNull Controller controller, @NonNull View view) {

    boolean viewStateWillBeRestored = false;

    if (presenter == null) {
      // Process death,
      // hence no presenter with the given viewState id stored, although we have a viewState id
      presenter = callback.createPresenter();
      if (DEBUG) {
        Log.d(DEBUG_TAG,
            "New Presenter instance created for " + controller + ". Presenter: " + presenter);
      }
    } else {
      viewStateWillBeRestored = true;
      if (DEBUG) {
        Log.d(DEBUG_TAG, "Reusing Presenter instance for controller "
            + controller
            + ". Presenter: "
            + presenter);
      }
    }

    V viewMpv = callback.getMvpView();
    if (viewMpv == null) {
      throw new NullPointerException(
          "MvpView returned from getMvpView() is null. Returned by " + controller.getActivity());
    }

    if (viewStateWillBeRestored) {
      callback.setRestoringViewState(true);
    }

    presenter.attachView(viewMpv);

    if (viewStateWillBeRestored) {
      callback.setRestoringViewState(false);
    }

    if (DEBUG) {
      Log.d(DEBUG_TAG,
          "MvpView attached to Presenter. MvpView: " + view + "   Presenter: " + presenter);
    }
  }

  /**
   * Determines whether or not a Presenter Instance should be kept
   *
   * @param keepPresenterInstance true, if the delegate has enabled keep
   */
  static boolean retainPresenterInstance(boolean keepPresenterInstance, Controller controller) {

    return keepPresenterInstance && (controller.getActivity().isChangingConfigurations()
        || !controller.getActivity().isFinishing()) && !controller.isBeingDestroyed();
  }

  @Override public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
    presenter.detachView();

    if (DEBUG) {
      Log.d(DEBUG_TAG, "detached MvpView from Presenter. MvpView "
          + callback.getMvpView()
          + "   Presenter: "
          + presenter);
    }
  }

  @Override public void postDestroy(@NonNull Controller controller) {
    if (DEBUG) {
      Log.d(DEBUG_TAG, "Presenter destroyed because View destroyed. MvpView "
              + callback.getMvpView()
              + "   Presenter: "
              + presenter);
    }
    presenter.destroy();
    presenter = null;
    callback = null;
  }
}
