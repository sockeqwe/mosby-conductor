package com.hannesdorfmann.mosby.conductor.sample.navigation.changehandlers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler;
import com.hannesdorfmann.mosby.conductor.sample.R;

/**
 * An {@link AnimatorChangeHandler} that will perform a circular reveal
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class CreateTaskCircularRevealChangeHandler
    extends AnimatorChangeHandler {

  public CreateTaskCircularRevealChangeHandler() {
    super(DEFAULT_ANIMATION_DURATION, true);
  }

  private Pair<Integer, Integer> getFabPosition(View fromView, View containerView) {
    int[] fromLocation = new int[2];
    fromView.getLocationInWindow(fromLocation);

    int[] containerLocation = new int[2];
    containerView.getLocationInWindow(containerLocation);

    int relativeLeft = fromLocation[0] - containerLocation[0];
    int relativeTop = fromLocation[1] - containerLocation[1];

    int mCx = fromView.getWidth() / 2 + relativeLeft;
    int mCy = fromView.getHeight() / 2 + relativeTop;
    return Pair.create(mCx, mCy);
  }

  @Override
  protected Animator getAnimator(@NonNull ViewGroup container, View from, View to, boolean isPush,
      boolean toAddedToContainer) {


    Animator animator = null;
    if (isPush && to != null) {

      Pair<Integer, Integer> coordinates = getFabPosition(from.findViewById(R.id.addTask), from);
      int mCx = coordinates.first;
      int mCy = coordinates.second;

      final float radius = (float) Math.hypot(mCx, mCy);

      AnimatorSet as = new AnimatorSet();
      final View toolbar = to.findViewById(R.id.toolbar);
      final View scrollView = to.findViewById(R.id.scrollView);

      toolbar.setVisibility(View.INVISIBLE);
      scrollView.setVisibility(View.INVISIBLE);

      Animator toolbarAnim =
          ObjectAnimator.ofFloat(toolbar, "translationY", -toolbar.getHeight(), 0);
      toolbarAnim.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationStart(Animator animation) {
          toolbar.setVisibility(View.VISIBLE);
        }
      });

      Animator scrollViewAnim =
          ObjectAnimator.ofFloat(scrollView, "translationY", scrollView.getHeight(), 0);
      scrollViewAnim.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationStart(Animator animation) {
          scrollView.setVisibility(View.VISIBLE);
        }
      });

      AnimatorSet contentViewAnim = new AnimatorSet();
      contentViewAnim.playTogether(toolbarAnim, scrollViewAnim);
      contentViewAnim.setStartDelay(200);
      contentViewAnim.setInterpolator(new DecelerateInterpolator());

      as.playSequentially(ViewAnimationUtils.createCircularReveal(to, mCx, mCy, 0, radius),
          contentViewAnim);

      animator = as;
    } else if (!isPush && from != null) {

      Pair<Integer, Integer> coordinates = getFabPosition(to.findViewById(R.id.addTask), to);
      int mCx = coordinates.first;
      int mCy = coordinates.second;

      final float radius = (float) Math.hypot(mCx, mCy);

      AnimatorSet as = new AnimatorSet();

      final View toolbar = from.findViewById(R.id.toolbar);
      final View scrollView = from.findViewById(R.id.scrollView);

      toolbar.setVisibility(View.VISIBLE);
      scrollView.setVisibility(View.VISIBLE);

      Animator toolbarAnim =
          ObjectAnimator.ofFloat(toolbar, "translationY", 0, -toolbar.getHeight());
      toolbarAnim.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationStart(Animator animation) {
          toolbar.setVisibility(View.VISIBLE);
        }
      });

      Animator scrollViewAnim =
          ObjectAnimator.ofFloat(scrollView, "translationY", 0, scrollView.getHeight());
      scrollViewAnim.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationStart(Animator animation) {
          scrollView.setVisibility(View.VISIBLE);
        }
      });

      AnimatorSet contentViewAnim = new AnimatorSet();
      contentViewAnim.playTogether(toolbarAnim, scrollViewAnim);
      contentViewAnim.setInterpolator(new AccelerateInterpolator());

      Animator circular = ViewAnimationUtils.createCircularReveal(from, mCx, mCy, radius, 0);
      //circular.setStartDelay(200);

      as.playSequentially(contentViewAnim, circular);

      animator = as;
    }
    return animator;
  }

  @Override protected void resetFromView(@NonNull View from) {
  }

}
