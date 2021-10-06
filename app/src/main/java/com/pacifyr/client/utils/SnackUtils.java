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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;
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
        TextView TextView_snackBarName=customSnackView.findViewById(R.id.TextView_snackBarName);
        LinearLayout LinearLayout_container=customSnackView.findViewById(R.id.LinearLayout_container);

        String[] separated = message.split(":");
        if (separated[0]!=null)
        {
            TextView_snackBarName.setText(separated[0]+"");
        }
        if (separated[1]!=null)
        {
            TextView_snackBarMessage.setText(separated[1]+"");
        }
       // separated[0]; // this will contain "name"
       // separated[1]; // this will contain "message"
        snackbarLayout.addView(customSnackView, 0);

        View view=snackBar.getView();
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity=Gravity.TOP;
        view.setLayoutParams(params);
        snackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);

        LinearLayout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mContextsnackBar, DialogsActivity.class);
                mContextsnackBar.startActivity(intent1);
            }
        });

        snackBar.show();

    }
}
