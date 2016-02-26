package com.arraybit.global;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import jp.wasabeef.recyclerview.animators.adapters.AnimationAdapter;

public class SlideInBottomAnimatorAdapter extends AnimationAdapter{

    public SlideInBottomAnimatorAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }
}
