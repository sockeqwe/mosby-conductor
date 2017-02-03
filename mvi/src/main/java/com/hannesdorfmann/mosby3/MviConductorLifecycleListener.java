package com.hannesdorfmann.mosby3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.mvi.MviPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import java.util.UUID;

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
 * @since 1.0
 */
public class MviConductorLifecycleListener<V extends MvpView, P extends MviPresenter<V, ?>>
    extends Controller.LifecycleListener {

  private static final boolean DEBUG = false;
  private static final String DEBUG_TAG = MviConductorLifecycleListener.class.getSimpleName();

  protected MviConductorDelegateCallback<V, P> callback;
  private static final String KEY_MOSBY_VIEW_ID = "com.hannesdorfmann.mosby3.activity.viewState.id";
  private String mosbyViewId = null;
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

  protected MviConductorDelegateCallback<V, P> getCallback() {
    return callback;
  }

  @Override public void onRestoreInstanceState(@NonNull Controller controller,
      @NonNull Bundle savedInstanceState) {

    if (keepPresenterInstance) {
      mosbyViewId = savedInstanceState.getString(KEY_MOSBY_VIEW_ID);
    }

    if (DEBUG) {
      Log.d(DEBUG_TAG, "MosbyView ID = " + mosbyViewId + " for MvpView: " + callback.getMvpView());
    }
  }

  @Override public void postCreateView(@NonNull Controller controller, @NonNull View view) {

    boolean viewStateWillBeRestored = false;

    if (mosbyViewId == null) {
      // No presenter available,
      // Activity is starting for the first time (or keepPresenterInstance == false)
      presenter = createViewIdAndCreatePresenter(controller);
      if (DEBUG) {
        Log.d(DEBUG_TAG, "new Presenter instance created: " + presenter);
      }
    } else {
      presenter = PresenterManager.getPresenter(controller.getActivity(), mosbyViewId);
      if (presenter == null) {
        // Process death,
        // hence no presenter with the given viewState id stored, although we have a viewState id
        presenter = createViewIdAndCreatePresenter(controller);
        if (DEBUG) {
          Log.d(DEBUG_TAG,
              "No Presenter instance found in cache, although MosbyView ID present. This was caused by process death, therefore new Presenter instance created: "
                  + presenter);
        }
      } else {
        viewStateWillBeRestored = true;
        if (DEBUG) {
          Log.d(DEBUG_TAG, "Presenter instance reused from internal cache: " + presenter);
        }
      }
    }

    // presenter is ready, so attach viewState
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
        || !controller.getActivity().isFinishing()) &&  !controller.isBeingDestroyed();
  }

  @Override public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
    boolean retainPresenterInstance = retainPresenterInstance(keepPresenterInstance, controller);
    presenter.detachView(retainPresenterInstance);

    if (DEBUG) {
      Log.d(DEBUG_TAG, "detached MvpView from Presenter. MvpView "
          + callback.getMvpView()
          + "   Presenter: "
          + presenter);
      Log.d(DEBUG_TAG, "Retaining presenter instance: "
          + Boolean.toString(retainPresenterInstance).toUpperCase()
          + " "
          + presenter);
    }
  }

  @Override public void postDestroy(@NonNull Controller controller) {

    if (!retainPresenterInstance(keepPresenterInstance, controller)) {
      PresenterManager.remove(controller.getActivity(), mosbyViewId);
    }

    presenter = null;
    callback = null;
  }

  @Override
  public void onSaveInstanceState(@NonNull Controller controller, @NonNull Bundle outState) {
    if (keepPresenterInstance) {
      outState.putString(KEY_MOSBY_VIEW_ID, mosbyViewId);
      if (DEBUG) {
        Log.d(DEBUG_TAG, "Saving MosbyViewId into Bundle. ViewId: " + mosbyViewId);
      }
    }
  }

  /**
   * Generates the unique (mosby internal) viewState id and calls {@link
   * MviDelegateCallback#createPresenter()}
   * to create a new presenter instance
   *
   * @return The new created presenter instance
   */
  private P createViewIdAndCreatePresenter(Controller controller) {

    P presenter = callback.createPresenter();
    if (presenter == null) {
      throw new NullPointerException(
          "Presenter returned from createPresenter() is null. Activity is "
              + controller.getActivity());
    }
    if (keepPresenterInstance) {
      mosbyViewId = UUID.randomUUID().toString();
      PresenterManager.putPresenter(controller.getActivity(), mosbyViewId, presenter);
    }
    return presenter;
  }
}
