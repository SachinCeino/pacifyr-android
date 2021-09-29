package com.pacifyr.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pacifyr.client.R;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;

public class SnackUtils {

    private static Snackbar snackBar;
    private static Context mContextsnackBar;

    @SuppressLint("WrongConstant")
    public static void show(View anchor, String message, Context mContext) {
        mContextsnackBar=mContext;
        if (snackBar != null && snackBar.isShown()) {
            snackBar.dismiss();
        }
        snackBar = Snackbar.make(anchor, message, Snackbar.LENGTH_LONG);
        snackBar.setBackgroundTint(anchor.getContext().getColor(R.color.bonus_green));
        snackBar.setTextColor(anchor.getContext().getColor(R.color.white));
        View view = snackBar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        // calculate actionbar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (anchor.getContext().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    anchor.getContext().getResources().getDisplayMetrics());
        }

        // set margin
        params.setMargins(0, 120, 0, 0);
        snackBar.getView().setVisibility(View.GONE);
        snackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);

        view.setLayoutParams(params);

        snackBar.setAction("View",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(mContextsnackBar, DialogsActivity.class);
                        mContextsnackBar.startActivity(intent1);
                    }
                });

        snackBar.show();

    }
}
