package com.pacifyr.client.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.model.AppoinmentNotification;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDialogFragment extends DialogFragment implements Serializable {

    AppoinmentNotification mAppointmentModel;
    static MyDialogFragment mMyDialogFragment=null;
    public static MyDialogFragment newInstance()
    {
        if (mMyDialogFragment==null)
        {
            return mMyDialogFragment=new MyDialogFragment();
        }
        else
        {
           return mMyDialogFragment;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View customLayout=inflater.inflate(R.layout.appoinment_call_popup_from_notification, container, false);;

        TextView textView_intro=customLayout.findViewById(R.id.textView_intro);
        TextView mTextView_PacifyrName=customLayout.findViewById(R.id.mTextView_PacifyrName);
        CircleImageView pacifyr_profileImage=customLayout.findViewById(R.id.pacifyr_profileImage);
        TextView mTextView_ScheduleTime=customLayout.findViewById(R.id.mTextView_ScheduleTime);
        TextView TextView_initiateCallInfo =customLayout.findViewById(R.id.TextView_initiateCallInfo);
        Button Button_Dismiss  =customLayout.findViewById(R.id.Button_Dismiss);
        RelativeLayout audiocall_rlay = customLayout.findViewById(R.id.audiocall_rlay);
        RelativeLayout videocall_rlay = customLayout.findViewById(R.id.videocall_rlay);
        RelativeLayout chat_rlay = customLayout.findViewById(R.id.chat_rlay);

        if (isValid(getArguments()) && isValid(getArguments().getSerializable("object")))
        {
            mAppointmentModel= (AppoinmentNotification) getArguments().getSerializable("object");
        }

        if (!isValid(mAppointmentModel))
        {
            mMyDialogFragment.dismiss();
        }

        SpannableStringBuilder spanTxt = new SpannableStringBuilder("Please visit your profile page and upload your identity document (PAN/Aadhaar/EPIC/DL) if you have not done so.\n\n");
        spanTxt.append(Html.fromHtml("<font color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + "Steps to follow:" + "</b></font>"));
        spanTxt.append("\n\n1. Click on the Audio or Video button to connect to the Provider.\n");
        spanTxt.append("2. Introduction by yourself and the Provider.\n");
        spanTxt.append("3. You will receive a ");
        spanTxt.append(Html.fromHtml("<font size=14 color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + " Session Started" + "<b></font>"));
        spanTxt.append(" message.");
        textView_intro.setText(spanTxt, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder spanTxt2 = new SpannableStringBuilder("If you click on dismiss you can initiate this call from ");
        spanTxt2.append(Html.fromHtml("<font size=14 color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + " MY APPOINTMENTS" + "<b></font>"));
        TextView_initiateCallInfo.setText(spanTxt2, TextView.BufferType.SPANNABLE);
        // create and show the alert dialog

        Button_Dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDialogFragment.dismiss();
            }
        });

        if (isValid(mAppointmentModel))
        {
            if (isValid(mAppointmentModel.getPacifyr()))
            {
                isValid(mAppointmentModel.getPacifyr().getFirstName());
                {
                    String name="";
                    name=mAppointmentModel.getPacifyr().getFirstName();
                    if (isValid(mAppointmentModel.getPacifyr().getLastName()))
                    {
                        name=mAppointmentModel.getPacifyr().getFirstName()+" "+mAppointmentModel.getPacifyr().getLastName();
                    }

                    mTextView_PacifyrName.setText(name);
                }

                if (isValid(mAppointmentModel.getPacifyr().getImage()))
                {
                    String imgUrl= mAppointmentModel.getPacifyr().getImage();
                    if (isValid(imgUrl))
                        imgUrl= UtilityNetworkService.GLOBAL_IMAGE_URL + imgUrl;

                    if (isValidhtml(imgUrl))
                        Picasso.with(getActivity()).load(imgUrl).placeholder(com.pacifyrapp.utilitylibrary.R.drawable.placeholder_user).into(pacifyr_profileImage);

                }


                audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((PacifyrActivity)getActivity()).startSessionWebforAppoinmetCall(mAppointmentModel.getPacifyr(), false,mAppointmentModel.getId());
                        mMyDialogFragment.dismiss();
                    }
                });

                videocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((PacifyrActivity)getActivity()).startSessionWebforAppoinmetCall(mAppointmentModel.getPacifyr(), true,mAppointmentModel.getId());
                        mMyDialogFragment.dismiss();
                    }
                });

                chat_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((PacifyrActivity)getActivity()).startChat(mAppointmentModel.getPacifyr());
                        mMyDialogFragment.dismiss();
                    }
                });

            }


            if (isValid(mAppointmentModel.getStartTimeGMT()))
            {
                String mDisplayDateAndTime = Utility.getStartFormatDate(mAppointmentModel.getStartTimeGMT(), getActivity());
                mTextView_ScheduleTime.setText(mDisplayDateAndTime+"");
            }

        }

        return customLayout;

    }

    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    public Boolean isValid(String text) {
        if (text != null)
            return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }

    public Boolean isListSize(List list,int size) {
        if (isValid(list))
            return list.size() == size;
        return false;

    }

    public Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;
    }

    public Boolean isValid(String[] list,int size) {

        if (isValid(list))
            return list.length >= size;
        return false;

    }

    public Boolean isValidNotNull(List list) {
        // if(list.size()>0)
        return list != null;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }


}
