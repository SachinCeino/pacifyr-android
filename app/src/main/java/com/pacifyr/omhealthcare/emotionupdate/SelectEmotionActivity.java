
package com.pacifyr.omhealthcare.emotionupdate;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.EmotionSpecialityAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.ConnectToSinglePacifyrActivity;
import com.pacifyr.omhealthcare.model.ModelSpecialityByEmotion;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

public class SelectEmotionActivity extends PacifyrAppCompactActivity {


    public  static ModelSpecialityByEmotion speciality;


    public static void start(Context context) {
        Intent intent = new Intent(context, SelectEmotionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_emotion);
        ListEmotionFragmnet listEmotionFragmnet = new ListEmotionFragmnet();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_rlay,listEmotionFragmnet);
        transaction.commit();

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

        speciality = event.specialityByEmotion;
        FragmentManager manager = getFragmentManager();
        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_rlay,listCategoryFragment,"SELECT YOUR REASON");
        transaction.commit();

        /*if(event.emotionType.equals("positive")){

        }else if(event.emotionType.equals("negative")){
        }*/

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

}
