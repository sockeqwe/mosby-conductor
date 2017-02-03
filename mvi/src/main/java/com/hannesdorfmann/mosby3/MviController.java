package com.hannesdorfmann.mosby3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.mvi.MviPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Base class you can use to extend from to have a Mosby MVP based controller.
 *
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class MviController<V extends MvpView, P extends MviPresenter<V, ?>>
    extends Controller implements MvpView, MviConductorDelegateCallback<V, P> {

  protected P presenter;

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
   * This method is for internal purpose only.
   * <p><b>Do not override this until you have a very good reason</b></p>
   *
   * @return Mosby's lifecycle listener so that
   */
  protected LifecycleListener getMosbyLifecycleListener() {
    return new MviConductorLifecycleListener<V, P>(this);
  }

  @NonNull @Override public P getPresenter() {
    return presenter;
  }

  @Override public void setPresenter(@NonNull P presenter) {
    this.presenter = presenter;
  }

  @NonNull @Override public V getMvpView() {
    return (V) this;
  }
}
