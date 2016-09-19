package com.example.tank.mytrimetpro.map;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.tank.mytrimetpro.R;

/**
 * Created by tank on 7/27/16.
 */

public class ImageClickedAnimation implements Animation.AnimationListener {

    private View mView;
    private int mDrawableId;
    private int mColorId;
    private int mTag;
    private AnimationUpdateImageViewCallback mAnimationUpdateImageViewCallback;
    private Context mContext;

    public ImageClickedAnimation(Context context, AnimationUpdateImageViewCallback animationUpdateImageViewCallback,
                                 View view, int drawableId, int colorId, int tag){
        mView = view;
        mDrawableId = drawableId;
        mColorId = colorId;
        mTag = tag;
        mAnimationUpdateImageViewCallback = animationUpdateImageViewCallback;
        mContext = context;
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        Animation imageExpandAnimation = AnimationUtils.loadAnimation(mContext, R.anim.image_click_expand);
        imageExpandAnimation.setAnimationListener(
                new ExpandViewAnimation());
        mView.startAnimation(imageExpandAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    private class ExpandViewAnimation implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            mAnimationUpdateImageViewCallback.updateImageView(mDrawableId, mColorId, mTag);
        }

        @Override
        public void onAnimationEnd(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }
}
