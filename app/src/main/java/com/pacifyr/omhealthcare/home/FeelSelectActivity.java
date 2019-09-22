package com.pacifyr.omhealthcare.home;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.NegativeEmotionsAdapter;
import com.pacifyr.omhealthcare.adapter.PostiveEmotions;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import static com.pacifyr.omhealthcare.network.NetworkService.modelListNegEmotions;
import static com.pacifyr.omhealthcare.network.NetworkService.modelListPosEmotions;

public class FeelSelectActivity extends PacifyrAppCompactActivity {

    PostiveEmotions postiveEmotions;
    NegativeEmotionsAdapter negativeEmotionsAdapter;
    ArrayList<MEmotion> list=new ArrayList<>();
    ArrayList<MEmotion> list2=new ArrayList<>();
    RecyclerView recyclerView,recyclerView2;

    public static String PAC_EMOTIONS_IMAGE,PAC_EMOTIONS_TITLE;
    public static Boolean IS_NEG_SELECTED=false,IS_POS_SELECTED=false,ADAPTER_RUN=false,ADAPTER_RUN2=false;

    boolean showingFirst = true;
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feel_select);
        NetworkService.webListPositiveEmotions(this);
        NetworkService.webListNegativeEmotions(this);
        onSetActionBar();
        recyclerView = findViewById(R.id.positive_rcv);
        recyclerView2 = findViewById(R.id.negative_rcv);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(com.pacifyr.omhealthcare.event.NotifyEvent event) {

        if(isValid(modelListPosEmotions)) {
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            list = modelListPosEmotions.getResultsList();
            postiveEmotions = new PostiveEmotions(this,FeelSelectActivity.this,list);
            //postiveEmotions.setClickListener(this);
            recyclerView.setAdapter(postiveEmotions);
            ADAPTER_RUN=true;

        }
        if (isValid(modelListNegEmotions))
        {
            int numberOfColumns = 4;
            recyclerView2.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            list2 = modelListNegEmotions.getResultsList();
            negativeEmotionsAdapter = new NegativeEmotionsAdapter(this,FeelSelectActivity.this, list2);
            recyclerView2.setAdapter(negativeEmotionsAdapter);
            ADAPTER_RUN2=true;
        }
        if(IS_NEG_SELECTED||IS_POS_SELECTED){
            finish();
            ADAPTER_RUN=false;
            IS_NEG_SELECTED=false;
            IS_POS_SELECTED=false;
            ADAPTER_RUN2=false;
        }
    }

   /* @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        if(isValid(modelListPosEmotions)) {
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            list = modelListPosEmotions.getResultsList();
            postiveEmotions = new PostiveEmotions(this,FeelSelectActivity.this,list);
            //postiveEmotions.setClickListener(this);
            recyclerView.setAdapter(postiveEmotions);
            ADAPTER_RUN=true;

        }
        if (isValid(modelListNegEmotions))
        {
            int numberOfColumns = 4;
            recyclerView2.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            list2 = modelListNegEmotions.getResultsList();
            negativeEmotionsAdapter = new NegativeEmotionsAdapter(this,FeelSelectActivity.this, list2);
            recyclerView2.setAdapter(negativeEmotionsAdapter);
            ADAPTER_RUN2=true;
        }
        if(IS_NEG_SELECTED||IS_POS_SELECTED){
            finish();
            ADAPTER_RUN=false;
            IS_NEG_SELECTED=false;
            IS_POS_SELECTED=false;
            ADAPTER_RUN2=false;
        }
    }*/

    @Subscribe
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

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
        //leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"how you feel");
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
