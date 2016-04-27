package com.hannesdorfmann.mosby.mvp.conductor.delegate;

import android.support.annotation.NonNull;
import android.view.View;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

/**
 * This {@link Controller.LifecycleListener} has to added to your Controller to integrate Mosby.
 * This class uses {@link MvpConductorDelegateCallback} to call Mosby's methods like {@code
 * createPresenter()} according to the {@link Controller} lifecycle events.
 *
 * <p>
 * {@link MvpController} already implements {@link MvpConductorDelegateCallback} and registers this
 * class as listener.
 * </p>
 *
 * @author Hannes Dorfmann
 * @see MvpConductorDelegateCallback
 * @see MvpController
 * @since 1.0
 */
public class MvpConductorLifecycleListener<V extends MvpView, P extends MvpPresenter<V>>
    extends Controller.LifecycleListener {

  protected final MvpConductorDelegateCallback<V, P> callback;

  /**
   * Instantiate a new Mosby MVP Listener
   *
   * @param callback {@link MvpConductorDelegateCallback} to set presenter. Typically the
   * controller
   * himself.
   */
  public MvpConductorLifecycleListener(MvpConductorDelegateCallback<V, P> callback) {
    this.callback = callback;
  }

  protected MvpConductorDelegateCallback<V, P> getCallback() {
    return callback;
  }


  @Override public void postCreateView(@NonNull Controller controller, @NonNull View view) {

    MvpConductorDelegateCallback<V, P> callback = getCallback();

    P presenter = callback.getPresenter();
    if (presenter == null) {
      presenter = callback.createPresenter();
      if (presenter == null) {
        throw new NullPointerException(
            "Presenter returned from createPresenter() is null in " + callback);
      }
      callback.setPresenter(presenter);
    }

    V mvpView = callback.getMvpView();
    if (mvpView == null) {
      throw new NullPointerException("MVP View returned from getMvpView() is null in " + callback);
    }
    presenter.attachView(mvpView);
  }

  @Override public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
    P presenter = getCallback().getPresenter();
    if (presenter == null) {
      throw new NullPointerException(
          "Presenter returned from getPresenter() is null in " + callback);
    }

    presenter.detachView(controller.getActivity().isChangingConfigurations());
  }
}
