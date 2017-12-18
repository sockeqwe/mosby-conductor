package com.hannesdorfmann.mosby3.mvp.conductor.delegate;

import android.app.Activity;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Hannes Dorfmann
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(Controller.class)
public class MvpConductorLifecycleListenerTest {

  @Test public void createAndSetNewPresenterAndAttachView() {

    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = Mockito.mock(Controller.class);
    View view = Mockito.mock(View.class);
    MvpView mvpView = Mockito.mock(MvpView.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    Mockito.when(callback.getPresenter()).thenReturn(null);
    Mockito.when(callback.createPresenter()).thenReturn(presenter);
    Mockito.when(callback.getMvpView()).thenReturn(mvpView);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    lifecycleListener.postCreateView(controller, view);

    Mockito.verify(callback, Mockito.times(1)).getPresenter();
    Mockito.verify(callback, Mockito.times(1)).createPresenter();
    Mockito.verify(callback, Mockito.times(1)).setPresenter(presenter);
    Mockito.verify(presenter, Mockito.times(1)).attachView(mvpView);
  }

  @Test public void nullPointerExceptionOnCreatePresenter() {

    Controller controller = Mockito.mock(Controller.class);
    View view = Mockito.mock(View.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    Mockito.when(callback.getPresenter()).thenReturn(null);
    Mockito.when(callback.createPresenter()).thenReturn(null);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    try {
      lifecycleListener.postCreateView(controller, view);
      Assert.fail("NullpointerException expected");
    } catch (NullPointerException e) {
      Assert.assertEquals(e.getMessage(),
          "Presenter returned from createPresenter() is null in " + callback);
    }
  }

  @Test public void nullPointerExceptionOnGetMvpView() {

    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = Mockito.mock(Controller.class);
    View view = Mockito.mock(View.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    Mockito.when(callback.getPresenter()).thenReturn(presenter);
    Mockito.when(callback.getMvpView()).thenReturn(null);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    try {
      lifecycleListener.postCreateView(controller, view);
      Assert.fail("NullpointerException expected");
    } catch (NullPointerException e) {
      Assert.assertEquals(e.getMessage(),
          "MVP View returned from getMvpView() is null in " + callback);
    }
  }

  @Test public void dontCreatePresenterIfOneExists() {
    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = Mockito.mock(Controller.class);
    View view = Mockito.mock(View.class);
    MvpView mvpView = Mockito.mock(MvpView.class);
    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    Mockito.when(callback.getPresenter()).thenReturn(presenter);
    Mockito.when(callback.getMvpView()).thenReturn(mvpView);

    lifecycleListener.postCreateView(controller, view);

    Mockito.verify(callback, Mockito.times(1)).getPresenter();
    Mockito.verify(callback, Mockito.never()).createPresenter();
    Mockito.verify(callback, Mockito.never()).setPresenter(presenter);
  }

  @Test public void attachDetachViewFromPresenterNotRetaining() {

    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = PowerMockito.mock(Controller.class);
    View view = Mockito.mock(View.class);
    MvpView mvpView = Mockito.mock(MvpView.class);
    Activity activity = Mockito.mock(Activity.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    Mockito.when(callback.getPresenter()).thenReturn(presenter);
    Mockito.when(callback.getMvpView()).thenReturn(mvpView);
    PowerMockito.when(controller.getActivity()).thenReturn(activity);
    Mockito.when(activity.isChangingConfigurations()).thenReturn(false);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    lifecycleListener.postCreateView(controller, view);

    // Attach
    Mockito.verify(presenter, Mockito.times(1)).attachView(mvpView);

    // Detach
    lifecycleListener.preDestroyView(controller, view);
    Mockito.verify(presenter, Mockito.times(1)).detachView();
  }

  @Test public void attachDetachReattachViewFromPresenterRetaining() {

    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = PowerMockito.mock(Controller.class);
    View view = Mockito.mock(View.class);
    MvpView mvpView = Mockito.mock(MvpView.class);
    Activity activity = Mockito.mock(Activity.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    Mockito.when(callback.getPresenter()).thenReturn(presenter);
    Mockito.when(callback.getMvpView()).thenReturn(mvpView);
    PowerMockito.when(controller.getActivity()).thenReturn(activity);
    Mockito.when(activity.isChangingConfigurations()).thenReturn(true);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    lifecycleListener.postCreateView(controller, view);

    // Attach
    Mockito.verify(presenter, Mockito.times(1)).attachView(mvpView);

    // Detach
    lifecycleListener.preDestroyView(controller, view);
    Mockito.verify(presenter, Mockito.times(1)).detachView();

    // Reattach
    lifecycleListener.postCreateView(controller, view);
    Mockito.verify(presenter, Mockito.times(2)).attachView(mvpView);
  }

  @Test public void nullPointerExceptionGetPresenter() {
    MvpPresenter<MvpView> presenter = Mockito.mock(MvpPresenter.class);
    Controller controller = Mockito.mock(Controller.class);
    View view = Mockito.mock(View.class);

    MvpConductorDelegateCallback<MvpView, MvpPresenter<MvpView>> callback =
        Mockito.mock(MvpConductorDelegateCallback.class);

    MvpConductorLifecycleListener<MvpView, MvpPresenter<MvpView>> lifecycleListener =
        new MvpConductorLifecycleListener<>(callback);

    Mockito.when(callback.getPresenter()).thenReturn(null);

    try {
      lifecycleListener.preDestroyView(controller, view);
      Assert.fail();
    } catch (NullPointerException e) {
      Assert.assertEquals(e.getMessage(),
          "Presenter returned from getPresenter() is null in " + callback);
    }
  }
}
