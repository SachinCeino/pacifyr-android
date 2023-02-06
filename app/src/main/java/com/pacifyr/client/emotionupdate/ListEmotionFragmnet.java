package com.pacifyr.client.emotionupdate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.network.GsonUtils;

import org.json.JSONObject;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

public class ListEmotionFragmnet extends Fragment {

    RecyclerView positiveRcv;
    RecyclerView negativeRcv;
    private PrefManager prefM;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragent_list_emotion,container);
        View view = inflater.inflate(R.layout.fragent_list_emotion, container, false);
        prefM = PrefManager.getInstance(getActivity());
        negativeRcv = view.findViewById(R.id.negative_emotions_rcv);
        positiveRcv = view.findViewById(R.id.positive_emotions_rcv);

        setNegativeAdapter();
        setPositiveAdapter();

        return view;
    }

    public void setNegativeAdapter(){
        String url = GLOBAL_URL+"emotions/negative";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    //hideProgress();
                    MListNegativeEmotions modelListNegEmotions= GsonUtils.getInstance().gsonToMListNegativeEmotions(json);
                    negativeRcv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                    NegativeChangeEmotionsAdapter adapter = new NegativeChangeEmotionsAdapter(getActivity(),modelListNegEmotions.getResultsList());
                    negativeRcv.setAdapter(adapter);
                } catch (Exception e) {
                    //hideProgress();
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",prefM.getAccessToken());
        aq.ajax(url,JSONObject.class, cb);
    }

    public void setPositiveAdapter(){
        String url = GLOBAL_URL+"emotions/positive";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    MListPositiveEmotions modelListPosEmotions= GsonUtils.getInstance().gsonToMListPositiveEmotions(json);
                    positiveRcv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                    PostiveChangeEmotionsAdapter adapter = new PostiveChangeEmotionsAdapter(getActivity(),modelListPosEmotions.getResultsList());
                    positiveRcv.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        //cb.header("AccessToken",token);
        cb.header("AccessToken",prefM.getAccessToken());
        aq.ajax(url,JSONObject.class, cb);
    }
}
