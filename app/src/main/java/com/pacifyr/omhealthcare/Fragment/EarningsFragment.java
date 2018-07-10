package com.pacifyr.omhealthcare.Fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MEarningsRoot;
import com.ceino.utilitylibrary.model.pacifyr.Mbonus;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.EarningWeekAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.event.WeekEarningsClickEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pacifyr.omhealthcare.network.NetworkService.modelEarningsAPI;

/*import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;*/


public class EarningsFragment extends BaseFragment{

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



    CustomFontTextView wkly_total_ctxv; RecyclerView list_weeks_rcv;
    CustomFontTextView text_dialog_hourstowork,text_dialog_bonusmessage,text_dialog_hourstobonus;

    public EarningsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_earnings, container, false);
       // pieChart = (PieChart) view.findViewById(R.id.piechart);

        pieINitial(view);
        wkly_total_ctxv= (CustomFontTextView) view.findViewById(R.id.wkly_total_ctxv);

        text_dialog_hourstowork = (CustomFontTextView) view.findViewById(R.id.text_dialog_hourstowork);
        text_dialog_hourstobonus = (CustomFontTextView) view.findViewById(R.id.text_dialog_hourstobonus);
        text_dialog_bonusmessage = (CustomFontTextView) view.findViewById(R.id.text_dialog_bonusmessage);




        webGetEarnings();
        webGetBonus();



        list_weeks_rcv= (RecyclerView) view.findViewById(R.id.list_weeks_rcv);
        LinearLayoutManager horizontalLayoutmanager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        list_weeks_rcv.setLayoutManager(horizontalLayoutmanager);
        list_weeks_rcv.setVisibility(View.VISIBLE);




        EarningWeekAdapter adapter =new EarningWeekAdapter(getActivity());
        list_weeks_rcv.setAdapter(adapter);
        if(isValid(adapter.getItemCount()))
        list_weeks_rcv.scrollToPosition(adapter.getItemCount()-1);
       // pieChart.setUsePercentValues(true);




        //setChartData();
       /* If you want to display chart in percentage
        then pass new PercentFormatter() as a parameter
        otherwise pass new DefaultValueFormatter(0).new DefaultValueFormatter(0) parameter show data value.
        */

        return view;
    }

    private void webGetBonus() {


        LocalDate now = new LocalDate();
        LocalDate startofWeek = now.withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate startofWeek_sun = startofWeek.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");
        String fromDateWeek = formatter.print(startofWeek_sun);

        String displayFrom = displayFormatter.print(startofWeek_sun);
        String displayTo = displayFormatter.print(now);

        String toDate = formatter.print(now);
        /*setLoading();
        showProgress();*/
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
/*
                hideProgress();
*/

                try {
                    if(json != null){
                        try {

                            mbonus = new Mbonus();

                            mbonus= GsonUtils.getInstance().gsonToMbonus(json.getJSONObject("bonus"));

                            setbonusData();




                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }else{

                        //ajax error, show error code
                        Toast.makeText(getActivity(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(thisWeekUrl,JSONObject.class, cb);
    }


    private void setbonusData() {

        try {
            if (isValid(mbonus)) {
/*
                setLoading();
*/

                Float total= Float.valueOf(0);

                int toBonus = mbonus.getToBonus();
                String message =mbonus.getMessage();
                int hoursThisWeek = mbonus.getHoursThisWeek();

                text_dialog_hourstowork.setText(String.valueOf(hoursThisWeek));
                text_dialog_hourstobonus.setText(String.valueOf(toBonus));

                text_dialog_bonusmessage.setText(message);

/*
                hideProgress();
*/
            }
        }
        catch (Exception exc)
        {
/*
            hideProgress();
*/
            exc.printStackTrace();
        }
    }




    private void pieINitial(View view) {
        try{
            piechart = (com.androidplot.pie.PieChart) view.findViewById(R.id.piechart);

            // enable the legend:
            piechart.getLegend().setVisible(true);

            final float padding = PixelUtils.dpToPix(30);
            piechart.getPie().setPadding(padding, padding, padding, padding);

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


                        int color_light=getResources().getColor(R.color.graph_light);
                        int color_hex=getResources().getColor(R.color.grey_hex_c6);
                        int color_dark=getResources().getColor(R.color.graph_dark);


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

/*        LocalDate now = new LocalDate();
       // LocalDate startofWeek=now.withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate startofWeek=now.withDayOfWeek(DateTimeConstants.SUNDAY);
        LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
        LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String fromDateWeek = formatter.print(startofWeek);
        String fromDateMonth = formatter.print(startofMonth);
        String fromDateYear = formatter.print(startofYear);
        String toDate = formatter.print(now)*/;


        LocalDate now = new LocalDate();
        LocalDate startofWeek = now.withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate startofWeek_sun = startofWeek.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");
        String fromDateWeek = formatter.print(startofWeek_sun);

        String displayFrom = displayFormatter.print(startofWeek_sun);
        String displayTo = displayFormatter.print(now);

        String toDate = formatter.print(now);

        setLoading();
        showProgress();
        PrefManager prefM= PrefManager.getInstance(getContext());
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
        AQuery aq = new AQuery(getContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(thisWeekUrl,JSONObject.class, cb);
    }

    private void webGetEarnings(String url) {

        LocalDate now = new LocalDate();
        LocalDate startofWeek=now.withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
        LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String fromDateWeek = formatter.print(startofWeek);
        String fromDateMonth = formatter.print(startofMonth);
        String fromDateYear = formatter.print(startofYear);
        String toDate = formatter.print(now);


        setLoading();
        showProgress();
        PrefManager prefM= PrefManager.getInstance(getContext());
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
        AQuery aq = new AQuery(getContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NetworkService.webEarningsAPI(getActivity());
    }



    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        setChartData();
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

                String week[]={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

               Float total= Float.valueOf(0);
                MEarningsRoot.Earnings[] earnings=modelEarningsAPI.getEarnings();
                int size=earnings.length;
                try {
                    for (int i = 0; i < size; i++) {
                        total = total + earnings[i].getEarnings();

                    }

                    for (int i = 0; i < size; i++) {
                        // int k=i+1;
                        int color=getActivity().getResources().getColor(R.color.grey_hex_c6);



                        float perentage=earnings[i].getEarnings();
                     /*   if(total>0)
                        {
                            perentage=  (earnings[i].getEarnings()/total)*100;
                        }
                        else {
                            perentage=100/size;
                        }*/

                        if(total<=0)
                        {
                            perentage=100/size;

                            Segment model = new Segment(week[i] + "   "+Utility.getPriceSign(getActivity())+ "0.0 " , perentage);
                            segmentlist.add(model);
                        }
                        else {
                            if(perentage>0) {
                                Segment model = new Segment(week[i] + "   "+Utility.getPriceSign(getActivity())+"" + perentage, perentage);
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
                wkly_total_ctxv.setText(Utility.getPriceSign(getActivity())+" "+ String.format("%.2f", total));

                EmbossMaskFilter emf = new EmbossMaskFilter(
                        new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

                int color_medium=getActivity().getResources().getColor(R.color.medium_grey_pacifyr);
                int color_light=getActivity().getResources().getColor(R.color.graph_light);
                int color_hex=getActivity().getResources().getColor(R.color.grey_hex_c6);
                int color_dark=getActivity().getResources().getColor(R.color.graph_dark);
                int color_white=getActivity().getResources().getColor(R.color.white);
                int color_blue=getResources().getColor(R.color.blue_pacifyr);

                SegmentFormatter sf1 =new SegmentFormatter(color_light,color_white,color_white,color_white);
              //  SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);
                sf1.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf1.getFillPaint().setMaskFilter(emf);
                sf1.getLabelPaint().setFakeBoldText(true);
              //  sf1.getLabelPaint().setSubpixelText(true);
                sf1.getLabelPaint().setTextSize(35.0f);

                SegmentFormatter sf2 =new SegmentFormatter(color_hex,color_white,color_white,color_white);
              //  SegmentFormatter sf2 = new SegmentFormatter(this, R.xml.pie_segment_formatter2);
                sf2.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf2.getFillPaint().setMaskFilter(emf);
                sf2.getLabelPaint().setFakeBoldText(true);
                sf2.getLabelPaint().setTextSize(35.0f);
                 /**/
                SegmentFormatter sf3 =new SegmentFormatter(color_medium,color_white,color_white,color_white);
              ///  SegmentFormatter sf3 = new SegmentFormatter(this, R.xml.pie_segment_formatter3);
           sf3.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf3.getFillPaint().setMaskFilter(emf);
                sf3.getLabelPaint().setTextSize(35.0f);
               // sf3.getLabelPaint().setDither(true);
                sf3.getLabelPaint().setFakeBoldText(true);
                SegmentFormatter sf4 =new SegmentFormatter(color_dark,color_white,color_white,color_white);
              //  SegmentFormatter sf4 = new SegmentFormatter(this, R.xml.pie_segment_formatter4);
            sf4.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf4.getFillPaint().setMaskFilter(emf);
                sf4.getLabelPaint().setTextSize(35.0f);
              //  sf4.getLabelPaint().setLinearText(true);
                sf4.getLabelPaint().setFakeBoldText(true);
                SegmentFormatter sf5 =new SegmentFormatter(color_light,color_white,color_white,color_white);
                sf5.getLabelPaint().setTextSize(35.0f);
           sf5.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf5.getFillPaint().setMaskFilter(emf);
                sf5.getLabelPaint().setFakeBoldText(true);
              //  sf5.getLabelPaint().setLetterSpacing(2f);

                //sf5.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf6 =new SegmentFormatter(color_medium,color_white,color_white,color_white);
                sf6.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf6.getLabelPaint().setTextSize(35.0f);
                sf6.getFillPaint().setMaskFilter(emf);
                sf6.getLabelPaint().setFakeBoldText(true);
               // sf6.getLabelPaint().setUnderlineText(true);

                //sf6.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf7 =new SegmentFormatter(color_hex,color_white,color_white,color_white);
            //    sf7.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf7.getLabelPaint().setTextSize(35.0f);
                sf7.getFillPaint().setMaskFilter(emf);
             //   sf7.getLabelPaint().setUnderlineText(true);
                sf7.getLabelPaint().setFakeBoldText(true);

                // sf7.getFillPaint().setMaskFilter(emf);



                //sf6.getFillPaint().setMaskFilter(emf);
                SegmentFormatter sf8 =new SegmentFormatter(color_blue,color_white,color_white,color_white);
                //    sf7.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
                sf8.getLabelPaint().setTextSize(35.0f);
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
                    }else{
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
/*    private void setChartData() {
        try {
            if (isValid(modelEarningsAPI)) {
                setLoading();
                pieChart.clearChart();
                ArrayList<PieModel> piemodelslist = new ArrayList<PieModel>();
               // ArrayList<Entry> yvalues = new ArrayList<Entry>();
                //ArrayList<String> xVals = new ArrayList<String>();
                //hideProgress();
                String week[]={"M","Tu","W","Th","F","Sa","Su"};
                Float total= Float.valueOf(0);
                MEarningsRoot.Earnings[] earnings=modelEarningsAPI.getEarnings();
                int size=earnings.length;
                try {
                    for (int i = 0; i < size; i++) {
                        total = total + earnings[i].getEarnings();
                    }

                    for (int i = 0; i < size; i++) {
                       // int k=i+1;
                        int color=getActivity().getResources().getColor(R.color.grey_hex_c6);
                      *//*  if(k%3==0)
                            color=getActivity().getResources().getColor(R.color.graph_dark);
                        else if(k%2==0)
                            color=getActivity().getResources().getColor(R.color.grey_hex_c6);
                        else
                            color=getActivity().getResources().getColor(R.color.graph_light);*//*


                        switch (i)
                        {
                            case 0 :color=getActivity().getResources().getColor(R.color.graph_light);break;
                            case 1 :color=getActivity().getResources().getColor(R.color.grey_hex_c6);break;
                            case 2 :color=getActivity().getResources().getColor(R.color.graph_dark);break;
                            case 3 :color=getActivity().getResources().getColor(R.color.graph_light);break;
                            case 4 :color=getActivity().getResources().getColor(R.color.grey_hex_c6);break;
                            case 5 :color=getActivity().getResources().getColor(R.color.graph_dark);break;
                            case 6 :color=getActivity().getResources().getColor(R.color.graph_light);break;
                            default:
                                color=getActivity().getResources().getColor(R.color.grey_hex_c6);break;
                        }
                        float perentage=earnings[i].getEarnings();
                        if(total>0)
                        {
                           perentage=  (earnings[i].getEarnings()/total)*100;
                        }
                        else {
                            perentage=100/size;
                        }

                        PieModel model=new PieModel(week[i]+"", perentage,color);
                        piemodelslist.add(model);
                    }
                }
                catch (Exception excc)
                {
                    excc.printStackTrace();
                }
                int b=Math.round(total);
                wkly_total_ctxv.setText(Integer.toString(b)+" "+ Utility.getPriceSign(getActivity()));

                for(int i=0;i<piemodelslist.size();i++)
                {
                    pieChart.addPieSlice(piemodelslist.get(i));
                }

                pieChart.startAnimation();
                hideProgress();
                //pieChart.setOnChartValueSelectedListener(this);

            }
        }
        catch (Exception exc)
        {
            hideProgress();
            exc.printStackTrace();
        }
    }*/
   /* private void setChartData() {
        try {
            if (isValid(modelEarningsAPI)) {
                setLoading();
                PieDataSet dataSet;
                //private PieChartView chart;
                PieData data;
                ArrayList<Entry> yvalues = new ArrayList<Entry>();
                ArrayList<String> xVals = new ArrayList<String>();
                //hideProgress();
                String week[]={"M","Tu","W","Th","F","Sa","Su"};
                Float total= Float.valueOf(0);
                MEarningsRoot.Earnings[] earnings=modelEarningsAPI.getEarnings();
                int size=earnings.length;
                try {
                    for (int i = 0; i < 7; i++) {
                        yvalues.add(new Entry(earnings[i].getEarnings(), i));
                        total = total + earnings[i].getEarnings();
                        xVals.add(week[i]);
                    }
                }
                catch (Exception excc)
                {
                    excc.printStackTrace();
                }
                int b=Math.round(total);
                wkly_total_ctxv.setText(Integer.toString(b)+" "+ Utility.getPriceSign(getActivity()));
               *//* yvalues.add(new Entry(8f, 0));
                yvalues.add(new Entry(15f, 1));
                yvalues.add(new Entry(12f, 2));
                yvalues.add(new Entry(25f, 3));
                yvalues.add(new Entry(23f, 4));
                yvalues.add(new Entry(17f, 5));
                yvalues.add(new Entry(17f, 6));*//*
               *//* xVals.add("Monday");
                xVals.add("Tuesday");
                xVals.add("Wednesday");
                xVals.add("Thirsday");
                xVals.add("Friday");
                xVals.add("Saturday");
                xVals.add("Sunday");*//*
                dataSet= new PieDataSet(yvalues, "Earnings");
                data = new PieData(xVals, dataSet);

                //data.setValueFormatter(new PercentFormatter());
                data.setValueFormatter(new DecimalValueFormatter());
                //arDataSet set1 = new BarDataSet(yVals1, " ");
                dataSet.setColors(new int[]{getResources().getColor(R.color.graph_dark),getResources().getColor(R.color.grey_hex_c6), getResources().getColor(R.color.graph_light)});
                // Default value
                //data.setValueFormatter(new DefaultValueFormatter(0));
                //dataSet.setColors(ColorTemplate.PASTEL_COLORS);
                data.setValueTextSize(15f);
                data.setValueTextColor(Color.DKGRAY);
                dataSet.setHighlightEnabled(true);

                pieChart.setDrawHoleEnabled(true);
                pieChart.setTransparentCircleRadius(30f);
                pieChart.setHoleRadius(50f);
                pieChart.setData(data);
                pieChart.setOnChartValueSelectedListener(this);
                pieChart.animateXY(500, 2000);
                pieChart.setDescription("");
                pieChart.invalidate();
                hideProgress();
                //pieChart.setOnChartValueSelectedListener(this);

            }
        }
        catch (Exception exc)
        {
            hideProgress();
            exc.printStackTrace();
        }
    }*/
/*    public interface OnChartValueSelectedListener {
        *//**
         * Called when a value has been selected inside the chart.
         *
         * @param e The selected Entry.
         * @param h The corresponding highlight object that contains information
         * about the highlighted position
         *//*
        public void onValueSelected(Entry e, Highlight h);
        *//**
         * Called when nothing has been selected or an "un-select" has been made.
         *//*
        public void onNothingSelected();
    }*/

    @Override
    public void onSetValues(View view) throws Exception {

    }



   /* @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        if (entry == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + entry.getVal() + ", xIndex: " + entry.getXIndex()
                        + ", DataSet index: " + highlight.toString());
        Toast.makeText(getActivity(),String.valueOf(entry.getVal()),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
  /*  public class DecimalValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public DecimalValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " "; // e.g. append a dollar-sign
        }
    }*/
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
}
