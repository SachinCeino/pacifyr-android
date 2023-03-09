package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

import org.greenrobot.eventbus.Subscribe;


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

    public static String prescrption = "";
    private TextView text_header;

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
        text_header = root.findViewById(R.id.text_header);
        etv_notes.setMovementMethod(new ScrollingMovementMethod());
        Bundle bundle = getArguments();

        if (isValid(bundle.get("mPrescriptionFile"))) {
            mPrescriptionFile = bundle.get("mPrescriptionFile").toString() + "";
        }

        if (isValid(mPrescriptionFile) && !mPrescriptionFile.equals(""))
            etv_notes.setText(mPrescriptionFile);
        onSetActionBar(root);

        if (VideoActivity.mSessionDetailRoot != null) {
            String header = getString(R.string.doc_name) +
                    VideoActivity.mSessionDetailRoot.getUser().getFirstName()
                    + " " + VideoActivity.mSessionDetailRoot.getUser().getLastName() + "\n\n";
            header += getString(R.string.prescription_date) + Utility.getDateForDisplay(getActivity());
            text_header.setText(header);
        }

        return root;

    }

    public void onSetActionBar(View root) {
        leftLlay = root.findViewById(R.id.left_llay);
        titleTxv = root.findViewById(R.id.title_txv);
        defSetText(titleTxv, getResources().getString(R.string.prescription));
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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
