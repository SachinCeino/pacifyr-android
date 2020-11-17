package com.pacifyrapp.utilitylibrary;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kaopiz.kprogresshud.KProgressHUD;

public class Dialog extends BottomSheetDialogFragment {
    private String title,desc;
    onClick listener;
    ImageView imageView;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static Dialog newInstance(String title,String desc,onClick listener) {
        Dialog f = new Dialog();

        // Supply num input as an argument.
       f.title=title;
        f.desc=desc;
        f.listener=listener;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Pick a style based on the num.
        int style = 1, theme = 5;
        switch (style) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch (theme) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
      //  setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.alert_yes_no, container, false);

imageView=view.findViewById(R.id.image);
if(title.equals("Logout"))imageView.setVisibility(View.VISIBLE);

        CustomFontTextView title_dtxv = view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = view.findViewById(R.id.desc_dtxv);
        CustomFontTextView dismis_dtxv = view.findViewById(R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = view.findViewById(R.id.ok_dtxv);
      title_dtxv.setText(title);
        desc_dtxv.setText(desc);
        dismis_dtxv.setText(" No ");
        ok_dtxv.setText(" Yes ");
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAction(Dialog.this);
                dismiss();
            }
        });
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dismiss();


            }
        });
        return view;
    }
public interface onClick{
        public void onAction(Dialog d);
}

}
