package com.hannesdorfmann.mosby3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.mvi.MviPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Base class you can use to extend from to have a Mosby MVI based controller.
 *
 * @author Hannes Dorfmann
 * @since 3.0
 */
public abstract class MviController<V extends MvpView, P extends MviPresenter<V, ?>>
    extends Controller implements MvpView, MviConductorDelegateCallback<V, P> {

  // Initializer block
  {
    addLifecycleListener(getMosbyLifecycleListener());
  }

  public MviController() {
  }

  public MviController(Bundle args) {
    super(args);
  }

  /**
   * Used to determine whether or not this controller is rendering the view (ViewState) again
   * because the view is reattached.
   */
  protected boolean isRestoringViewState = false;

  /**
   * This method is for internal purpose only.
   * <p><b>Do not override this until you have a very good reason</b></p>
   *
   * @return Mosby's MVI lifecycle listener
   */
  protected LifecycleListener getMosbyLifecycleListener() {
    return new MviConductorLifecycleListener<V, P>(this);
  }

  @NonNull @Override public V getMvpView() {
    try {
      return (V) this;
    } catch (ClassCastException e) {
      String msg =
          "Couldn't cast the View to the corresponding View interface. Most likely you have forgot to add \"Controller implements YourMvpViewInterface\".\"";
      Log.e(this.toString(), msg);
      throw new RuntimeException(msg, e);
    }
  }

  @Override public void setRestoringViewState(boolean restoringViewState) {
    isRestoringViewState = restoringViewState;
  }
}
