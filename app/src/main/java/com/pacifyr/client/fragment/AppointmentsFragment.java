package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.AppointmentsListAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class AppointmentsFragment extends BaseFragment {

    private PrefManager mPrefManager;
    private String userToken, userid;
    private RecyclerView mRecyclerViewAppointments;
    private AppointmentsListAdapter mAppointmentsListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppointmentModel mAppointmentModel;
    private boolean isReschedule;
    private int reSchedulePosition;
    private AppointmentModel.Result mObject;
    private TextView emptyLayout;

    public int getReSchedulePosition() {
        return reSchedulePosition;
    }

    public void setReSchedulePosition(int reSchedulePosition) {
        this.reSchedulePosition = reSchedulePosition;
    }

    public boolean isReschedule() {
        return isReschedule;
    }

    public void setReschedule(boolean reschedule) {
        isReschedule = reschedule;
    }

    public AppointmentsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void getAppointmentsList() {

        try {
            setLoading();
            showProgress();
            mPrefManager = PrefManager.getInstance(getContext());
            String token = mPrefManager.getAccessToken();
            String url = GLOBAL_URL + "schedule/user/" + mPrefManager.getUserID() + "/bookings?skip=0&limit=100";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        if (isValid(json))
                        {
                            mAppointmentModel = GsonUtils.getInstance().gsonToAppointmentModel(json);
                            if (isValidSize(mAppointmentModel.getResults())) {
                                emptyLayout.setVisibility(View.GONE);
                                mRecyclerViewAppointments.setVisibility(View.VISIBLE);
                                setAppointmentListAdapter(mAppointmentModel.getResults());
                            }
                            else{
                                emptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerViewAppointments.setVisibility(View.GONE);
                            }


                        }
                        else
                        {
                            emptyLayout.setVisibility(View.VISIBLE);
                            mRecyclerViewAppointments.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        mRecyclerViewAppointments.setVisibility(View.GONE);
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
        }
    }


    private void setAppointmentListAdapter(List<AppointmentModel.Result> mList) {
        try {
            hideProgress();
            mAppointmentsListAdapter = new AppointmentsListAdapter(mList, getActivity(), new AppointmentsListAdapter.ScheduleCancelListner() {
                @Override
                public void onScheduleCancel(AppointmentModel.Result object, int position) {
                    mObject = object;
                    setReschedule(false);
                    if (isValid(mObject)) {

                        showBookedSlotCancel(mObject, position);
                    }
                }
            }, new AppointmentsListAdapter.ReSchedulelListner() {
                @Override
                public void onReshecdule(AppointmentModel.Result object, int position) {
                    setReSchedulePosition(position);
                    mObject = object;
                    setReschedule(true);
                    ShowReScheduleUI(mObject);
                }
            });

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerViewAppointments.setLayoutManager(mLayoutManager);
            mRecyclerViewAppointments.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewAppointments.setAdapter(mAppointmentsListAdapter);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }


    private void showBookedSlotCancel(AppointmentModel.Result mObject, int position) {

        ((PacifyrActivity) getActivity()).setAlertStatusInBlue("Cancel Appointment", "Do you want to cancel the Appointment ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PacifyrActivity) getActivity()).hideStatus();

                callCAncelBookedSlotApi(mObject,position);
            }
        });

    }

    private void ShowReScheduleUI(AppointmentModel.Result mObject) {

        if (isValid(mObject.getPacifyr())) {
            if (isValid(getPrefManager().getGENDER()) && isValid(getPrefManager().getZIP())) {
                ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("OBJECT", mObject.getPacifyr());
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
            }
            else
            {
                ((PacifyrAppCompactActivity)getActivity()).showMandatoryFieldsPopUp();
            }
        }



    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);
        mRecyclerViewAppointments = root.findViewById(R.id.mRecyclerViewAppointments);
        emptyLayout=root.findViewById(R.id.emptyLayout);
        getAppointmentsList();
        return root;
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

    public Boolean isValid(Object object) {
        return object != null;

    }


    private void callCAncelBookedSlotApi(AppointmentModel.Result mObject, int position) {

        if (isValid(mObject)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", mPrefManager.getUserID());

                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/slots/book/cancel/" + mObject.getId();
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    ((PacifyrActivity) getActivity()).setLoading();
                    ((PacifyrActivity) getActivity()).showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                ((PacifyrActivity) getActivity()).hideProgress();

                                if (isValid(json)) {
                                    String jsonS = json.toString();
                                    String status;
                                    String messsage = null;
                                    Log.w("QQQQ", jsonS);

                                    if (json.has("status")) {

                                        status = json.getString("status");
                                        messsage = status;
                                        if (json.has("message"))
                                            messsage = json.getString("message");

                                        if (isValid(status)) {


                                            if (status.equals("success")) {
                                                mAppointmentModel.getResults().remove(position);
                                                if (isReschedule()) {
                                                    ((PacifyrActivity) getActivity()).setStatus("Rescheduled successfully", R.color.md_green_400);
                                                    ((PacifyrActivity) getActivity()).showStatus();
                                                } else {
                                                    ((PacifyrActivity) getActivity()).setStatus(messsage, R.color.md_green_400);
                                                    ((PacifyrActivity) getActivity()).showStatus();
                                                }


                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyrActivity) getActivity()).hideStatus();

                                                    }
                                                }, 2000);
                                            } else {

                                                ((PacifyrActivity) getActivity()).setStatus(messsage, R.color.md_red_A700);
                                                ((PacifyrActivity) getActivity()).showStatus();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyrActivity) getActivity()).hideStatus();

                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            ((PacifyrActivity) getActivity()).setStatus("Something went wrong", R.color.md_red_A700);
                                            ((PacifyrActivity) getActivity()).showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    ((PacifyrActivity) getActivity()).hideStatus();

                                                }
                                            }, 2000);
                                        }

                                        getAppointmentsList();
                                    } else {
                                        ((PacifyrActivity) getActivity()).setStatus("Something went wrong !", R.color.md_red_A700);
                                        ((PacifyrActivity) getActivity()).showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ((PacifyrActivity) getActivity()).hideStatus();

                                            }
                                        }, 2000);
                                    }


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                ((PacifyrActivity) getActivity()).hideProgress();
                            }
                        }
                    };

                    AQuery aq = new AQuery(getActivity());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    aq.post(url, jsonObject, JSONObject.class, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {

            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onRescheduleDone(String event) {
        if (event.equals("NEWSCHEDULE")) {
            if (isReschedule()) {
                callCAncelBookedSlotApi(mObject, getReSchedulePosition());
            } else {
                getAppointmentsList();
            }

        }
    }
}
