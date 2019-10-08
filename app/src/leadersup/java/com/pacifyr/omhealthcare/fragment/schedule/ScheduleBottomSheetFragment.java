package com.pacifyr.omhealthcare.fragment.schedule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pacifyr.omhealthcare.Fragment.schedule.ScheduleData;
import com.pacifyr.omhealthcare.Fragment.schedule.Slot;
import com.pacifyr.omhealthcare.Fragment.schedule.WeekScheduleListAdapter;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

public class ScheduleBottomSheetFragment extends BottomSheetDialogFragment {
    MUser pacifyer_obj;

    public ScheduleBottomSheetFragment() {

    }


    private LinearLayout ll_next_week, ll_prev_week;
    private String[] data;
    private CustomFontTextView tv_week_display;
    private Calendar calendar;
    private RecyclerView rcv_week_schedule_, rcv_slote_schedule_;
    public PrefManager prefM;
    private int weekNumber = 0;
    private List<ScheduleData> WeekScheduleData = new ArrayList<ScheduleData>();
    private List<Slot> mSlotesData = new ArrayList<Slot>();


    private GridLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private WeekScheduleListAdapter mWeekScheduleListAdapter;
    private ScheduleTimeSlotesAdapter mScheduleTimeSlotesAdapter;
    private boolean isDateIsPassed = false;

    private ScheduleData TempScheduleData = new ScheduleData();
    private RelativeLayout noDataAvailable;

    boolean after, before, today, passedTime;

    public boolean isAfter() {
        return after;
    }

    public void setAfter(boolean after) {
        this.after = after;
    }

    public boolean isBefore() {
        return before;
    }

    public void setBefore(boolean before) {
        this.before = before;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isPassedTime() {
        return passedTime;
    }

    public void setPassedTime(boolean passedTime) {
        this.passedTime = passedTime;
    }

    private LinearLayout nextClick;
    private TextView txtv_next;
    private Slot SlotForNextClick = null;
    private RelativeLayout firstLayout;
    private RelativeLayout confirmLayout;
    private CircleImageView pacifyr_avatar;
    private TextView pacifyr_name, appointment_date_and_time;
    private RelativeLayout appointmentDoneLayout;
    private TextView confirmationMessageText;
    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.schedule_bottomsheet_dialogue, container, false);

        prefM = PrefManager.getInstance(getActivity());
        data = getCurrentWeek();
        ll_next_week = view.findViewById(R.id.ll_next_week);

        ll_prev_week = view.findViewById(R.id.ll_prev_week);

        rcv_week_schedule_ = view.findViewById(R.id.rcv_week_schedule_);
        rcv_slote_schedule_ = view.findViewById(R.id.rcv_slote_schedule_);
        noDataAvailable = view.findViewById(R.id.noDataAvailable);
        nextClick = view.findViewById(R.id.nextClick);
        nextClick.setVisibility(View.VISIBLE);
        txtv_next = view.findViewById(R.id.txtv_next);
        txtv_next.setText("Cancel");
        firstLayout = view.findViewById(R.id.firstLayout);
        confirmLayout = view.findViewById(R.id.confirmLayout);
        pacifyr_avatar = view.findViewById(R.id.pacifyr_avatar);
        pacifyr_name = view.findViewById(R.id.pacifyr_name);
        appointment_date_and_time = view.findViewById(R.id.appointment_date_and_time);
        appointmentDoneLayout = view.findViewById(R.id.appointmentDoneLayout);
        confirmationMessageText = view.findViewById(R.id.confirmationMessageText);


        tv_week_display = view.findViewById(R.id.tv_week_display);
        if (getArguments() != null) {
            pacifyer_obj = (MUser) getArguments().getSerializable("OBJECT");
        }


        if (isValid(data)) {
            String disaplay_date = getDisplayDateValue(data);
            if (isValid(disaplay_date))
                tv_week_display.setText(disaplay_date);

        }


        ll_prev_week.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                weekNumber--;
                if (isValid(data))
                    data = getPreviousWeek();

                String disaplay_date = getDisplayDateValue(data);

                if (isValid(disaplay_date))
                    tv_week_display.setText(disaplay_date);

                /** if previous wee is loaded
                 * the the current date is passed**/


