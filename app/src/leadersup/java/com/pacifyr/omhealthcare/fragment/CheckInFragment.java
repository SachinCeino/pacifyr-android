package com.pacifyr.omhealthcare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CheckinAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.EmployerList;
import com.pacifyr.omhealthcare.model.CheckinModel;
import com.pacifyr.omhealthcare.model.InterviewModel;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.BaseFragment;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sachin Thampan on 18/10/19.
 */
public class CheckInFragment extends BaseFragment {

    private RecyclerView recycler_checkin;
    private ArrayList<CheckinModel> models;
    private CheckinAdapter adapter;
    private InterviewModel.Result pending;
    private LinearLayout employer_list;
    private View layout_congrats;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pending = getArguments() != null ?
                (InterviewModel.Result) getArguments().getSerializable("pending") : null;
    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkin, null);

        recycler_checkin = view.findViewById(R.id.recycler_checkin);
        employer_list = view.findViewById(R.id.layout_employerlist);
        layout_congrats = view.findViewById(R.id.layout_congrats);

        CheckinModel checkinModel = new CheckinModel(pending.getEventId(), false, "Resume Zone",
                CheckinModel.RESUME_ZONE, pending.getResumeZoneCompleted());
        CheckinModel checkinModel2 = new CheckinModel(pending.getEventId(), false, "Elevated Intro" +
                " Zone", CheckinModel.ELEVATED_ZONE, pending.getElevatedIntroZoneCompleted());
        CheckinModel checkinModel3 = new CheckinModel(pending.getEventId(), false, "Fresh Sesh",
                CheckinModel.FRESH_SESH, pending.getFreshSeshCompleted());
        CheckinModel checkinModel4 = new CheckinModel(pending.getEventId(), false, "Mock Interview",
                CheckinModel.MOCK_INTERVIEW, pending.getMockInterviewCompleted());
        CheckinModel checkinModel5 = new CheckinModel(pending.getEventId(), false, "Dress for Success",
                CheckinModel.DRESS_FOR_SUCCESS, pending.getDressForSuccessCompleted());
        models = new ArrayList<>();
        models.add(checkinModel);
        models.add(checkinModel2);
        models.add(checkinModel3);
        models.add(checkinModel4);
        models.add(checkinModel5);

        if (adapter == null)
            adapter = new CheckinAdapter(getActivity(), models);

        LinearLayoutManager manager
                = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recycler_checkin.setLayoutManager(manager);

        recycler_checkin.setAdapter(adapter);

        recycler_checkin.setItemAnimator(new DefaultItemAnimator());

        recycler_checkin.addItemDecoration(new DividerItemDecoration(getActivity(),
                RecyclerView.VERTICAL));

        if (adapter.isAllComplete()) {
            layout_congrats.setVisibility(View.VISIBLE);
        }
        employer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pending != null) {
                    getEventDetails(pending.getEventId());
                }
            }
        });
        return view;
    }

    public void getEventDetails(String eventID) {
        setLoading();
        showProgress();
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {

                    hideProgress();
                    if (isValid(json)) {
                        // Log.w("@@@@",String.valueOf(json));

                        ModelEvent data = GsonUtils.getInstance().gsonToModelEvent(json);
                        if (isValid(data)) {

                            List<ModelEvent.Employer> listEvent = data.getEmployers();
                            if (isValid(listEvent)) {
                                Intent intent = new Intent(getActivity(), EmployerList.class);
                                intent.putExtra("data", (Serializable) listEvent);
                                startActivity(intent);
                            }

                        }
                    } else {
                        if (isValid(status.getMessage())) {
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                    hideProgress();

                    e.printStackTrace();
                }
            }
        };
        getEventDetails(eventID, cb);
    }


}
