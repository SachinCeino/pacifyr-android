package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceino.utilitylibrary.activity.WebActivity;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OpeningDetailFragment extends BaseFragment {


    public OpeningDetailFragment() {

    }

    private Bundle bundle;
    ModelEvent mModelEvent;
    private TextView txtv_opening_location,
            txtv_numberOf_openings, txtv_position_title,
            txtv_salary, txtv_company_website;
    private ModelEvent.Position object;
    private LinearLayout layout_bennefits, layout_qualification, layout_description;
    private RecyclerView rcv_attending_company;
    private RecyclerView.LayoutManager mLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_opening, container, false);

        txtv_position_title = view.findViewById(R.id.txtv_position_title);
        txtv_opening_location = view.findViewById(R.id.txtv_opening_location);
        txtv_numberOf_openings = view.findViewById(R.id.txtv_numberOf_openings);
        txtv_salary = view.findViewById(R.id.txtv_salary);
        txtv_company_website = view.findViewById(R.id.txtv_company_website);

        layout_bennefits = view.findViewById(R.id.layout_bennefits);
        layout_qualification = view.findViewById(R.id.layout_qualification);

        String s = "401(k) Pension Plan.,Dental Insurance,Life and AD&D Insurance.,Maternity Benefits.," +
                "Medical Insurance.,Paid Vacation.,Paternity Benefits.";

        Bundle bundle = getArguments();
        if (isValid(bundle) && isValid(bundle.getSerializable("object")))
            object = (ModelEvent.Position) bundle.getSerializable("object");


        if (isValid(object)) {
            txtv_position_title.setText(object.getTitle());
            txtv_opening_location.setText(":  " + object.getLocation());
            txtv_numberOf_openings.setText(":  " + object.getNumberOfPositions());
            txtv_salary.setText(":  " + object.getSalary());
            txtv_company_website.setText(object.getWebsite());

            if (isValid(object.getBenefits()))
                addBenefitsView(object.getBenefits());

            if (isValid(object.getQualifications()))
                addQualificationView(object.getQualifications());

            if (isValid(object.getBenefits()))
                addBenefitsView(object.getBenefits());
        }


        txtv_company_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUrl=txtv_company_website.getText().toString();
                if (!inputUrl.contains("http://"))
                   inputUrl = "http://" + inputUrl;
                WebActivity.startWebActivity(getActivity(), inputUrl + "");
            }
        });

        return view;
    }


    private void addQualificationView(List<String> stringData) {

        for (String item : stringData) {
            TextView tv = new com.pacifyr.omhealthcare.events.views.EventTextViewMedium(getActivity());
            tv.setPadding(3, 3, 3, 3);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_single_dot, 0, 0, 0);
            tv.setCompoundDrawablePadding(3);
            tv.setText(item);
            layout_qualification.addView(tv);

        }
    }

    private void addBenefitsView(List<String> stringData) {
        for (String item : stringData) {
            TextView tv = new com.pacifyr.omhealthcare.events.views.EventTextViewMedium(getActivity());
            tv.setPadding(3, 3, 3, 3);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_single_dot, 0, 0, 0);
            tv.setCompoundDrawablePadding(3);
            tv.setText(item);
            layout_bennefits.addView(tv);

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((EventActivity) getActivity()).changeTitle("Position");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((EventActivity) getActivity()).changeTitle("Attending Employer");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void getWebData() {


    }


}
