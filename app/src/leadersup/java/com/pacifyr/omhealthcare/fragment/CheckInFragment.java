package com.pacifyr.omhealthcare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CheckinAdapter;
import com.pacifyr.omhealthcare.model.CheckinModel;
import com.pacifyr.omhealthcare.model.InterviewModel;

import java.util.ArrayList;

/**
 * Created by Sachin Thampan on 18/10/19.
 */
public class CheckInFragment extends Fragment {

    private RecyclerView recycler_checkin;
    private ArrayList<CheckinModel> models = new ArrayList<>();
    private CheckinAdapter adapter;
    private InterviewModel.Result pending;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pending = getArguments() != null ?
                (InterviewModel.Result) getArguments().getSerializable("pending") : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkin, null);

        recycler_checkin = view.findViewById(R.id.recycler_checkin);

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

        return view;
    }
}