                getWeakScheduleList();

            }
        });


        ll_next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weekNumber++;
                if (isValid(data))
                    data = getNextWeek();

                String disaplay_date = getDisplayDateValue(data);

                if (isValid(disaplay_date))
                    tv_week_display.setText(disaplay_date);
                getWeakScheduleList();
            }
        });

        getWeakScheduleList();


        nextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtv_next.getText().equals("Cancel")) {
                    dismiss();
                } else if (txtv_next.getText().equals("Next")) {
                    if (isValid(SlotForNextClick)) {
                        callAppoinmentConfirmationScreen(SlotForNextClick);
                        //callSlotBookApi(SlotForNextClick);
                    }
                } else if (txtv_next.getText().equals("Confirm")) {
                    if (isValid(SlotForNextClick)) {
                        callSlotBookApi(SlotForNextClick);
                    }

                } else if (txtv_next.getText().equals("Done")) {
                    dismiss();
                }
            }
        });


        return view;
    }


    private void callAppoinmentConfirmationScreen(Slot slotForNextClick) {
        firstLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);
        appointmentDoneLayout.setVisibility(View.GONE);
        Glide.with(getActivity())
                .load(GLOBAL_IMAGE_URL + pacifyer_obj.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        // progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_error_white)
                .dontTransform()
                .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                .into(pacifyr_avatar);

        pacifyr_name.setText(pacifyer_obj.getFirstName() + "" + pacifyer_obj.getLastName() + "");

        appointment_date_and_time.setText(Utility.getDateFormatOne(TempScheduleData.getDate() + "") + "," + Utility.convertTime(slotForNextClick.getStartTime()));

        txtv_next.setText("Confirm");


    }

    private void getWeakScheduleList() {

        ((PacifyerActivity) getActivity()).setLoading();
        ((PacifyerActivity) getActivity()).showProgress();

        String token = prefM.getAccessToken();

        String userid = prefM.getUserID();

        AjaxCallback cb = new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                try {


                    if (isValid(json)) {
                        ((PacifyerActivity) getActivity()).hideProgress();
                        Log.w("@@@@", String.valueOf(json));
                        WeekScheduleData = GsonUtils.getInstance().gsonToModelScheduleData(json);
                        if (isValid(WeekScheduleData)) {
                            ((PacifyerActivity) getActivity()).hideProgress();

                            noDataAvailable.setVisibility(View.GONE);
                            rcv_week_schedule_.setVisibility(View.VISIBLE);
                            rcv_slote_schedule_.setVisibility(View.VISIBLE);

                            setWeekScheduleAdapter(WeekScheduleData);
                        } else {
                            ((PacifyerActivity) getActivity()).hideProgress();
                            noDataAvailable.setVisibility(View.VISIBLE);
                            rcv_week_schedule_.setVisibility(View.GONE);
                            rcv_slote_schedule_.setVisibility(View.GONE);
                        }

                    } else {
                        ((PacifyerActivity) getActivity()).hideProgress();
                        if (isValid(status.getMessage())) {
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                    ((PacifyerActivity) getActivity()).hideProgress();

                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getActivity());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "schedule/byWeek?pacifyrId=" + pacifyer_obj.getId() + "&weekNumber=" + weekNumber, JSONArray.class, cb);

        } catch (Exception ex) {
            ((PacifyerActivity) getActivity()).hideProgress();

            ex.printStackTrace();
        }

    }

    public void getTheDateIsPassedOrNot(List<ScheduleData> dates) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        ArrayList<String> serverDates = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {

            Date d = null;
            try {
                d = input.parse(dates.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            Log.w("DATE", "" + formatted);
            serverDates.add(formatted);


        }

        if (isValid(serverDates)) {
            String dd = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            isDateIsPassed = !serverDates.contains(dd);
        }


    }

    private void setWeekScheduleAdapter(List<ScheduleData> weekScheduleData) {

        setToday(false);
        setBefore(false);
        setAfter(false);

        mWeekScheduleListAdapter = new WeekScheduleListAdapter(weekScheduleData, getActivity(), isDateIsPassed, new WeekScheduleListAdapter.ListAdapterListener() {
            @Override
            public void onDateClick(int position) {

                if (isValid(weekScheduleData.get(position))) {
                    ScheduleData singleItem = weekScheduleData.get(position);
                    TempScheduleData = singleItem;
                    if (isValid(singleItem)) {
                        webGetTimSlotes(singleItem);
                    }
                }


            }
        });

        mLayoutManager = new GridLayoutManager(getActivity(), 7, GridLayoutManager.VERTICAL, false);

        rcv_week_schedule_.setLayoutManager(mLayoutManager);

        rcv_week_schedule_.setAdapter(mWeekScheduleListAdapter);

        rcv_week_schedule_.setItemAnimator(new DefaultItemAnimator());
    }


    private void checkIsItCurrentDate(String date_value) {

        setToday(false);
        setBefore(false);
        setAfter(false);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date d = null;
        try {
            d = input.parse(date_value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);


        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = formatter.format(todayDate);


        if (todayString.equals(formatted)) {
            setToday(true);

            Log.w("WWWWW", "today");
        }

        Date enteredDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            enteredDate = sdf.parse(formatted);
        } catch (Exception ex) {
            // enteredDate will be null if date="287686";
        }
        Date currentDate = new Date();
        if (enteredDate.after(currentDate)) {
            setBefore(false);

        } else {
            setBefore(true);

        }


    }


    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list, int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }

    public Boolean isValidPos(List list, int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }

    public Boolean isValidNotNull(List list) {
        // if(list.size()>0)
        return list != null;

    }

    public Boolean isValidSize(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(int object) {
        return object > 0;

    }


    public String[] getCurrentWeek() {

        this.calendar = Calendar.getInstance();

        this.calendar.setFirstDayOfWeek(Calendar.MONDAY);

        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return getNextWeek();

    }

    public String[] getNextWeek() {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String[] days = new String[7];

        for (int i = 0; i < 7; i++) {

            days[i] = format.format(this.calendar.getTime());
            this.calendar.add(Calendar.DATE, 1);
        }

        return days;
    }

    public String[] getPreviousWeek() {

        this.calendar.add(Calendar.DATE, -14);

        return getNextWeek();
    }


    private String getDisplayDateValue(String[] data) {

        String displaydate = "";

        String first_date = data[0];

        String second_date = data[6];

        /**split date first **/

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

        Date dt1 = null;

        try {
            dt1 = format1.parse(first_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format2 = new SimpleDateFormat("dd");

        String first_dd = format2.format(dt1);

        /**split date second **/

        Date dt2 = null;

        try {
            dt2 = format1.parse(second_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format3 = new SimpleDateFormat("dd");

        String second_dd = format3.format(dt2);


        /**split month from first**/

        Date dt3 = null;

        try {
            dt3 = format1.parse(first_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format4 = new SimpleDateFormat("MMM");

        String month = format4.format(dt3);


        /**split month from second**/

        Date dt4 = null;
        try {
            dt4 = format1.parse(second_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format5 = new SimpleDateFormat("MMM");

        String month2 = format5.format(dt4);


        displaydate = first_dd + "-" + month + "--" + second_dd + "-" + month2;

        return displaydate;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    private void webGetTimSlotes(ScheduleData mObject) {

        ((PacifyerActivity) getActivity()).setLoading();
        ((PacifyerActivity) getActivity()).showProgress();
        PrefManager prefM = PrefManager.getInstance(getActivity());
        String token = prefM.getAccessToken();
        String id = "";
        if (isValid(mObject.getId())) {
            id = mObject.getId();
        } else {
            return;
        }

        AjaxCallback cb = new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                try {
                    ((PacifyerActivity) getActivity()).hideProgress();
                    if (isValid(json)) {
                        mSlotesData = GsonUtils.getInstance().gsonToModelSlotesModel(json);
                        List<Slot> availableSlotArray = new ArrayList<Slot>();

                        if (isValid(mSlotesData)) {
                            for (Slot item : mSlotesData) {
                                if (item.getAvailable() == true) {
                                    availableSlotArray.add(item);
                                }
                            }
                            mSlotesData.clear();
                            mSlotesData.addAll(availableSlotArray);
                            availableSlotArray.clear();
                            if (isValid(mSlotesData)) {
                                checkIsItCurrentDate(mObject.getDate());
                                setTimeSlotesAdapter(mSlotesData);
                            } else {
                                noDataAvailable.setVisibility(View.VISIBLE);
                                rcv_week_schedule_.setVisibility(View.VISIBLE);
                                rcv_slote_schedule_.setVisibility(View.GONE);
                            }


                        } else {
                            Toast.makeText(getActivity(), "No slot's Available!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (isValid(status.getMessage())) {
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    ((PacifyerActivity) getActivity()).hideProgress();
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(GLOBAL_URL + "schedule/slots/" + id, JSONArray.class, cb);

        } catch (Exception ex) {
            ((PacifyerActivity) getActivity()).hideProgress();
            ex.printStackTrace();
        }


    }

    private void setTimeSlotesAdapter(List<Slot> mSlotesData) {


        if (!isToday()) {
            if (isBefore()) {
                for (Slot slot : mSlotesData) {
                    slot.setHideSlote(true);

                }

            } else {
                for (Slot slot : mSlotesData) {
                    slot.setHideSlote(false);

                }
            }

        } else {


            for (int i = 0; i < mSlotesData.size(); i++) {
                long currentMill = getMilliSecondsTime(getCurrentTime());
                long servermilli = getMilliSecondsTime(mSlotesData.get(i).getStartTime());

                if (servermilli > currentMill) {
                    mSlotesData.get(i).setHideSlote(false);
                } else {
                    mSlotesData.get(i).setHideSlote(true);
                }
               /* Log.w("@@@currentMill", String.valueOf(currentMill));
                Log.w("@@@servermilli", String.valueOf(servermilli));*/


            }


        }


        noDataAvailable.setVisibility(View.GONE);
        rcv_week_schedule_.setVisibility(View.VISIBLE);
        rcv_slote_schedule_.setVisibility(View.VISIBLE);
        mScheduleTimeSlotesAdapter = new ScheduleTimeSlotesAdapter(getActivity(), mSlotesData, isDateIsPassed, new ScheduleTimeSlotesAdapter.SloteBookedListener() {
            @Override
            public void onSloteBooked(int position) {
                SlotForNextClick = null;
                if (isValid(mSlotesData) && isValid(mSlotesData.get(position))) {
                    Slot singleSlote = mSlotesData.get(position);

                    if (isValid(singleSlote)) {
                        SlotForNextClick = singleSlote;
                        //showSlotBookedConfirmationPopup(singleSlote);
                        if (singleSlote.getBooked()) {
                            SlotForNextClick = null;
                            if (isValid(singleSlote.getBookedUserId()) && isValid(prefM.getUserID())) {
                                if (singleSlote.getBookedUserId().equals(prefM.getUserID())) {
                                    showBookedSlotCancel(singleSlote);
                                }

                            }

                        } else {
                            txtv_next.setText("Next");
                            mScheduleTimeSlotesAdapter.notifyDataSetChanged();
                        }

                    }
                }

            }
        });
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rcv_slote_schedule_.setLayoutManager(mGridLayoutManager);
        rcv_slote_schedule_.setItemAnimator(new DefaultItemAnimator());
        rcv_slote_schedule_.setAdapter(mScheduleTimeSlotesAdapter);

    }

    private void showBookedSlotCancel(Slot singleSlote) {

        ((PacifyerActivity) getActivity()).setAlertStatusInBlue("Cancel the slot", "Do you want to cancel the slot ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PacifyerActivity) getActivity()).hideStatus();

                callCAncelBookedSlotApi(singleSlote);
            }
        });

    }


    private String getCurrentTime() {

        Calendar rightNow = Calendar.getInstance();
        int HOUR_OF_DAY = rightNow.get(Calendar.HOUR_OF_DAY);
        int MINUTE = rightNow.get(Calendar.MINUTE);
        String hr = String.valueOf(HOUR_OF_DAY);
        String min = "";

        if (MINUTE >= 30) {
            min = "30";
        } else {
            min = "00";
        }
        Log.w("QQQQQQ", hr + min);
        return hr + ":" + min;
    }


    private long getMilliSecondsTime(String time) {
        String[] values = time.split(":");

        int hours = Integer.parseInt(values[0]);
        int minutes = Integer.parseInt(values[1]);

        long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes));
        return milliseconds;
    }


    private void callCAncelBookedSlotApi(Slot singleSlote) {

        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());

                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/slots/book/cancel/" + singleSlote.getId();
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    ((PacifyerActivity) getActivity()).setLoading();
                    ((PacifyerActivity) getActivity()).showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                ((PacifyerActivity) getActivity()).hideProgress();

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
                                                ((PacifyerActivity) getActivity()).setStatus(messsage, R.color.md_green_400);
                                                ((PacifyerActivity) getActivity()).showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyerActivity) getActivity()).hideStatus();

                                                    }
                                                }, 2000);
                                            } else {
                                                ((PacifyerActivity) getActivity()).setStatus(messsage, R.color.md_red_A700);
                                                ((PacifyerActivity) getActivity()).showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyerActivity) getActivity()).hideStatus();

                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            ((PacifyerActivity) getActivity()).setStatus("Something went wrong", R.color.md_red_A700);
                                            ((PacifyerActivity) getActivity()).showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    ((PacifyerActivity) getActivity()).hideStatus();

                                                }
                                            }, 2000);
                                        }

                                        ((PacifyerActivity) getActivity()).showStatus();
                                    } else {
                                        ((PacifyerActivity) getActivity()).setStatus("Something went wrong !", R.color.md_red_A700);
                                        ((PacifyerActivity) getActivity()).showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ((PacifyerActivity) getActivity()).hideStatus();

                                            }
                                        }, 2000);
                                    }

                                    if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                ((PacifyerActivity) getActivity()).hideProgress();
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


    /**
     * popu for confirmtion api for slote boking api
     **/
 /*   private void showSlotBookedConfirmationPopup(Slot singleSlote) {

        ((PacifyerActivity)getActivity()).setAlertStatusInBlue("Book the slot", "Do you want to book the slot ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PacifyerActivity)getActivity()).hideStatus();

                callSlotBookApi(singleSlote);
            }
        });
    }*/
    private void callSlotBookApi(Slot singleSlote) {


        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());

                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/slots/book/" + singleSlote.getId();
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    ((PacifyerActivity) getActivity()).setLoading();
                    ((PacifyerActivity) getActivity()).showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                ((PacifyerActivity) getActivity()).hideProgress();

                                if (isValid(json)) {
                                    String jsonS = json.toString();
                                    String status;
                                    String messsage;
                                    Log.w("QQQQ", jsonS);

                                    if (json.has("status")) {

                                        status = json.getString("status");

                                        messsage = status;
                                        if (json.has("message"))
                                            messsage = json.getString("message");
                                        if (isValid(status)) {


                                            if (status.equals("success")) {
                                                appointmentDoneLayout.setVisibility(View.VISIBLE);
                                                confirmLayout.setVisibility(View.GONE);
                                                txtv_next.setText("Done");
                                                confirmationMessageText.setText("Your appointment is scheduled with "
                                                        + pacifyer_obj.getFirstName() + ""
                                                        + pacifyer_obj.getLastName() + "" + "on "
                                                        + Utility.getDateFormatOne(TempScheduleData.getDate()) + ","
                                                        + Utility.convertTime(singleSlote.getStartTime()));

                                                /*((PacifyerActivity)getActivity()).setStatus(messsage, R.color.md_green_400);
                                                ((PacifyerActivity)getActivity()).showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyerActivity)getActivity()).hideStatus();

                                                    }
                                                }, 2000);*/
                                            } else {
                                                ((PacifyerActivity) getActivity()).setStatus(messsage, R.color.md_red_A700);
                                                ((PacifyerActivity) getActivity()).showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyerActivity) getActivity()).hideStatus();

                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            ((PacifyerActivity) getActivity()).setStatus("Something went wrong", R.color.md_red_A700);
                                            ((PacifyerActivity) getActivity()).showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    ((PacifyerActivity) getActivity()).hideStatus();

                                                }
                                            }, 2000);
                                        }

                                        ((PacifyerActivity) getActivity()).showStatus();
                                    } else {
                                        ((PacifyerActivity) getActivity()).setStatus("Something went wrong !", R.color.md_red_A700);
                                        ((PacifyerActivity) getActivity()).showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ((PacifyerActivity) getActivity()).hideStatus();

                                            }
                                        }, 2000);
                                    }

                                    if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ((PacifyerActivity) getActivity()).hideProgress();
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

    private class MyBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                    ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    }
}
