package com.pacifyr.client.home;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEarningsRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Mbonus;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.EarningWeekAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.WeekEarningsClickEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.Utility;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pacifyr.client.network.NetworkService.modelEarningsAPI;

/*import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;*/


public class EarningsActivity extends PacifyrAppCompactActivity {

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    public static Mbonus mbonus;


    private String weeklyTotal;
   // PieChart pieChart;

    public static final int SELECTED_SEGMENT_OFFSET = 50;
    public com.androidplot.pie.PieChart piechart;

    private Segment s1;
    private Segment s2;
    private Segment s3;
    private Segment s4,s5,s6,s7;

    int i;

    MainTextView wkly_total_ctxv;
    RecyclerView list_weeks_rcv;
    MainTextView text_dialog_hourstowork, text_dialog_bonusmessage, text_dialog_hourstobonus;

    public EarningsActivity() {
        // Required empty public constructor
    }
    public static void start(Context context ) {
        Intent intent = new Intent(context, EarningsActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        onCreateView();
        onSetActionBar();
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    public void  onCreateView() {

     try {

         pieINitial();
         wkly_total_ctxv = findViewById(R.id.wkly_total_ctxv);
         text_dialog_hourstowork = findViewById(R.id.text_dialog_hourstowork);
         text_dialog_hourstobonus = findViewById(R.id.text_dialog_hourstobonus);
         text_dialog_bonusmessage = findViewById(R.id.text_dialog_bonusmessage);


         webGetEarnings();
         webGetBonus();


         list_weeks_rcv = findViewById(R.id.list_weeks_rcv);
         LinearLayoutManager horizontalLayoutmanager = new LinearLayoutManager(EarningsActivity.this, LinearLayoutManager.HORIZONTAL, false);
         list_weeks_rcv.setLayoutManager(horizontalLayoutmanager);
         list_weeks_rcv.setVisibility(View.VISIBLE);


         EarningWeekAdapter adapter = new EarningWeekAdapter(EarningsActivity.this);
         list_weeks_rcv.setAdapter(adapter);
         if(isValid(adapter.getItemCount()))
             list_weeks_rcv.scrollToPosition(adapter.getItemCount() - 1);
         // pieChart.setUsePercentValues(true);


         //setChartData();
       /* If you want to display chart in percentage
        then pass new PercentFormatter() as a parameter
        otherwise pass new DefaultValueFormatter(0).new DefaultValueFormatter(0) parameter show data value.
        */
     }catch (Exception e)
     {
         e.printStackTrace();
     }

    }

    private void pieINitial() {
        try{
            piechart = findViewById(R.id.piechart);

            // enable the legend:
            piechart.getLegend().setVisible(true);

            final float padding = PixelUtils.dpToPix(30);
            piechart.getPie().setPadding(padding, padding, padding, padding);
/*

            private void deselectAll() {

                List<Segment> segments = piechart.getRegistry().getSeriesList();
                int i=0;
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                for(Segment segment : segments) {
                    i++;
                    if(day==i){
                        setSelected(segment, true);
                    }else {
                        setSelected(segment, false);
                    }
                }
            }
*/

            // detect segment clicks:
            piechart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                    if(piechart.getPie().containsPoint(click)) {
                        Segment segment = piechart.getRenderer(PieRenderer.class).getContainingSegment(click);

                        if(segment != null) {
                            final boolean isSelected = getFormatter(segment).getOffset() != 0;
                            deselectAll();
                            setSelected(segment, !isSelected);
                            piechart.redraw();
                        }
                    }
                    return false;
                }

                private SegmentFormatter getFormatter(Segment segment) {
                    return piechart.getFormatter(segment, PieRenderer.class);
                }

                private void deselectAll() {

                    List<Segment> segments = piechart.getRegistry().getSeriesList();
                    for(Segment segment : segments) {
                        setSelected(segment, false);
                    }
                }

                private void setSelected(Segment segment, boolean isSelected) {
                    SegmentFormatter f = getFormatter(segment);

                    if(isSelected) {
                        i=0;

                        int color_lightblue=getResources().getColor(R.color.blue_pacifyr);

         //               f =new SegmentFormatter(color_lightblue,color_white,color_white,color_white);
                        //  SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);

                        f.getFillPaint().setColor(color_lightblue);
                        f.setOffset(SELECTED_SEGMENT_OFFSET);
                    } else {


                        int color_medium=getResources().getColor(R.color.light_grey2_pacifyr);


                        int color_light = getResources().getColor(R.color.graph_light);
                        int color_hex = getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c6);
                        int color_dark = getResources().getColor(R.color.graph_dark);


                        switch (i)
                        {
                            case 0 : f.getFillPaint().setColor(color_light); break;
                            case 1 : f.getFillPaint().setColor(color_hex); break;
                            case 2 : f.getFillPaint().setColor(color_medium); break;
                            case 3 : f.getFillPaint().setColor(color_dark);break;
                            case 4 : f.getFillPaint().setColor(color_light); break;
                            case 5 : f.getFillPaint().setColor(color_medium); break;
                            case 6 : f.getFillPaint().setColor(color_hex); break;
                            default:
                                f.getFillPaint().setColor(color_medium);break;
                        }
                        i=i+1;
                        f.setOffset(0);
                    }
                }
            });
        }catch (Exception e)
        {

        }
    }

    private void webGetEarnings() {


        LocalDate now1 = new LocalDate();
        LocalDate backNow = now1.minusDays(7);
        LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
        LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
        LocalDate startofWeek_sun = startofWeek.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");
        String fromDateWeek = formatter.print(startofWeek_sun);

        String displayFrom = displayFormatter.print(startofWeek_sun);
        String displayTo = displayFormatter.print(now);

        String toDate = formatter.print(now);

        setLoading();
        showProgress();
        PrefManager prefM= PrefManager.getInstance(EarningsActivity.this);
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String thisWeekUrl= NetworkService.GLOBAL_URL+"earnings?pacifyrId="+userid+"&fromDate="+fromDateWeek+"&toDate="+toDate;
       // String url=GLOBAL_URL+"earnings?pacifyrId="+userid+"&fromDate=2018-01-15&toDate=2018-01-21";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                try {

                    modelEarningsAPI= GsonUtils.getInstance().gsonToMEarningsAPI(json);
                 //   postEventOnMainThread(new NotifyEvent(true, false));
          setChartData();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(EarningsActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(thisWeekUrl,JSONObject.class, cb);
    }

    private void webGetBonus() {


        LocalDate now1 = new LocalDate();
        LocalDate backNow = now1.minusDays(7);
        LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
        LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
        LocalDate startofWeek_sun = startofWeek.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");
        String fromDateWeek = formatter.print(startofWeek_sun);

        String displayFrom = displayFormatter.print(startofWeek_sun);
        String displayTo = displayFormatter.print(now);

        String toDate = formatter.print(now);

        setLoading();
        showProgress();
        /*PrefManager prefM= PrefManager.getInstance(EarningsActivity.this);
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();*/

        String token=getAccessToken();
        String userid=getUserID();

        String thisWeekUrl= NetworkService.GLOBAL_URL+"earnings/bonus?pacifyrId="+userid;
        // String url=GLOBAL_URL+"earnings?pacifyrId="+userid+"&fromDate=2018-01-15&toDate=2018-01-21";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();

                try {
                    if(json != null){
                       try {

                          mbonus = new Mbonus();

                           mbonus= GsonUtils.getInstance().gsonToMbonus(json.getJSONObject("bonus"));

                           setbonusData();




                        } catch (JSONException e) {
                            Toast.makeText(EarningsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }else{

                        //ajax error, show error code
                        Toast.makeText(EarningsActivity.this, "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        AQuery aq = new AQuery(EarningsActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(thisWeekUrl,JSONObject.class, cb);
    }


    private void webGetEarnings(String url) {

        LocalDate now1 = new LocalDate();
        LocalDate backNow = now1.minusDays(7);
        LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
        LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
        LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
        LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String fromDateWeek = formatter.print(startofWeek);
        String fromDateMonth = formatter.print(startofMonth);
        String fromDateYear = formatter.print(startofYear);
        String toDate = formatter.print(now);


        setLoading();
        showProgress();
        PrefManager prefM= PrefManager.getInstance(EarningsActivity.this);
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                   hideProgress();
                    modelEarningsAPI= GsonUtils.getInstance().gsonToMEarningsAPI(json);
                    //   postEventOnMainThread(new NotifyEvent(true, false));
                    setChartData();
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.earnings_api)));
                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(EarningsActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }




    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        setChartData();
    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    private void setbonusData() {

        try {
            if (isValid(mbonus)) {
                setLoading();

                Float total= Float.valueOf(0);

                int toBonus = mbonus.getToBonus();
                String message =mbonus.getMessage();
                int hoursThisWeek = mbonus.getHoursThisWeek();

                text_dialog_hourstowork.setText(String.valueOf(hoursThisWeek));
                text_dialog_hourstobonus.setText(String.valueOf(toBonus));

                text_dialog_bonusmessage.setText(message);

                hideProgress();
            }
        }
        catch (Exception exc)
        {
            hideProgress();
            exc.printStackTrace();
        }
    }





    private void setChartData() {
        try {
            if (isValid(modelEarningsAPI)) {
                setLoading();
                if(isValid(piechart))
                piechart.clear();
                ArrayList<Segment> segmentlist = new ArrayList<Segment>();
                // ArrayList<Entry> yvalues = new ArrayList<Entry>();
                //ArrayList<String> xVals = new ArrayList<String>();
                //hideProgress();
                String[] week = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                Float total= Float.valueOf(0);
                MEarningsRoot.Earnings[] earnings=modelEarningsAPI.getEarnings();
                int size=earnings.length;
                try {
                    for (int i = 0; i < size; i++) {
                        total = total + earnings[i].getEarnings();
                    }

                    for (int i = 0; i < size; i++) {
                        // int k=i+1;
                        int color = EarningsActivity.this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c6);


                          DecimalFormat df2 = new DecimalFormat("0.00");
                        float perentage=earnings[i].getEarnings();
                        //float perentage= Float.parseFloat(df2.format(earnings[i].getEarnings()));
                     /*   if(total>0)
                        {
                            perentage=  (earnings[i].getEarnings()/total)*100;
                        }
                        else {
                            perentage=100/size;
                        }*/

                        if(perentage<=0)
                        {   if(total<=0){
                            perentage=100/size;
                        }
                            else {
                            perentage= (float) (0.1*total);
                        }
                            Segment model = new Segment(week[i] + "   "+Utility.getPriceSign(EarningsActivity.this)+"0.00" , perentage);
                            segmentlist.add(model);
                        }
                        else {
                            if(perentage>0) {
                                String perentage12= String.format("%.2f", perentage);
                                Segment model = new Segment(week[i] + "   "+Utility.getPriceSign(EarningsActivity.this)+"" + perentage12, perentage);
                                segmentlist.add(model);
                            }
                        }
                    }
                }
                catch (Exception excc)
                {
                    excc.printStackTrace();
                }
                int b=Math.round(total);


                wkly_total_ctxv.setText(Utility.getPriceSign(EarningsActivity.this)+" "+String.format("%.2f", total) );

                EmbossMaskFilter emf = new EmbossMaskFilter(
                        new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

                int color_medium=getResources().getColor(R.color.medium_grey_pacifyr);
                int color_light = getResources().getColor(R.color.graph_light);
                int color_hex = getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c6);
                int color_dark = getResources().getColor(R.color.graph_dark);
                int color_white=getResources().getColor(R.color.white);

                int color_blue=getResources().getColor(R.color.blue_pacifyr);

                SegmentFormatter sf1 =new SegmentFormatter(color_light,color_white,color_white,color_white);
                //  SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);

                float fontsize=35.0f;
                sf1.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf1.getFillPaint().setMaskFilter(emf);
                sf1.getLabelPaint().setFakeBoldText(true);
                //  sf1.getLabelPaint().setSubpixelText(true);
                sf1.getLabelPaint().setTextSize(fontsize);

                SegmentFormatter sf2 =new SegmentFormatter(color_hex,color_white,color_white,color_white);
                //  SegmentFormatter sf2 = new SegmentFormatter(this, R.xml.pie_segment_formatter2);
                sf2.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf2.getFillPaint().setMaskFilter(emf);
                sf2.getLabelPaint().setFakeBoldText(true);
                sf2.getLabelPaint().setTextSize(fontsize);

                SegmentFormatter sf3 =new SegmentFormatter(color_medium,color_white,color_white,color_white);
                ///  SegmentFormatter sf3 = new SegmentFormatter(this, R.xml.pie_segment_formatter3);
                sf3.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf3.getFillPaint().setMaskFilter(emf);
                sf3.getLabelPaint().setTextSize(fontsize);
                // sf3.getLabelPaint().setDither(true);
                sf3.getLabelPaint().setFakeBoldText(true);
                SegmentFormatter sf4 =new SegmentFormatter(color_dark,color_white,color_white,color_white);
                //  SegmentFormatter sf4 = new SegmentFormatter(this, R.xml.pie_segment_formatter4);
                sf4.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf4.getFillPaint().setMaskFilter(emf);
                sf4.getLabelPaint().setTextSize(fontsize);
                //  sf4.getLabelPaint().setLinearText(true);
                sf4.getLabelPaint().setFakeBoldText(true);
                SegmentFormatter sf5 =new SegmentFormatter(color_light,color_white,color_white,color_white);
                sf5.getLabelPaint().setTextSize(fontsize);
                sf5.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf5.getFillPaint().setMaskFilter(emf);
                sf5.getLabelPaint().setFakeBoldText(true);
                //  sf5.getLabelPaint().setLetterSpacing(2f);

                //sf5.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf6 =new SegmentFormatter(color_medium,color_white,color_white,color_white);
                sf6.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf6.getLabelPaint().setTextSize(fontsize);
                sf6.getFillPaint().setMaskFilter(emf);
                sf6.getLabelPaint().setFakeBoldText(true);
                // sf6.getLabelPaint().setUnderlineText(true);

                //sf6.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf7 =new SegmentFormatter(color_hex,color_white,color_white,color_white);
                //    sf7.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf7.getLabelPaint().setTextSize(fontsize);
                sf7.getFillPaint().setMaskFilter(emf);
                //   sf7.getLabelPaint().setUnderlineText(true);
                sf7.getLabelPaint().setFakeBoldText(true);



                //sf6.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf8 =new SegmentFormatter(color_blue,color_white,color_white,color_white);
                //    sf7.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf8.getLabelPaint().setTextSize(fontsize);
                sf8.getFillPaint().setMaskFilter(emf);
                //   sf7.getLabelPaint().setUnderlineText(true);
                sf8.getLabelPaint().setFakeBoldText(true);
                sf8.setOffset(SELECTED_SEGMENT_OFFSET);


                Calendar calendar = Calendar.getInstance();

                String todaydate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


                for(int i=0;i<segmentlist.size();i++)
                {

/*
                    if((day==i )&&( segmentlist.size() != 7)){
                        piechart.addSegment(segmentlist.get(i),sf8);
                    }*/

                    if(earnings[i].getEndDate().equalsIgnoreCase(todaydate)){
                        piechart.addSegment(segmentlist.get(i),sf8);
                    }
                    else{
                    switch (i)
                    {
                        case 0 :piechart.addSegment(segmentlist.get(i),sf1);break;
                        case 1 :piechart.addSegment(segmentlist.get(i),sf2);break;
                        case 2 :piechart.addSegment(segmentlist.get(i),sf3);break;
                        case 3 :piechart.addSegment(segmentlist.get(i),sf4);break;
                        case 4 :piechart.addSegment(segmentlist.get(i),sf5);break;
                        case 5 :piechart.addSegment(segmentlist.get(i),sf6);break;
                        case 6 :piechart.addSegment(segmentlist.get(i),sf7);break;
                        default:
                            piechart.addSegment(segmentlist.get(i),sf1);break;
                    }
                    }

                }

                piechart.getBorderPaint().setColor(Color.TRANSPARENT);
                piechart.getBackgroundPaint().setColor(Color.TRANSPARENT);
                piechart.getLegend().setVisible(false);
//piechart.getLegend().setAnchor();
                hideProgress();
                setupIntroAnimation();
            }
        }
        catch (Exception exc)
        {
            hideProgress();
            exc.printStackTrace();
        }
    }


    protected void setupIntroAnimation() {

        final PieRenderer renderer = piechart.getRenderer(PieRenderer.class);
        // start with a zero degrees pie:

        renderer.setExtentDegs(0);
        // animate a scale value from a starting val of 0 to a final value of 1:
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        // use an animation pattern that begins and ends slowly:
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
                renderer.setExtentDegs(360 * scale);
                piechart.redraw();
            }
        });

        // the animation will run for 1.5 seconds:
        animator.setDuration(1500);
        animator.start();
    }










    @Subscribe
    public void onWeekEarningsClickEvent(WeekEarningsClickEvent event) {
        try {
     /*       setLoading();
            showProgress();*/
            webGetEarnings(event.getUrl());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
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
        try {
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
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv,"earnings");
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
                {
                    setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideProgress();
                            hideStatus2();
                            finish();
                        }
                    });
                }
                else
                {
                    hideProgress();
                    hideStatus2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }
}
