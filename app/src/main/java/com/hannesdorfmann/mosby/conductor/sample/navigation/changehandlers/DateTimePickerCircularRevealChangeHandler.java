package com.hannesdorfmann.mosby.conductor.sample.navigation.changehandlers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler;
import com.hannesdorfmann.mosby.conductor.sample.R;

/**
 * An {@link AnimatorChangeHandler} that will perform a circular reveal
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class DateTimePickerCircularRevealChangeHandler
    extends AnimatorChangeHandler {

  public DateTimePickerCircularRevealChangeHandler() {
    super(DEFAULT_ANIMATION_DURATION, false);
  }

  private Pair<Integer, Integer> getViewPosition(View fromView, View containerView) {
    int[] fromLocation = new int[2];
    fromView.getLocationInWindow(fromLocation);

    int[] containerLocation = new int[2];
    containerView.getLocationInWindow(containerLocation);

    int relativeLeft = fromLocation[0] - containerLocation[0];
    int relativeTop = fromLocation[1] - containerLocation[1];

    int mCx = relativeLeft + 20;
    int mCy = fromView.getHeight() / 2 + relativeTop;
    return Pair.create(mCx, mCy);
  }

  @Override
  protected Animator getAnimator(@NonNull ViewGroup container, View from, View to, boolean isPush,
      boolean toAddedToContainer) {

    Animator animator = null;
    if (isPush && to != null) {

      Pair<Integer, Integer> coordinates = getViewPosition(from.findViewById(R.id.date), from);
      int mCx = coordinates.first;
      int mCy = coordinates.second;

      final float radius = (float) Math.hypot(mCx, mCy);

      // Background animation
      ObjectAnimator backgroundDimAnimator =
          ObjectAnimator.ofPropertyValuesHolder(to.getBackground(),
              PropertyValuesHolder.ofInt("alpha", 0, 180));

      AnimatorSet as = new AnimatorSet();
      as.playTogether(
          ViewAnimationUtils.createCircularReveal(to.findViewById(R.id.contentView), mCx, mCy, 0,
              radius), backgroundDimAnimator);

      animator = as;
    } else if (!isPush && from != null) {

      Pair<Integer, Integer> coordinates = getViewPosition(to.findViewById(R.id.date), to);
      int mCx = coordinates.first;
      int mCy = coordinates.second;

      final float radius = (float) Math.hypot(mCx, mCy);

      ObjectAnimator backgroundDimAnimator =
          ObjectAnimator.ofPropertyValuesHolder(from.getBackground(),
              PropertyValuesHolder.ofInt("alpha", 180, 0));

      Animator circular =
          ViewAnimationUtils.createCircularReveal(from.findViewById(R.id.contentView), mCx, mCy,
              radius, 0);

      AnimatorSet as = new AnimatorSet();
      as.playTogether(backgroundDimAnimator, circular);

      animator = as;
    }
    return animator;
  }

  @Override protected void resetFromView(@NonNull View from) {
  }
}
