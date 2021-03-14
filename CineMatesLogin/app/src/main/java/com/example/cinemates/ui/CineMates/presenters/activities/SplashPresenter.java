package com.example.cinemates.ui.CineMates.presenters.activities;

import android.content.Intent;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.cinemates.databinding.ActivitySplashBinding;
import com.example.cinemates.ui.CineMates.views.activities.LoginActivity;
import com.example.cinemates.ui.CineMates.views.activities.SplashActivity;

public class SplashPresenter {
    private final SplashActivity splashActivity;


    public SplashPresenter(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    public void Transizione(ActivitySplashBinding binding){

        binding.motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                Intent intent = new Intent(splashActivity, LoginActivity.class);
                splashActivity.getActivity().startActivity(intent);
                splashActivity.finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }
}
