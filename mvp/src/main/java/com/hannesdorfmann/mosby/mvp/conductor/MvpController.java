package com.hannesdorfmann.mosby.mvp.conductor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.conductor.delegate.MvpConductorDelegateCallback;
import com.hannesdorfmann.mosby.mvp.conductor.delegate.MvpConductorLifecycleListener;

/**
 * Base class you can use to extend from to have a Mosby MVP based controller.
 *
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class MvpController<V extends MvpView, P extends MvpPresenter<V>> extends Controller
    implements MvpView, MvpConductorDelegateCallback<V, P> {

  protected P presenter;

  // Initializer block
  {
    addLifecycleListener(getMosbyLifecycleListener());
  }

  public MvpController() {
  }

  public MvpController(Bundle args) {
    super(args);
  }

  /**
   * This method is for internal purpose only.
   * <p><b>Do not override this until you have a very good reason</b></p>
   * @return Mosby's lifecycle listener so that
   */
  protected LifecycleListener getMosbyLifecycleListener() {
    return new MvpConductorLifecycleListener<V, P>(this);
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
