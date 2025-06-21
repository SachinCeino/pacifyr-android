package com.pacifyr.client.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CommentsAdapter;
import com.pacifyr.client.adapter.EmotionsListAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Utility;
import com.pacifyr.client.utils.WrappingSlidingPaneLayout;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONObject;

import java.util.List;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import rjsv.circularview.CircleView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.lastSession;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyr.client.network.NetworkService.webListEmotions;

public class LastSessionFragment extends BaseFragment {

    boolean showingFirst = true;
    CircleView circle_view;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;

    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private int changedRating;
    private int changedProgress;
    private String effectiveness;
    private Integer intEffectiveness;

    List<MEmotion> emotions;
    private CircleImageView pacifyrImgCimv;
    private MainTextView pacifyrNameCtxv;
    private ImageView beforeFeelDimv;
    private MainTextView durationCtxv;
    private MainTextView pacifyrRateCtxv;
    private MainTextView sessionCostCtxv;
    private int oldRating;
    private RelativeLayout submitRlay;

    public static MEmotion selEmotion;

    PrefManager prefM;
    String token;
    String userid;

    public MSessionRating[] editSessionObjectArray;
    public MSessionRating editSessionObject;
    private CustomEditText commentCedt;

    public LastSessionFragment() {
        // Required empty public constructor
    }

