package com.pacifyr.client.emotionupdate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.R;

import static com.pacifyr.client.emotionupdate.SelectEmotionActivity.speciality;

public class ListCategoryFragment extends Fragment {

    private RecyclerView detailRcv;
    private MainTextView moreCtxv;
    private MainTextView whyCtxv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragent_list_emotion_detail,container);

        detailRcv = view.findViewById(R.id.detail_rcv);
        moreCtxv = view.findViewById(R.id.more_ctxv);
        whyCtxv = view.findViewById(R.id.why_ctxv);



        detailRcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        EmotionCategoryAdapter adapter=new EmotionCategoryAdapter(getContext(),speciality.getResultsList());
        detailRcv.setAdapter(adapter);



        return view;
    }
}
