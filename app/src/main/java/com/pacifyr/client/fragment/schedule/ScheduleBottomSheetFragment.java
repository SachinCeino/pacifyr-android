package com.pacifyr.client.fragment.schedule;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.PaymentActivity;
import com.pacifyr.client.model.BillingSummary;
import com.pacifyr.client.model.SchedulePayment;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.utils.Consts;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.CustomFontTextView;
import com.pacifyrapp.utilitylibrary.Dialog;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.pacifyr.client.base.PacifyrActivity.errorCount;
import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.utils.Constants.PAYMENT_CARD_ADDED;
import static com.pacifyr.client.utils.Constants.PAYMENT_CARD_ADDED_FINISH;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ScheduleBottomSheetFragment extends BottomSheetDialogFragment {
    MUser pacifyer_obj;
    private TextView text_loading;

    public ScheduleBottomSheetFragment() {

    }
    public KProgressHUD hud_progress = null;
    public KProgressHUD hud_status = null;

    private LinearLayout ll_next_week, ll_prev_week;
    private String[] data;
    private MainTextView tv_week_display;
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

    private RelativeLayout nextClick;
    private TextView txtv_next,txtv_back;
    private Slot SlotForNextClick = null;
    private RelativeLayout firstLayout;
    private RelativeLayout confirmLayout;
    private CircleImageView pacifyr_avatar;
    private TextView pacifyr_name, appointment_date_and_time;
    private RelativeLayout appointmentDoneLayout;
    private TextView confirmationMessageText;
    BottomSheetBehavior mBottomSheetBehavior;
    LinearLayout l1;
    private EditText message_comments;
    private String bookingRequestPaymentId =null;




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
        text_loading = view.findViewById(R.id.text_loading);
        rcv_slote_schedule_ = view.findViewById(R.id.rcv_slote_schedule_);
        noDataAvailable = view.findViewById(R.id.noDataAvailable);
        nextClick = view.findViewById(R.id.nextClick);
        nextClick.setVisibility(View.VISIBLE);
        txtv_next = view.findViewById(R.id.txtv_next);
        txtv_back = view.findViewById(R.id.txtv_back);
        txtv_next.setText("Cancel");
        txtv_next.setVisibility(GONE);
        firstLayout = view.findViewById(R.id.firstLayout);
        confirmLayout = view.findViewById(R.id.confirmLayout);
        pacifyr_avatar = view.findViewById(R.id.pacifyr_avatar);
        pacifyr_name = view.findViewById(R.id.pacifyr_name);
        appointment_date_and_time = view.findViewById(R.id.appointment_date_and_time);
        appointmentDoneLayout = view.findViewById(R.id.appointmentDoneLayout);
        confirmationMessageText = view.findViewById(R.id.confirmationMessageText);
        message_comments=view.findViewById(R.id.message_comments);
        l1 = view.findViewById(R.id.l1);
        message_comments.setVisibility(GONE);

        tv_week_display = view.findViewById(R.id.tv_week_display);
        if (getArguments() != null) {
            pacifyer_obj = (MUser) getArguments().getSerializable("OBJECT");
        }


        if (isValid(data)) {
            String disaplay_date = getDisplayDateValue(data);
            if (isValid(disaplay_date))
                tv_week_display.setText(disaplay_date);

        }

        ll_prev_week.setVisibility(View.INVISIBLE);
        ll_prev_week.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!pacifyer_obj.isEnableScheduling())
                    return;
                weekNumber--;
                if (weekNumber<=0)
                {
                    ll_prev_week.setVisibility(View.INVISIBLE);
                }
                rcv_week_schedule_.setAdapter(null);
                rcv_slote_schedule_.setAdapter(null);

                if (isValid(data))
                    data = getPreviousWeek();

                String disaplay_date = getDisplayDateValue(data);

                if (isValid(disaplay_date))
                    tv_week_display.setText(disaplay_date);

                /** if previous wee is loaded
                 * the the current date is passed**/

                noDataAvailable.setVisibility(GONE);
                getWeakScheduleList();

            }
        });


        ll_next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pacifyer_obj.isEnableScheduling())
                    return;
                rcv_week_schedule_.setAdapter(null);
                rcv_slote_schedule_.setAdapter(null);
                weekNumber++;
                if (weekNumber>0)
                {
                    ll_prev_week.setVisibility(VISIBLE);

                }
                if (isValid(data))
                    data = getNextWeek();

                String disaplay_date = getDisplayDateValue(data);

                if (isValid(disaplay_date))
                    tv_week_display.setText(disaplay_date);
                noDataAvailable.setVisibility(GONE);
                getWeakScheduleList();
            }
        });

        getWeakScheduleList();


        txtv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtv_next.getText().equals("Cancel")) {
                    dismiss();
                } else if (txtv_next.getText().equals("Next")) {
                    txtv_back.setVisibility(VISIBLE);
                    txtv_back.setText("Back");
                    rcv_week_schedule_.setVisibility(GONE);
                    if (isValid(SlotForNextClick)) {
                        callAppoinmentConfirmationScreen(SlotForNextClick);
                        //callSlotBookApi(SlotForNextClick);
                    }
                }
                /*else if (txtv_next.getText().equals("Confirm")) {
                    if (isValid(SlotForNextClick)) {
                            //for US customers
                        callSlotBookApiForUS(SlotForNextClick);
                    }

                }*/
                else if (txtv_next.getText().equals("Proceed to pay") ||
                        txtv_next.getText().equals(getString(R.string.continue_btn_text))) {
                    if (isValid(SlotForNextClick)) {
                        //for Indian customers
                        callSlotBookApi(SlotForNextClick);
                    }

                } else if (txtv_next.getText().equals("Done")) {
                   dismiss();
                }
            }
        });

        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(getActivity());

        args.putString(FirebaseLogging.EC, ScheduleBottomSheetFragment.class.getSimpleName());

        FirebaseLogging.sendLog(getActivity(),
                FirebaseLogging.EVENT_ON_SCREEN,
                args);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtv_back.getText().equals("Close"))
                {
                    dismiss();
                }
                else {
                    firstLayout.setVisibility(VISIBLE);
                    confirmLayout.setVisibility(View.GONE);
                    appointmentDoneLayout.setVisibility(GONE);
                    rcv_week_schedule_.setVisibility(VISIBLE);
                    txtv_next.setText("Next");
                    txtv_back.setText("Close");
                    l1.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    private void callSlotBookApiAfterPaymentSuccess(Slot singleSlote) {
        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());
                jsonObject.put("timeZoneOffset",Utility.timeZoneOffset());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(singleSlote.getId());
                jsonObject.put("timeSlotId",jsonArray);
                if (!isValid(bookingRequestPaymentId))
                {
                    Toast.makeText(getActivity(), "Invalid bookingRequestPaymentId", Toast.LENGTH_SHORT).show();
                    return;
                }
                jsonObject.put("bookingRequestPaymentId",bookingRequestPaymentId);


                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/slots/book";
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    setLoading();
                    showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                hideProgress();
                                if (isValid(json)) {
                                    String jsonS = json.toString();
                                    String status = null;
                                    String messsage= null;
                                    Log.w("QQQQ", jsonS);

                                    if (json.has("status"))
                                    {
                                        try {
                                            status=json.getString("status");
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                    if (json.has("message"))
                                    {
                                        try {
                                            messsage=json.getString("message");
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }

                                        if (isValid(status)) {

                                            if (status.equals("success"))
                                            {
                                                setStatus("Successfully booked", R.color.md_green_400);
                                                showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        hideStatus();
                                                        if (isValid(USPAYMENT_MESSAGE_SUMMARY))
                                                        showBillingSummary(USPAYMENT_MESSAGE_SUMMARY);
                                                    }
                                                }, 4000);

                                            }

                                        } else {
                                            setStatus(messsage, R.color.md_red_A700);
                                            showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    hideStatus();
                                                    hideProgress();
                                                }
                                            }, 4000);
                                        }

                                    } else {
                                        setStatus("Something went wrong !", R.color.md_red_A700);
                                        showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                hideStatus();

                                            }
                                        }, 4000);
                                    }

                                    /*if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                            }
                        }
                    };

                    AQuery aq = new AQuery(getActivity());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    aq.post(url, jsonObject, JSONObject.class, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("@@@",String.valueOf(e));
                }


            } catch (Exception e) {
                Log.w("@@@",String.valueOf(e));
            }

        }


    }


    private void callAppoinmentConfirmationScreen(Slot slotForNextClick) {
        firstLayout.setVisibility(GONE);
        confirmLayout.setVisibility(View.VISIBLE);
        appointmentDoneLayout.setVisibility(GONE);
        ll_prev_week.setVisibility(View.INVISIBLE);
        Glide.with(getActivity())
                .load(GLOBAL_IMAGE_URL + pacifyer_obj.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(R.drawable.ic_error_white)
                .dontTransform()
                .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                .into(pacifyr_avatar);

        pacifyr_name.setText(pacifyer_obj.getFirstName() + "" + pacifyer_obj.getLastName() + "");

        String t =TempScheduleData.getDate();
        String time = Utility.getStartFormatDate(t,getActivity());
        int spacePos = time.indexOf(" ");
        if (spacePos > 0) {
            time= time.substring(0, spacePos);
        }
        appointment_date_and_time.setText(time +" "+ Utility.convertTime(slotForNextClick.getStartTime()));
       // Log.w("@@@",TempScheduleData.getDate() + slotForNextClick.getStartTime());

      //  appointment_date_and_time.setText(Utility.getDateFormatOne(TempScheduleData.getDate() + "") + ", " + Utility.convertTime(slotForNextClick.getStartTime()));

      //  if (prefM.getCountry().equals("India") || prefM.getCountry().equals("Wakanda"))
        if (Integer.parseInt(pacifyer_obj.getPrice()) > 0) {
            txtv_next.setText("Proceed to pay");
        } else {
            txtv_next.setText(getString(R.string.continue_btn_text));
        }
      //  else
       //     txtv_next.setText("Confirm");



    }

    private void getWeakScheduleList() {
        if (isValid(pacifyer_obj) &&isValid(pacifyer_obj.isEnableScheduling()) && !pacifyer_obj.isEnableScheduling()) {
            noDataAvailable.setVisibility(VISIBLE);
            return;
        }

        text_loading.setVisibility(VISIBLE);
        rcv_week_schedule_.setAdapter(null);

        String token = prefM.getAccessToken();

        String userid = prefM.getUserID();

        AjaxCallback cb = new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                text_loading.setVisibility(GONE);
                try {


                    if (isValid(json)) {
                        Log.w("@@@@", String.valueOf(json));
                        WeekScheduleData = GsonUtils.getInstance().gsonToModelScheduleData(json);
                        if (isValid(WeekScheduleData)) {
                            hideProgress();

                            noDataAvailable.setVisibility(GONE);
                            rcv_week_schedule_.setVisibility(View.VISIBLE);
                            rcv_slote_schedule_.setVisibility(View.VISIBLE);

                            setWeekScheduleAdapter(WeekScheduleData);
                        } else {
                            hideProgress();
                            noDataAvailable.setVisibility(View.VISIBLE);
                            rcv_week_schedule_.setVisibility(GONE);
                            rcv_slote_schedule_.setVisibility(GONE);
                        }

                    } else {
                        hideProgress();
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
        try {
            AQuery aq = new AQuery(getActivity());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "schedule/byWeek?pacifyrId=" + pacifyer_obj.getId() + "&weekNumber=" + weekNumber, JSONArray.class, cb);

        } catch (Exception ex) {
            text_loading.setVisibility(GONE);

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

        /** select current date as default selection**/
        mWeekScheduleListAdapter.setDefaultFirstWeekSelection();

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
        setBefore(!enteredDate.after(currentDate));


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


        displaydate =  month+ " " +first_dd  + " - " + month2 + " " + second_dd;

        return displaydate;

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w("WWW","onStart");
        EventBus.getDefault().register(this);
    }
    public static boolean isCardAddedFromAppointment=false;
    @Override
    public void onResume() {
        super.onResume();
        Log.w("WWW","onResume");
        if (isCardAddedFromAppointment)
        {
            isCardAddedFromAppointment=false;
            checkIfCardIsAddedOrNot(SlotForNextClick);
        }
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

        Log.w("WWW","onStop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Log.w("WWW","onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("WWW","onDestroy");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.w("WWW","onPause");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.w("WWW","onAttach");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w("WWW","onAttach");
    }


    private void webGetTimSlotes(ScheduleData mObject) {

        text_loading.setVisibility(VISIBLE);
        noDataAvailable.setVisibility(GONE);
        rcv_slote_schedule_.setAdapter(null);
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
                text_loading.setVisibility(GONE);
                try {
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
                                if (!txtv_next.getText().equals("Done"))
                                    txtv_next.setVisibility(GONE);
                                l1.setVisibility(View.VISIBLE);
                            } else {
                                noDataAvailable.setVisibility(View.VISIBLE);
                                rcv_week_schedule_.setVisibility(View.VISIBLE);
                                rcv_slote_schedule_.setVisibility(GONE);
                            }


                        } else {
                            Toast.makeText(getActivity(), "No slots available", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (isValid(status.getMessage())) {
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    text_loading.setVisibility(GONE);
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
            hideProgress();
            ex.printStackTrace();
        }

        hideProgress();
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

                mSlotesData.get(i).setHideSlote(servermilli <= currentMill);
               /* Log.w("@@@currentMill", String.valueOf(currentMill));
                Log.w("@@@servermilli", String.valueOf(servermilli));*/


            }


        }

        List<Slot> tempSlote=new ArrayList<>();

        if (isValidSize(mSlotesData))
        {
            for (Slot item:mSlotesData) {
                if (item.isHideSlote()==false)
                {
                    tempSlote.add(item);
                }

            }
        }
        if (!isValidSize(tempSlote))
        {
            noDataAvailable.setVisibility(VISIBLE);
            rcv_week_schedule_.setVisibility(VISIBLE);
            rcv_slote_schedule_.setVisibility(View.GONE);
            return;
        }

        noDataAvailable.setVisibility(GONE);
        rcv_week_schedule_.setVisibility(View.VISIBLE);
        rcv_slote_schedule_.setVisibility(View.VISIBLE);
        mScheduleTimeSlotesAdapter = new ScheduleTimeSlotesAdapter(getActivity(), tempSlote, isDateIsPassed, new ScheduleTimeSlotesAdapter.SloteBookedListener() {
            @Override
            public void onSloteBooked(int position) {
                SlotForNextClick = null;
                if (isValid(tempSlote) && isValid(tempSlote.get(position))) {
                    Slot singleSlote = tempSlote.get(position);

                    if (isValid(singleSlote)) {
                        SlotForNextClick = singleSlote;
                        //showSlotBookedConfirmationPopup(singleSlote);
                        if (singleSlote.getBooked()) {
                            SlotForNextClick = null;
                            if (isValid(singleSlote.getBookedUserId()) && isValid(prefM.getUserID())) {
                                if (singleSlote.getBookedUserId().equals(prefM.getUserID())) {
                                  //  showBookedSlotCancel(singleSlote);
                                }

                            }

                        } else {
                            txtv_next.setText("Next");
                            txtv_next.setVisibility(View.VISIBLE);
                            l1.setVisibility(View.INVISIBLE);
                            mScheduleTimeSlotesAdapter.notifyDataSetChanged();
                        }

                    }
                }

            }
        });
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        rcv_slote_schedule_.setLayoutManager(mGridLayoutManager);
        rcv_slote_schedule_.setItemAnimator(new DefaultItemAnimator());
        rcv_slote_schedule_.setAdapter(mScheduleTimeSlotesAdapter);

    }

    private void showBookedSlotCancel(Slot singleSlote) {

        ((PacifyrActivity) getActivity()).setAlertStatusInBlue("Cancel the slot", "Do you want to cancel the slot ?", new Dialog.onClick() {
            @Override
            public void onAction(Dialog d) {
                ((PacifyrActivity) getActivity()).hideStatus();

                callCAncelBookedSlotApi(singleSlote);
            }
        });

    }


    public String getCurrentTime() {

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


    public long getMilliSecondsTime(String time) {
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
                    setLoading();
                    showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                hideProgress();

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
                                                setStatus(messsage, R.color.md_green_400);
                                                showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        hideStatus();

                                                    }
                                                }, 2000);
                                            } else {
                                                setStatus(messsage, R.color.md_red_A700);
                                                showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        hideStatus();

                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            setStatus(messsage, R.color.md_red_A700);
                                            showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    hideStatus();

                                                }
                                            }, 2000);
                                        }

                                        showStatus();
                                    } else {
                                        setStatus("Something went wrong !", R.color.md_red_A700);
                                        showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                hideStatus();

                                            }
                                        }, 2000);
                                    }

                                    if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
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

        ((PacifyrActivity)getActivity()).setAlertStatusInBlue("Book the slot", "Do you want to book the slot ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PacifyrActivity)getActivity()).hideStatus();

                callSlotBookApi(singleSlote);
            }
        });
    }*/
    private void callSlotBookApi(Slot singleSlote) {
//"createdAt":"2020-07-29T08:49:56.144Z","cost":1,"id":"5f213834afdb75314aa8b3a6","userId":"5e9d63e53b105d2ba27f8819","pacifyrId":"5e6a27b03b105d65099abadc","charged":false,"updatedAt":"2020-07-29T08:49:56.144Z"},"status":"success"}
        bookingRequestPaymentId=null;
        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());
                jsonObject.put("pacifyrId", pacifyer_obj.getId());
                //jsonObject.put("timeZoneOffset",Utility.timeZoneOffset());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(singleSlote.getId());
                jsonObject.put("timeSlotId",singleSlote.getId());


                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/bookingRequestPayment";
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    setLoading();
                    showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                hideProgress();

                                if (isValid(json)) {
                                    String jsonS = json.toString();
                                    String status;
                                    String messsage;
                                    String paytmOrderId = null;
                                    Log.w("QQQQ", jsonS);
                                    float cost=0;

                                    SchedulePayment mSchedulePayment=GsonUtils.getInstance().gsonToSchedulePayment(json);



                                    if (isValid(mSchedulePayment)) {

                                        status = mSchedulePayment.getStatus()+"";
                                        messsage = mSchedulePayment.getMessage()+"";
                                        if (isValid(status)) {

                                            if (status.equals("success"))
                                            {
                                                /**Call Paytm Activity from here **/


                                                if (isValid(mSchedulePayment.getBookingRequestPayment().getCost()))
                                                {
                                                    cost= mSchedulePayment.getBookingRequestPayment().getCost();
                                                }

                                                if (isValid(mSchedulePayment.getBookingRequestPayment() )&& isValid(mSchedulePayment.getBookingRequestPayment().getId()))
                                                {
                                                    paytmOrderId = mSchedulePayment.getBookingRequestPayment().getId();
                                                }
                                                if (prefM.getCountry().equals("India") || prefM.getCountry().equals("Wakanda"))
                                                {
                                                    if (isValid(paytmOrderId)) {

                                                        bookingRequestPaymentId = paytmOrderId;

                                                        if (Math.round(cost) <= 0) { //EAP counselors have price set as 0. Continue with booking

                                                            ((PacifyrActivity)getActivity()).setAlertStatusInWhiteNoInternet("", getResources().getString(R.string.You_will_not_charged_) , "Yes","No", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ((PacifyrActivity) getActivity()).hideProgress();
                                                                    ((PacifyrActivity) getActivity()).hideStatus2();
                                                                    EventBus.getDefault().post("SCHEDULE_PAY");
                                                                }
                                                            });

                                                        } else {
                                                            String bookingTempId = paytmOrderId;
                                                            if (errorCount != 0)
                                                                bookingTempId = bookingTempId + "_" + errorCount;
                                                            String finalBookingTempId = bookingTempId;
                                                            float finalCost = cost;
                                                            ((PacifyrActivity)getActivity()).setAlertStatusInWhiteNoInternet("","You will be charged amount "+Utility.getPriceSign(getActivity())+ cost+" for this session. Do you want to continue?" , "Yes","No", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ((PacifyrActivity) getActivity()).hideProgress();
                                                                    ((PacifyrActivity) getActivity()).hideStatus2();
                                                                    ((PacifyrActivity) getActivity()).generateCheckSum(false, finalBookingTempId, Math.round(finalCost));
                                                                }
                                                            });

                                                        }


                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getActivity(), "Invalid TimeSloteId", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {

                                                    if (isValid(paytmOrderId)) {
                                                        bookingRequestPaymentId = paytmOrderId;
                                                        if (Math.round(cost) <= 0) { //EAP counselors have price set as 0. Continue with booking

                                                            ((PacifyrActivity)getActivity()).setAlertStatusInWhiteNoInternet("", getResources().getString(R.string.You_will_not_charged_) , "Yes","No", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ((PacifyrActivity) getActivity()).hideProgress();
                                                                    ((PacifyrActivity) getActivity()).hideStatus2();
                                                                    EventBus.getDefault().post("SCHEDULE_PAY");
                                                                }
                                                            });

                                                        } else {
                                                            ((PacifyrActivity)getActivity()).setAlertStatusInWhiteNoInternet("","You will be charged amount $"+ String.valueOf(cost)+" for this session. Do you want to continue?" , "Yes","No", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ((PacifyrActivity) getActivity()).hideProgress();
                                                                    ((PacifyrActivity) getActivity()).hideStatus2();
                                                                    checkIfCardIsAddedOrNot(singleSlote);
                                                                }
                                                            });

                                                        }


                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getActivity(), "Invalid TimeSloteId", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            if (status.equals("error"))
                                            {
                                                setStatus(messsage, R.color.md_red_A700);
                                                showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        hideStatus();

                                                    }
                                                }, 4000);
                                            }

                                        } else {
                                          setStatus(messsage, R.color.md_red_A700);
                                            showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    hideStatus();

                                                }
                                            }, 4000);
                                        }

                                    } else {
                                        setStatus("Something went wrong !", R.color.md_red_A700);
                                        showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                hideStatus();

                                            }
                                        }, 2000);
                                    }

                                    /*if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }*/
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                            }
                        }
                    };

                    AQuery aq = new AQuery(getActivity());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    aq.post(url, jsonObject, JSONObject.class, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("@@@",String.valueOf(e));
                }


            } catch (Exception e) {
                Log.w("@@@",String.valueOf(e));
            }

        }

    }

    private void checkIfCardIsAddedOrNot(Slot singleSlote) {

        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb " );
            PrefManager prefM= PrefManager.getInstance(getActivity());
            showProgress();
            String url= NetworkService.GLOBAL_URL+"payment/savedcard/"+prefM.getUserID();
            AjaxCallback cb = new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Intent i = new Intent(getActivity(), PaymentActivity.class);
                                i.putExtra("isFromBooking",true);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        };
                        hideProgress();
                        LogUtils.LOGD("session", "PA getSavedCardWeb callbaack "+json );
                        if(isValid(json)) {
                            String jsonString = json.toString();
                            int l=jsonString.length();
                            if(l>3) {
                                LogUtils.LOGD("session", "PA saveCardExist true " );
                                callSlotBookApiAfterStripeSuccess(singleSlote);
                            }
                            else{
                                ((PacifyrActivity)getActivity()).showIOSDialogOK("","A payment method needs to be set up.",Utility.getPaymentLabel(getActivity(),prefM.getCountry()),listener);
                            }
                        }else{
                            ((PacifyrActivity)getActivity()).showIOSDialogOK("","A payment method needs to be set up.",Utility.getPaymentLabel(getActivity(),prefM.getCountry()),listener);
                        }
                    } catch (Exception e) {

                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",prefM.getAccessToken());
            aq.ajax(url,JSONArray.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ((PacifyrActivity)getActivity()).showIOSDialogOK("","Please try again","OK");
            LogUtils.LOGD("session","PA getSavedCardWeb exce " + ex.getMessage());
        }

    }

    String USPAYMENT_MESSAGE_SUMMARY= null;
    private void callSlotBookApiAfterStripeSuccess(Slot singleSlote) {

        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());
                jsonObject.put("timeZoneOffset",Utility.timeZoneOffset());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(singleSlote.getId());
                jsonObject.put("timeSlotId",jsonArray);
                if (!isValid(bookingRequestPaymentId))
                {
                    Toast.makeText(getActivity(), "Invalid bookingRequestPaymentId", Toast.LENGTH_SHORT).show();
                    return;
                }
                jsonObject.put("bookingRequestPaymentId",bookingRequestPaymentId);


                try {
                    String url = NetworkService.GLOBAL_URL + "payment/chargeForAppointment";
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                    setLoading();
                    showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                hideProgress();
                                if (isValid(json)) {
                                    String jsonS = json.toString();
                                    String status = null;
                                    String messsage= null;
                                    String amount= null;
                                    String transactionId= null;
                                    Log.w("QQQQ", jsonS);

                                    if (json.has("status"))
                                    {
                                        try {
                                            status=json.getString("status");
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                    if (json.has("message"))
                                    {
                                        try {
                                            messsage=json.getString("message");
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }

                                    if (json.has("messageExtended"))
                                    {
                                        String extendMessage=null;
                                        try {
                                            if (isValid(json.getString("messageExtended")))
                                                extendMessage=json.getString("messageExtended");
                                            if (!extendMessage.equalsIgnoreCase("null"))
                                                messsage = extendMessage;

                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }


                                    if (isValid(status)) {

                                        if (status.equals("success"))
                                        {
                                            USPAYMENT_MESSAGE_SUMMARY=messsage;
                                            EventBus.getDefault().post("SCHEDULE_PAY");
                                        }
                                        else {
                                            setStatus(messsage, R.color.md_red_A700);
                                            showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    hideStatus();
                                                    hideProgress();
                                                }
                                            }, 2000);
                                        }

                                    } else {
                                        setStatus(messsage, R.color.md_red_A700);
                                        showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                hideStatus();
                                                hideProgress();
                                            }
                                        }, 2000);
                                    }

                                } else {
                                    setStatus("Something went wrong !", R.color.md_red_A700);
                                    showStatus();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            hideStatus();

                                        }
                                    }, 2000);
                                }

                                    /*if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                            }
                        }
                    };

                    AQuery aq = new AQuery(getActivity());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    aq.post(url, jsonObject, JSONObject.class, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("@@@",String.valueOf(e));
                }


            } catch (Exception e) {
                Log.w("@@@",String.valueOf(e));
            }

        }


    }


    private void showBillingSummary(String messsage) {


       try {
            ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bill_summary_for_appointment_booking, viewGroup, false);
        TextView ok = view.findViewById(R.id.button_ok);

        TextView textViewCopyTransaction = view.findViewById(R.id.textViewCopyTransaction);
        TextView TextView_amount_charged= view.findViewById(R.id.TextView_amount_charged);
        TextView transactionId= view.findViewById(R.id.transactionId);
        TextView title = view.findViewById(R.id.title);
       TextView text_date = view.findViewById(R.id.text_date);
           TextView text_time = view.findViewById(R.id.text_time);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        String[] list = messsage.split("\n");
        transactionId.setText(list[2].substring(list[2].lastIndexOf(":") + 1));
        TextView_amount_charged.setText(list[1].substring(list[1].lastIndexOf(":") + 1));
        title.setText(getResources().getString(R.string.payment_summary));

           Date c = Calendar.getInstance().getTime();
           SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
           String strDate = targetFormat.format(c.getTime());
           strDate.replace("AM", "am").replace("PM","pm");


           String[] separated = strDate.split(" ");
           text_date.setText(separated[0].toLowerCase()+"");
           text_time.setText(separated[2].toLowerCase()+" "+separated[3].toLowerCase()+"");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        textViewCopyTransaction.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int[] textLocation = new int[2];
                    textViewCopyTransaction.getLocationOnScreen(textLocation);

                    if(event.getRawX() >= textViewCopyTransaction.getRight() - textViewCopyTransaction.getTotalPaddingRight()) {
                        /** copy transaction id to clipboard **/

                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("transactionId", transactionId.getText());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                }
                return true;
            }
        });
       }catch (Exception e)
       {

       }

    }

    private void callSlotBookApiForUS(Slot singleSlote) {


        if (isValid(singleSlote)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", prefM.getUserID());
                jsonObject.put("timeZoneOffset",Utility.timeZoneOffset());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(singleSlote.getId());
                jsonObject.put("timeSlotId",jsonArray);


                try {
                    String url = NetworkService.GLOBAL_URL + "schedule/slots/book";
                    PrefManager prefM = PrefManager.getInstance(getActivity());
                    String token = prefM.getAccessToken();
                   setLoading();
                    showProgress();
                    AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus stat) {
                            try {
                                hideProgress();

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
                                                confirmLayout.setVisibility(GONE);
                                                txtv_next.setText("Done");
                                                txtv_back.setVisibility(GONE);
                                                confirmationMessageText.setText("Your request for an appointment with "+pacifyer_obj.getFirstName() + " "
                                                        + pacifyer_obj.getLastName()+" " +
                                                        "has been submitted and you will be " +
                                                        "notified once the time slot is confirmed.");

                                                /*confirmationMessageText.setText("Your appointment is scheduled with "
                                                        + pacifyer_obj.getFirstName() + ""
                                                        + pacifyer_obj.getLastName() + "" + "on "
                                                        + Utility.getDateFormatOne(TempScheduleData.getDate()) + ","
                                                        + Utility.convertTime(singleSlote.getStartTime()));*/

                                                /*((PacifyrActivity)getActivity()).setStatus(messsage, R.color.md_green_400);
                                                ((PacifyrActivity)getActivity()).showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ((PacifyrActivity)getActivity()).hideStatus();

                                                    }
                                                }, 2000);*/
                                            } else {
                                                setStatus(messsage, R.color.md_red_A700);
                                                showStatus();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                       hideStatus();

                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            setStatus(messsage, R.color.md_red_A700);
                                         showStatus();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    hideStatus();

                                                }
                                            }, 2000);
                                        }

                                    } else {
                                        setStatus("Something went wrong !", R.color.md_red_A700);
                                       showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                hideStatus();

                                            }
                                        }, 2000);
                                    }

                                    if (isValid(TempScheduleData)) {
                                        webGetTimSlotes(TempScheduleData);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                            }
                        }
                    };

                    AQuery aq = new AQuery(getActivity());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    aq.post(url, jsonObject, JSONObject.class, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("@@@",String.valueOf(e));
                }


            } catch (Exception e) {
                Log.w("@@@",String.valueOf(e));
            }

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayTmDone(Object event) {

        Log.w("@@@", "onPayTmDone");
         try {
            if (event.equals("SCHEDULE_PAY")) {
                Log.w("@@@@---", bookingRequestPaymentId);
                appointmentDoneLayout.setVisibility(View.VISIBLE);
                confirmLayout.setVisibility(GONE);
                txtv_next.setText("Done");
                txtv_back.setVisibility(GONE);
                confirmationMessageText.setText("Your request for an appointment with " + pacifyer_obj.getFirstName() + " "
                        + pacifyer_obj.getLastName() + " " +
                        "has been submitted and you will be " +
                        "notified once the time slot is confirmed.");
                callSlotBookApiAfterPaymentSuccess(SlotForNextClick);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        try {
            if(isValid(hud_progress)) if(hud_progress.isShowing()) hud_progress.dismiss();
        }catch (Exception e)
        {

        }
    }

    public void showProgress() {
        try {
            hideProgress();
            if(isValid(hud_progress)) if(!hud_progress.isShowing()) hud_progress.show();
        }catch (Exception e)
        {

        }
    }

    public void setStatus(String status, int color) {
        View view = LayoutInflater.from(getActivity()).inflate(com.pacifyrapp.utilitylibrary.R.layout.status_dialog, null, false);
        CustomFontTextView status_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.status_dtxv);
        status_dtxv.setText("" + status);
        hideStatus();
        hud_status = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(getActivity().getResources().getColor(color)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);

    }

    public void hideStatus() {
        if(hud_status != null) if(hud_status.isShowing()) hud_status.dismiss();
    }

    public void showStatus() {
        try {
            if(hud_status != null) if(!hud_status.isShowing()) hud_status.show();
        }
        catch (WindowManager.BadTokenException e) {
            Log.w("@@@EXC","WindowManager.BadTokenException");
        }

    }

    public void setLoading() {
        hideProgress();
        if(isValid(hud_progress))
            hud_progress=null;
        hud_progress = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }


}
