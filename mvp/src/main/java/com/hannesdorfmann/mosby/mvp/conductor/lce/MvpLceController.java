package com.hannesdorfmann.mosby.mvp.conductor.lce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bluelinelabs.conductor.Controller;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;
import com.hannesdorfmann.mosby.mvp.conductor.R;
import com.hannesdorfmann.mosby.mvp.conductor.delegate.MvpConductorLifecycleListener;
import com.hannesdorfmann.mosby.mvp.lce.LceAnimator;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * A {@link MvpController} that implements {@link MvpLceView} which gives you 3 options:
 * <ul>
 * <li>Display a loading view: A view with <b>R.id.loadingView</b> must be specified in your
 * inflated xml layout</li>
 * <li>Display a error view: A <b>TextView</b> with <b>R.id.errorView</b> must be declared in your
 * inflated xml layout</li>
 * <li>Display content view: A view with <b>R.id.contentView</b> must be specified in your
 * inflated xml layout</li>
 * </ul>
 *
 * <p>
 * If you want to use custom id's you can override {@link #getContentViewId()}, {@link
 * #getErrorViewId()} and {@link #getLoadingViewId()}
 * </p>
 *
 * @param <CV> The type of the content view with the id = R.id.contentView. Can be any kind of
 * android view widget like ListView, RecyclerView, ScrollView or a simple layout like Framelayout
 * etc. (everything that extends from android.view.View)
 * @param <M> The underlying data model that will be displayed with this view
 * @param <V> The View interface that must be implemented by this view. You can use {@link
 * MvpLceView}, but if you want to add more methods you have to provide your own view interface
 * that
 * extends from {@link MvpLceView}
 * @param <P> The type of the Presenter. Must extend from {@link MvpPresenter}
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class MvpLceController<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpController<V, P> implements MvpLceView<M> {

  protected View loadingView;
  protected CV contentView;
  protected TextView errorView;

  public MvpLceController() {
  }

  public MvpLceController(Bundle args) {
    super(args);
  }

  @Override protected LifecycleListener getMosbyLifecycleListener() {
    return new MvpConductorLifecycleListener<V, P>(this) {

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

  /**
   * Specifies which id should be used to determine the content view
   *
   * @return per default:  {@code R.id.contentView}
   */
  protected int getContentViewId() {
    return R.id.contentView;
  }

  /**
   * Specifies which id should be used to determine the error view
   *
   * @return per default: {@code R.id.errorView}
   */
  protected int getErrorViewId() {
    return R.id.errorView;
  }

  /**
   * Specifies which id should be used to determine the loading view
   *
   * @return per default:  {@code R.id.loadingView}
   */
  protected int getLoadingViewId() {
    return R.id.loadingView;
  }

  @Override public void showLoading(boolean pullToRefresh) {

    if (!pullToRefresh) {
      animateLoadingViewIn();
    }

    // otherwise the pull to refresh widget will already display a loading animation
  }

  /**
   * Override this method if you want to provide your own animation for showing the loading view
   */
  protected void animateLoadingViewIn() {
    LceAnimator.showLoading(loadingView, contentView, errorView);
  }

  @Override public void showContent() {
    animateContentViewIn();
  }

  /**
   * Called to animate from loading view to content view
   */
  protected void animateContentViewIn() {
    LceAnimator.showContent(loadingView, contentView, errorView);
  }

  /**
   * Get the error message for a certain Exception that will be shown on {@link
   * #showError(Throwable, boolean)}
   */
  protected abstract String getErrorMessage(Throwable e, boolean pullToRefresh);

  /**
   * The default behaviour is to display a toast message as light error (i.e. pull-to-refresh
   * error).
   * Override this method if you want to display the light error in another way (like crouton).
   */
  protected void showLightError(String msg) {
    if (getActivity() != null) {
      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * Called if the error view has been clicked. To disable clicking on the errorView use
   * <code>errorView.setClickable(false)</code>
   */
  protected void onErrorViewClicked() {
    loadData(false);
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {

    String errorMsg = getErrorMessage(e, pullToRefresh);

    if (pullToRefresh) {
      showLightError(errorMsg);
    } else {
      errorView.setText(errorMsg);
      animateErrorViewIn();
    }
  }

  /**
   * Animates the error view in (instead of displaying content view / loading view)
   */
  protected void animateErrorViewIn() {
    LceAnimator.showErrorView(loadingView, contentView, errorView);
  }
}
