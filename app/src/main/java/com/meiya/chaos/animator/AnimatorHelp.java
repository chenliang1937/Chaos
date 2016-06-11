package com.meiya.chaos.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by chenliang3 on 2016/5/19.
 */
public class AnimatorHelp {

    public static void btnClickAnim(View view, int duration){
        ObjectAnimator sacleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f, 1f);
        ObjectAnimator sacleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f, 1f);
        AnimatorSet animation = new AnimatorSet();
        animation.play(sacleX).with(sacleY);
        animation.setDuration(duration);
        animation.start();
    }

}
