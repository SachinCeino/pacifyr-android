package com.pacifyr.omhealthcare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.adapter.ListSavedCardAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

public class CrisisFragment extends BaseFragment {


    private CustomFontTextView paymentMethodCtxv;
    private CustomFontTextView credictCardCtxv;
    private PrefManager prefM;
    private String userToken,userid;

    private RecyclerView cardRcv;
    private ListSavedCardAdapter listSavedCardAdapter;

    public CrisisFragment() {
        // Required empty public constructor
    }


    public static CrisisFragment newInstance(String param1, String param2) {
        CrisisFragment fragment = new CrisisFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crisis, container, false);
    }


    private RelativeLayout suicideRlay;
    private CustomFontTextView suicideHeaderCtxv;
    private LinearLayout suicideUrlLlay;
    private CustomFontTextView suicideUrlCtxv;
    private RelativeLayout suicideTalkLlay;
    private ImageView suicideTalkImv;
    private CustomFontTextView suicideTalkCtxv;
    private CustomFontTextView suicideTtyCtxv;
    private LinearLayout suicideDescriptionLlay;
    private CustomFontTextView suicideDescriptionCtxv;
    private RelativeLayout samhsaRlay;
    private CustomFontTextView samhsaHeaderCtxv;
    private RelativeLayout samhsaTalkLlay;
    private ImageView samhsaTalkImv;
    private CustomFontTextView samhsaTalkCtxv;
    private CustomFontTextView samhsaTtyCtxv;
    private LinearLayout samhsaDescriptionLlay;
    private CustomFontTextView samhsaDescriptionCtxv;
    private RelativeLayout veteransRlay;
    private CustomFontTextView veteransHeaderCtxv;
    private LinearLayout veteransUrlLlay;
    private CustomFontTextView veteransUrlCtxv;
    private RelativeLayout veteransTalkLlay;
    private ImageView veteransTalkImv;
    private CustomFontTextView veteransTalkCtxv;
    private CustomFontTextView vetaransTtyCtxv;
    private LinearLayout veteransDescriptionLlay;
    private CustomFontTextView veteransDescriptionCtxv;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        suicideRlay = view.findViewById(R.id.suicide_rlay);
        suicideHeaderCtxv = view.findViewById(R.id.suicide_header_ctxv);
        suicideUrlLlay = view.findViewById(R.id.suicide_url_llay);
        suicideUrlCtxv = view.findViewById(R.id.suicide_url_ctxv);
        suicideTalkLlay = view.findViewById(R.id.suicide_talk_llay);
        suicideTalkImv = view.findViewById(R.id.suicide_talk_imv);
        suicideTalkCtxv = view.findViewById(R.id.suicide_talk_ctxv);
        suicideTtyCtxv = view.findViewById(R.id.suicide_tty_ctxv);
        suicideDescriptionLlay = view.findViewById(R.id.suicide_description_llay);
        suicideDescriptionCtxv = view.findViewById(R.id.suicide_description_ctxv);
        samhsaRlay = view.findViewById(R.id.samhsa_rlay);
        samhsaHeaderCtxv = view.findViewById(R.id.samhsa_header_ctxv);
        samhsaTalkLlay = view.findViewById(R.id.samhsa_talk_llay);
        samhsaTalkImv = view.findViewById(R.id.samhsa_talk_imv);
        samhsaTalkCtxv = view.findViewById(R.id.samhsa_talk_ctxv);
        samhsaTtyCtxv = view.findViewById(R.id.samhsa_tty_ctxv);
        samhsaDescriptionLlay = view.findViewById(R.id.samhsa_description_llay);
        samhsaDescriptionCtxv = view.findViewById(R.id.samhsa_description_ctxv);
        veteransRlay = view.findViewById(R.id.veterans_rlay);
        veteransHeaderCtxv = view.findViewById(R.id.veterans_header_ctxv);
        veteransUrlLlay = view.findViewById(R.id.veterans_url_llay);
        veteransUrlCtxv = view.findViewById(R.id.veterans_url_ctxv);
        veteransTalkLlay = view.findViewById(R.id.veterans_talk_llay);
        veteransTalkImv = view.findViewById(R.id.veterans_talk_imv);
        veteransTalkCtxv = view.findViewById(R.id.veterans_talk_ctxv);
        vetaransTtyCtxv = view.findViewById(R.id.vetarans_tty_ctxv);
        veteransDescriptionLlay = view.findViewById(R.id.veterans_description_llay);
        veteransDescriptionCtxv = view.findViewById(R.id.veterans_description_ctxv);


        suicideUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.suicide_http_url));
                startActivity(i);
            }
        });

        veteransUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.veterans_http_url));
                startActivity(i);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
