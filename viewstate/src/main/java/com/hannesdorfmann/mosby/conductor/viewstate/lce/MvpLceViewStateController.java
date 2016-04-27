package com.hannesdorfmann.mosby.conductor.viewstate.lce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;
import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorDelegateCallback;
import com.hannesdorfmann.mosby.conductor.viewstate.delegate.MvpViewStateConductorLifecycleListener;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.conductor.lce.MvpLceController;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

/**
 * {@link MvpLceController} with {@link ViewState} support
 *
 * @author Hannes Dorfmann
 * @see MvpLceController
 * @see MvpViewStateController
 * @since 1.0
 */
public abstract class MvpLceViewStateController<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpLceController<CV, M, V, P>
    implements MvpViewStateConductorDelegateCallback<V, P, LceViewState<M, V>> {

  protected LceViewState<M, V> viewState;
  protected boolean restoringViewState = false;

  @Override protected LifecycleListener getMosbyLifecycleListener() {
    return new MvpViewStateConductorLifecycleListener<V, P, LceViewState<M, V>>(this) {

      private void setupViewsIfNeeded(View view) {
        if (loadingView == null) {
          loadingView = view.findViewById(getLoadingViewId());
          contentView = (CV) view.findViewById(getContentViewId());
          errorView = (TextView) view.findViewById(getErrorViewId());

          if (loadingView == null) {
            throw new NullPointerException(
                "Loading view is null! Have you specified a loading view in your layout xml file?"
                    + " You have to give your loading View the id R.id.loadingView "
                    + "(or your custom id if you have overridden getLoadingViewId()");
          }

          if (contentView == null) {
            throw new NullPointerException(
                "Content view is null! Have you specified a content view in your layout xml file?"
                    + " You have to give your content View the id R.id.contentView"
                    + "(or your custom id if you have overridden getContentViewId()");
          }

          if (errorView == null) {
            throw new NullPointerException(
                "Error view is null! Have you specified a content view in your layout xml file?"
                    + " You have to give your error View the id R.id.errorView"
                    + "(or your custom id if you have overridden getErrorViewId()");
          }

          errorView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onErrorViewClicked();
            }
          });
        }
      }

      @Override public void onRestoreViewState(@NonNull Controller controller,
          @NonNull Bundle savedViewState) {
        setupViewsIfNeeded(controller.getView());
        super.onRestoreViewState(controller, savedViewState);
      }

      @Override public void postCreateView(@NonNull Controller controller, @NonNull View view) {
        setupViewsIfNeeded(view);
        super.postCreateView(controller, view);
      }

      @Override public void postDestroyView(@NonNull Controller controller) {
        super.postDestroyView(controller);
        loadingView = null;
        contentView = null;
        errorView.setOnClickListener(null);
        errorView = null;
      }
    };
  }

  @Override public boolean isRestoringViewState() {
    return restoringViewState;
  }

  @Override public void setRestoringViewState(boolean restoringViewState) {
    this.restoringViewState = restoringViewState;
  }

  @Override public void setViewState(LceViewState<M, V> viewState) {
    this.viewState = viewState;
  }

  @Override public LceViewState<M, V> getViewState() {
    return viewState;
  }

  @Override public void showContent() {
    super.showContent();
    viewState.setStateShowContent(getData());
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    viewState.setStateShowError(e, pullToRefresh);
  }

  @Override public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    viewState.setStateShowLoading(pullToRefresh);
  }

  @Override protected void showLightError(String msg) {
    if (isRestoringViewState()) {
      return; // Do not display toast again while restoring viewstate
    }

    super.showLightError(msg);
  }

  @Override public void onViewStateInstanceRestored(boolean instanceStateRetained) {
    // Not needed in general. override it in subclass if you need this callback
  }

  @Override public void onNewViewStateInstance() {
    loadData(false);
  }

  /**
   * Get the data that has been set before in {@link #setData(Object)}
   * <p>
   * <b>It's necessary to return the same data as set before to ensure that {@link ViewState} works
   * correctly</b>
   * </p>
   *
   * @return The data
   */
  public abstract M getData();
}