    public static LastSessionFragment newInstance(String param1, String param2) {
        LastSessionFragment fragment = new LastSessionFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        if(isValid(modelAllEmotions)) {
            emotions = modelAllEmotions.getResultsList();
            emotionsListAdapter = new EmotionsListAdapter(emotions, getActivity());
            //postiveEmotions.setClickListener(this);
            recyclerView.setAdapter(emotionsListAdapter);
        }
    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        webListEmotions(getActivity());
        View view=inflater.inflate(R.layout.fragment_last_sssion,container,false);
        onSetActionBar();
        pacifyrImgCimv = view.findViewById(R.id.last_session_cimv);
        submitRlay = view.findViewById(R.id.sumit_lastsesn_rlay);
        sessionCostCtxv = view.findViewById(R.id.session_cost_ctxv);
        pacifyrNameCtxv = view.findViewById(R.id.pacifyr_name_ctxv);
        beforeFeelDimv = view.findViewById(R.id.before_feel_dimv);
        commentCedt = view.findViewById(R.id.comments_cedt);
        recyclerView = view.findViewById(R.id.begning_rcv);
        commentsRcv = view.findViewById(R.id.comments_rcv);
        pacifyrRateCtxv = view.findViewById(R.id.pacifyr_rate_ctxv);
        durationCtxv = view.findViewById(R.id.duration_ctxv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        commentsRcv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        commentsAdapter=new CommentsAdapter(getContext());
        commentsRcv.setAdapter(commentsAdapter);

        ScaleRatingBar ratingBar = view.findViewById(R.id.simpleRatingBar5);
        ratingBar.setNumStars(5);
        ratingBar.setStarPadding(10);
        ratingBar.setClickable(true);
        ratingBar.setClearRatingEnabled(true);
        ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v, boolean fromUser) {
                //Log.e(TAG, "onRatingChange: " + rating);
                changedRating= (int) baseRatingBar.getRating();
            }
        });

        SeekBarCompat seekBarCompat = view.findViewById(R.id.materialSeekBar);
        //holoCircleSeekBar=(HoloCircleSeekBar) view.findViewById(R.id.picker);
        final CircleView circle_view = view.findViewById(R.id.circle_view);
        circle_view.setProgressStepAsInteger(true);
        seekBarCompat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circle_view.setProgressValue(progress);
                changedProgress = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(isValid(lastSession))
        {
            String imagePac;
            editSessionObjectArray=lastSession.getSessionRatings();
            for(int i=0;i<editSessionObjectArray.length;i++)
            {
                if(editSessionObjectArray[i].getType().equals("customer")){
                    editSessionObject=editSessionObjectArray[i];
                }
            }
            String beforeImgUrl = NetworkService.GLOBAL_IMAGE_URL+modelCustomerDashboard2.getUser().getEmotion().getImage();
            String pacName = lastSession.getPacifyr().getFirstName()+" "+lastSession.getPacifyr().getLastName();
            oldRating = Integer.parseInt(lastSession.getSessionRatings()[0].getRating());
            imagePac = NetworkService.GLOBAL_IMAGE_URL + lastSession.getPacifyr().getImage();

            if(isValid(editSessionObject.getEffectiveness())) {
                effectiveness = editSessionObject.getEffectiveness();
                intEffectiveness = Integer.parseInt(effectiveness);
                seekBarCompat.setProgress(intEffectiveness);
                circle_view.setProgressValue(intEffectiveness);
            }
            if(oldRating>0) {
                ratingBar.setRating(oldRating);
            }
            else {
                ratingBar.setRating(1);
            }
            pacifyrNameCtxv.setText(pacName);
            sessionCostCtxv.setText(" $ " + lastSession.getSessionCost());
            pacifyrRateCtxv.setText("$ " + lastSession.getPacifyr().getPrice() + " " + Utility.getCostLabel(getActivity()));
            durationCtxv.setText(lastSession.getDuration());
            if (isValidhtml(imagePac))
                Picasso.with(getContext()).load(imagePac).placeholder(com.pacifyrapp.utilitylibrary.R.drawable.placeholder_user).into(pacifyrImgCimv);
            if (isValidhtml(beforeImgUrl))
                Picasso.with(getContext()).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);

        }

        submitRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefM= PrefManager.getInstance(getActivity());
                token=prefM.getAccessToken();
                userid=prefM.getUserID();
                editSessionObject.setUserId(userid);
                //editSessionObject.setSessionId();
                editSessionObject.setRating(String.valueOf(changedRating));
                editSessionObject.setEffectiveness(String.valueOf(circle_view.getProgressValue()));
                //editSessionObject.setAfterSessionEmotionId();
                editSessionObject.setComment(commentCedt.getText().toString());
                if(isValid(selEmotion)){
                    editSessionObject.setAfterSessionEmotionId(selEmotion.getId());
                }
                saveSession();

                /*{"userId":"594a1a4095f8d11ff498aa90",
                        "sessionId":"596472c3dc224d16489de49b",
                        "rating":4,"effectiveness":75,
                        "afterSessionEmotionId":"5971c337734d1d6202a818b5",
                        "comment":""}*/
                
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void saveSession() {

        if (isValid(editSessionObject)) {
            Gson gson = new Gson();
            // 2. Java object to JSON, and assign to a String
            String jsonInString = gson.toJson(editSessionObject);


            String url = GLOBAL_URL + "rating/customer/session";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String js=json.toString();
                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        UserDashboardFragmentNew fragment=new UserDashboardFragmentNew();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment,fragment).commit();
                        //  postEventOnMainThread(new ActionbarEvent("MY DASHBOARD"));
                        //defSetText(titleTxv,"Dashboard");
                       } catch (Exception e) {
                        e.printStackTrace();//RespSearchPacifyrWOption
                    }
                }
            };
            try {
                AQuery aq = new AQuery(getActivity());
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                JSONObject jsonObject = new JSONObject(jsonInString);
                /*jsonObject.putOpt("initiatorId", userid);*/
                aq.post(url, jsonObject, JSONObject.class, cb);
                //aq.ajax(url,JSONObject.class, cb);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void defSetText(TextView textv, String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
        actionBarTopLlay = getActivity().findViewById(R.id.action_bar_top_llay);
        bottomPanel = getActivity().findViewById(R.id.bottom_panel);
        leftLlay = getActivity().findViewById(R.id.left_llay);
        backImv = getActivity().findViewById(R.id.back_imv);
        leftTxv = getActivity().findViewById(R.id.left_txv);
        titleTxv = getActivity().findViewById(R.id.title_txv);
        rightLlay = getActivity().findViewById(R.id.right_llay);
        rightTxv = getActivity().findViewById(R.id.right_txv);
        rightImv = getActivity().findViewById(R.id.right_imv);
        mSlidingPaneLayout = getActivity().findViewById(R.id.sliding_pane);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"Last Session");
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.leftmen));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingFirst == true){
                    mSlidingPaneLayout.openPane();
                    showingFirst = false;
                }else{
                    mSlidingPaneLayout.closePane();
                    showingFirst = true;
                }
            }
        });
    }


}
