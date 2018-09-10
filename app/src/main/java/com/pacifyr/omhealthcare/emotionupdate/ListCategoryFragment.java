package com.pacifyr.omhealthcare.emotionupdate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;

import static com.pacifyr.omhealthcare.emotionupdate.SelectEmotionActivity.speciality;

public class ListCategoryFragment extends Fragment {

    private RecyclerView detailRcv;
    private CustomFontTextView moreCtxv;
    private CustomFontTextView whyCtxv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragent_list_emotion_detail,container);

        detailRcv = (RecyclerView) view.findViewById(R.id.detail_rcv);
        moreCtxv = (CustomFontTextView) view.findViewById(R.id.more_ctxv);
        whyCtxv = (CustomFontTextView) view.findViewById(R.id.why_ctxv);



        detailRcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        EmotionCategoryAdapter adapter=new EmotionCategoryAdapter(getContext(),speciality.getResultsList());
        detailRcv.setAdapter(adapter);



        return view;
    }
}
