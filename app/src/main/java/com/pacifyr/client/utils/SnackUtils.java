package com.pacifyr.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pacifyr.client.R;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;

public class SnackUtils {

    private static Snackbar snackBar;
    private static Context mContextsnackBar;

    @SuppressLint("WrongConstant")
    public static void show(View anchor, String message, Context mContext, LayoutInflater layoutInflater) {
        mContextsnackBar=mContext;
        if (snackBar != null && snackBar.isShown()) {
            snackBar.dismiss();
        }

        // create an instance of the snackbar
        snackBar= Snackbar.make(anchor, "", Snackbar.LENGTH_LONG);
        View customSnackView =layoutInflater.inflate(R.layout.custom_snackbar_view, null);
        snackBar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackBar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        TextView TextView_snackBarMessage=customSnackView.findViewById(R.id.TextView_snackBarMessage);
        TextView_snackBarMessage.setText(message);
        snackbarLayout.addView(customSnackView, 0);

        View view=snackBar.getView();
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity=Gravity.TOP;
        view.setLayoutParams(params);
        snackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);

        TextView_snackBarMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mContextsnackBar, DialogsActivity.class);
                mContextsnackBar.startActivity(intent1);
            }
        });

        snackBar.show();

    }
}
