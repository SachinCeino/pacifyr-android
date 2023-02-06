package com.pacifyr.client.home;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.EmotionsListAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelListPosEmotions;

public class CallSummaryActivity extends PacifyrAppCompactActivity {

    RecyclerView calsum_emotions_rcv;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<MEmotion> emotions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_summary);

        calsum_emotions_rcv = findViewById(R.id.calsum_emotions_rcv);
        calsum_emotions_rcv.setLayoutManager(new LinearLayoutManager(CallSummaryActivity.this,LinearLayoutManager.HORIZONTAL, false));
        if (isValid(NetworkService.modelListNegEmotions) && isValid(NetworkService.modelListPosEmotions)) {
            emotions = modelListPosEmotions.getResultsList();
            emotions.addAll(NetworkService.modelListNegEmotions.getResultsList());
            emotionsListAdapter = new EmotionsListAdapter(emotions, this);
            //postiveEmotions.setClickListener(this);
            calsum_emotions_rcv.setAdapter(emotionsListAdapter);
        } else {
            NetworkService.webListEmotions(this);
        }




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
