package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;


public class PrescriptionWhileCallFragment extends BaseFragment {


    TextView submit_ctxv;
    private String mPrescriptionFile;
    private String userId;

    private MainTextView submit_notes;
    private TextView etv_notes;
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public static String prescrption="";
    public PrescriptionWhileCallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_prescription_while_call, container, false);
        etv_notes = root.findViewById(R.id.etv_notes);
        Bundle bundle = getArguments();

        if (isValid(bundle.get("mPrescriptionFile"))) {
            mPrescriptionFile = bundle.get("mPrescriptionFile").toString() + "";
        }

        if (isValid(mPrescriptionFile) && !mPrescriptionFile.equals(""))
            etv_notes.setText(mPrescriptionFile);
        onSetActionBar(root);

        return root;

    }

    public void onSetActionBar(View root) {
        actionBarTopLlay = root.findViewById(R.id.action_bar_top_llay);
        bottomPanel = root.findViewById(R.id.bottom_panel);
        leftLlay = root.findViewById(R.id.left_llay);
        backImv = root.findViewById(R.id.back_imv);
        leftTxv = root.findViewById(R.id.left_txv);
        titleTxv = root.findViewById(R.id.title_txv);
        rightLlay = root.findViewById(R.id.right_llay);
        rightTxv = root.findViewById(R.id.right_txv);
        rightImv = root.findViewById(R.id.right_imv);
        submit_ctxv = root.findViewById(R.id.submit_ctxv);
        rightImv.setVisibility(View.GONE);
        rightTxv.setVisibility(View.GONE);
        rightLlay.setVisibility(View.GONE);
        leftTxv.setVisibility(View.GONE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "Prescription");
        rightTxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
        // backImv.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rightLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  submit_notes.performClick();
            }
        });


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


}
