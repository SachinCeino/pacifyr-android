package com.pacifyr.client.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.model.PreviousPrescription;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

public class AppointmentDetailsActivity extends PacifyrActivity {


    private String titleText="Prescription";
    private String text;
    private TextView TextView_name,TextView_specialities,TextView_date,TextView_status,TextView_Notes,
            TextView_refundAmount,TextView_TransactionId;
    CircleImageView CircleImageView_profileImage;
    LinearLayout LinearLayout_rejectReason,LinearLayout_callLayout;
    RelativeLayout videocall_rlay,audiocall_rlay,chat_rlay;
    AppointmentModel.Result mObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointment_detail_view);

        onSetActionBar();
        initViews();
        getIntentValues();



    }

    private void getIntentValues() {
        if (isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getSerializable("object")))
        {
            mObject= (AppointmentModel.Result) getIntent().getExtras().getSerializable("object");
        }

        if (isValid(mObject)) {
            initValues();
        }
    }

    private void initValues() {
        if (isValid(mObject.getPacifyr())) {
            Picasso.with(getApplicationContext())
                    .load(NetworkService.GLOBAL_IMAGE_URL + mObject.getPacifyr().getImage())
                        .placeholder(R.drawable.placeholder).into(CircleImageView_profileImage);

            TextView_name.setText(mObject.getPacifyr().getFirstName()
                    + " " + mObject.getPacifyr().getLastName());

            if (isValid(mObject.getStatus())) {
                char[] words = mObject.getStatus().toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i=0;i<words.length;i++)
                {
                    if (i==0) {
                        sb.append(Character.toUpperCase(words[0]));
                    }
                    sb.append(words[i]);
                }

                TextView_status.setText(sb);
            }

            try {
                if (isValid(mObject.getPacifyr().getSpecialities())) {
                    StringBuffer mStringBuffer = new StringBuffer();
                    for (String mSpecialityItem : mObject.getPacifyr().getSpecialities()) {
                        mStringBuffer.append(mSpecialityItem + ",");
                    }
                    mStringBuffer.setLength(Math.max(mStringBuffer.length()-1,0));
                    if (isValid(mStringBuffer))
                        TextView_specialities.setText(mStringBuffer);

                    if (isValid(mObject.getStartTimeGMT())) {
                        /*String mDisplayDateAndTime = Utility.getDateAndTimeForAppointmentList(mObject.getStartTimeGMT());
                        holder.mTextView_ScheduleTime.setText(mDisplayDateAndTime);*/

                        String t =mObject.getStartTimeGMT();
                        String time = Utility.getStartFormatDate(t,getApplicationContext());
                        TextView_date.setText(time);
                    }

                    if (scheduleIsPassed(mObject.getStartTimeGMT())) {
                        if (AppointMentCalUIForAppointMentTime(mObject.getStartTimeGMT()) == true && mObject.getConfirmed() ==true)
                        {

                            if (mObject.getRejected())
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                TextView_status.setTextColor(getResources().getColor(R.color.red));
                                if (isValid(mObject.getPacifyr().getPrice()))
                                    TextView_refundAmount.setText("Refund amount "+Utility.getPriceSign(getApplicationContext())+"" +mObject.getPacifyr().getPrice());
                                if (isValid(mObject.getTransactionId()))
                                    TextView_TransactionId.setText("Transaction Id: "+mObject.getTransactionId());
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }


                            else if(isValid(mObject.getCancelled()) && mObject.getCancelled()==true)
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                TextView_status.setTextColor(getResources().getColor(R.color.red));
                                if (isValid(mObject.getPacifyr().getPrice()))
                                    TextView_refundAmount.setText("Refund amount "+Utility.getPriceSign(getApplicationContext())+"" +mObject.getPacifyr().getPrice());
                                if (isValid(mObject.getTransactionId()))
                                    TextView_TransactionId.setText("Transaction Id: "+mObject.getTransactionId());
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }
                            else if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                    && isValid(mObject.getCompleted())&&mObject.getCompleted())
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }
                            else
                            {
                                if (AppointMentCalUIForAppointMentTime(mObject.getStartTimeGMT()) == true && mObject.getConfirmed() ==true)
                                {
                                    LinearLayout_callLayout.setVisibility(View.VISIBLE);
                                }

                                LinearLayout_rejectReason.setVisibility(View.GONE);
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());

                            }


                            if(isValid(mObject.getRejected()) && mObject.getRejected())
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                TextView_status.setTextColor(getResources().getColor(R.color.red));
                                if (isValid(mObject.getPacifyr().getPrice()))
                                    TextView_refundAmount.setText("Refund amount "+Utility.getPriceSign(getApplicationContext())+"" +mObject.getPacifyr().getPrice());
                                if (isValid(mObject.getTransactionId()))
                                    TextView_TransactionId.setText("Transaction Id: "+mObject.getTransactionId());
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }
                            else
                            {

                                if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                        && isValid(mObject.getCompleted())&&mObject.getCompleted())
                                {
                                    LinearLayout_callLayout.setVisibility(View.GONE);
                                    LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                }
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }



                        }
                        else {
                            if (mObject.getRejected())
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                TextView_status.setTextColor(getResources().getColor(R.color.red));
                                if (isValid(mObject.getPacifyr().getPrice()))
                                    TextView_refundAmount.setText("Refund amount "+Utility.getPriceSign(getApplicationContext())+"" +mObject.getPacifyr().getPrice());
                                if (isValid(mObject.getTransactionId()))
                                    TextView_TransactionId.setText("Transaction Id: "+mObject.getTransactionId());
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }


                            else if(isValid(mObject.getCancelled()) && mObject.getCancelled()==true)
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                                LinearLayout_rejectReason.setVisibility(View.VISIBLE);
                                TextView_status.setTextColor(getResources().getColor(R.color.red));
                                if (isValid(mObject.getPacifyr().getPrice()))
                                    TextView_refundAmount.setText("Refund amount "+Utility.getPriceSign(getApplicationContext())+"" +mObject.getPacifyr().getPrice());
                                if (isValid(mObject.getTransactionId()))
                                    TextView_TransactionId.setText("Transaction Id: "+mObject.getTransactionId());
                                if (isValid(mObject.getPreVisitNote()))
                                    TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            }
                        }


                    }

                    if (isValid(mObject.getPreVisitNote()))
                        TextView_Notes.setText(mObject.getPreVisitNote().toString());
                            if (mObject.getStatus().equals("pending"))
                            {
                                LinearLayout_callLayout.setVisibility(View.GONE);
                            }

                            videocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(getApplicationContext())) {
                                        Toast.makeText(getApplicationContext(), "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    startSessionWebforAppoinmetCall(mObject.getPacifyr(), true,mObject.getId());
                                }
                            });

                            audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(getApplicationContext())) {
                                        Toast.makeText(getApplicationContext(), "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    startSessionWebforAppoinmetCall(mObject.getPacifyr(), false,mObject.getId());

                                }
                            });

                            chat_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(getApplicationContext())) {
                                        Toast.makeText(getApplicationContext(), "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                  startChat(mObject.getPacifyr());
                                }
                            });


                }

            }catch (Exception e)
            {
                Log.w("ERROR",String.valueOf(e));
            }
        }


    }

    public boolean scheduleIsPassed(String date) {
        //add 15 minuts more
        long mcurrentMilliSeconds =900000+ Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }
    public boolean scheduleIsFullyPassed(String date) {
        //add 15 minuts more
        long mcurrentMilliSeconds =Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }
    public boolean AppointMentCalUIForAppointMentTime(String date) {
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        long TimDifference=mcurrentMilliSeconds - mServerMilliSeconds;
        if (TimDifference<1800000)
        {
            return true;
        }
        else {
            return false;
        }

    }

    private void initViews() {
        CircleImageView_profileImage=findViewById(R.id.CircleImageView_profileImage);
        TextView_name=findViewById(R.id.TextView_name);
        TextView_specialities=findViewById(R.id.TextView_specialities);
        TextView_date=findViewById(R.id.TextView_date);
        TextView_status=findViewById(R.id.TextView_status);
        TextView_Notes=findViewById(R.id.TextView_Notes);
        TextView_refundAmount=findViewById(R.id.TextView_refundAmount);
        TextView_TransactionId=findViewById(R.id.TextView_TransactionId);
        LinearLayout_rejectReason=findViewById(R.id.LinearLayout_rejectReason);
        LinearLayout_callLayout=findViewById(R.id.LinearLayout_callLayout);
        videocall_rlay=findViewById(R.id.videocall_rlay);
        audiocall_rlay=findViewById(R.id.audiocall_rlay);
        chat_rlay=findViewById(R.id.chat_rlay);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    ///ACTION BAR FEATURES
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setText("MY APPOINTMENT");
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }




}
