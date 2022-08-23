package com.parneet.plotline;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CustomSnacks {

    private Snackbar snackbar;

    public CustomSnacks() { }

    public void successSnack(View view, String msg) {
        SpannableStringBuilder builder = new SpannableStringBuilder().append(msg);
        builder.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.success_text)), 0, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackbar = Snackbar.make(view,builder, BaseTransientBottomBar.LENGTH_LONG);
        View viewSnack = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewSnack.getLayoutParams();
        params.gravity = Gravity.TOP;
        viewSnack.setLayoutParams(params);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(view.getResources().getColor(R.color.success_back));
        snackbar.show();
    }

    public void failSnack(View view, String msg) {
        SpannableStringBuilder builder = new SpannableStringBuilder().append(msg);
        builder.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.failed_text)), 0, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackbar = Snackbar.make(view,builder, BaseTransientBottomBar.LENGTH_LONG);
        View viewSnack = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewSnack.getLayoutParams();
        params.gravity = Gravity.TOP;
        viewSnack.setLayoutParams(params);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(view.getResources().getColor(R.color.failed_back));
        snackbar.show();
    }

    public void infoSnack(View view, String msg) {
        SpannableStringBuilder builder = new SpannableStringBuilder().append(msg);
        builder.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.info_text)), 0, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackbar = Snackbar.make(view,builder, BaseTransientBottomBar.LENGTH_LONG);
        View viewSnack = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewSnack.getLayoutParams();
        params.gravity = Gravity.TOP;
        viewSnack.setLayoutParams(params);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(view.getResources().getColor(R.color.info_back));
        snackbar.show();
    }

    public void warnSnack(View view, String msg) {
        SpannableStringBuilder builder = new SpannableStringBuilder().append(msg);
        builder.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.warn_text)), 0, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackbar = Snackbar.make(view,builder, BaseTransientBottomBar.LENGTH_LONG);
        View viewSnack = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewSnack.getLayoutParams();
        params.gravity = Gravity.TOP;
        viewSnack.setLayoutParams(params);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(view.getResources().getColor(R.color.warn_back));
        snackbar.show();
    }
}
