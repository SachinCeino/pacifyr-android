package com.pacifyr.omhealthcare.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.EmotionsListAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.modelAllEmotions;

public class CallSummaryActivity extends PacifyrAppCompactActivity {

    RecyclerView calsum_emotions_rcv;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<MEmotion> emotions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_summary);

        calsum_emotions_rcv=(RecyclerView) findViewById(R.id.calsum_emotions_rcv);
        calsum_emotions_rcv.setLayoutManager(new LinearLayoutManager(CallSummaryActivity.this,LinearLayoutManager.HORIZONTAL, false));
        NetworkService.webListEmotions(this);




    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        if(isValid(modelAllEmotions)) {

            emotions = modelAllEmotions.getResultsList();
            emotionsListAdapter = new EmotionsListAdapter(emotions, this);
            //postiveEmotions.setClickListener(this);
            calsum_emotions_rcv.setAdapter(emotionsListAdapter);
        }

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }
}
