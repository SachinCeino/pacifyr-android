package com.pacifyr.client.quickblox.chat.ui.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pacifyr.client.R;
import com.pacifyr.client.quickblox.chat.interfaces.AttachmentClick;

public class BottomAttachDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private ObjectView cam, gallery, doc;
    private boolean isCallActive;
    private RelativeLayout relativeLayout;
    private AttachmentClick attachmentClick;
    private LinearLayout parent;

    public BottomAttachDialog(boolean isCallActive, AttachmentClick attachmentClick) {
        this.isCallActive = isCallActive;
        this.attachmentClick = attachmentClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_attach_popup,
                container, false);
        //  v.setBackgroundResource(android.R.color.transparent);
        cam = v.findViewById(R.id.cam);
        gallery = v.findViewById(R.id.gallery);
        doc = v.findViewById(R.id.doc);
        relativeLayout= v.findViewById(R.id.attach_layout);
        parent= v.findViewById(R.id.popup_element);
        if (isCallActive) {
            cam.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cam.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            cam.setLayoutParams(params);
        } else {
            cam.setVisibility(View.VISIBLE);

        }
        parent.setOnClickListener(this);
        cam.setOnClickListener(this);
        gallery.setOnClickListener(this);
        doc.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);


        return v;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cam:
                attachmentClick.onClick(0);
                ;
                break;
            case R.id.gallery:
                attachmentClick.onClick(1);
                break;
            case R.id.doc:
                attachmentClick.onClick(2);
                break;
            case R.id.attach_layout:
                attachmentClick.onClick(-1);
                break;
            case R.id.popup_element:
                attachmentClick.onClick(-1);
                break;
        }
    }

}