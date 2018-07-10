package com.pacifyr.omhealthcare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

        suicideRlay = (RelativeLayout) view.findViewById(R.id.suicide_rlay);
        suicideHeaderCtxv = (CustomFontTextView) view.findViewById(R.id.suicide_header_ctxv);
        suicideUrlLlay = (LinearLayout) view.findViewById(R.id.suicide_url_llay);
        suicideUrlCtxv = (CustomFontTextView) view.findViewById(R.id.suicide_url_ctxv);
        suicideTalkLlay = (RelativeLayout) view.findViewById(R.id.suicide_talk_llay);
        suicideTalkImv = (ImageView) view.findViewById(R.id.suicide_talk_imv);
        suicideTalkCtxv = (CustomFontTextView) view.findViewById(R.id.suicide_talk_ctxv);
        suicideTtyCtxv = (CustomFontTextView) view.findViewById(R.id.suicide_tty_ctxv);
        suicideDescriptionLlay = (LinearLayout) view.findViewById(R.id.suicide_description_llay);
        suicideDescriptionCtxv = (CustomFontTextView) view.findViewById(R.id.suicide_description_ctxv);
        samhsaRlay = (RelativeLayout) view.findViewById(R.id.samhsa_rlay);
        samhsaHeaderCtxv = (CustomFontTextView) view.findViewById(R.id.samhsa_header_ctxv);
        samhsaTalkLlay = (RelativeLayout) view.findViewById(R.id.samhsa_talk_llay);
        samhsaTalkImv = (ImageView) view.findViewById(R.id.samhsa_talk_imv);
        samhsaTalkCtxv = (CustomFontTextView) view.findViewById(R.id.samhsa_talk_ctxv);
        samhsaTtyCtxv = (CustomFontTextView) view.findViewById(R.id.samhsa_tty_ctxv);
        samhsaDescriptionLlay = (LinearLayout) view.findViewById(R.id.samhsa_description_llay);
        samhsaDescriptionCtxv = (CustomFontTextView) view.findViewById(R.id.samhsa_description_ctxv);
        veteransRlay = (RelativeLayout) view.findViewById(R.id.veterans_rlay);
        veteransHeaderCtxv = (CustomFontTextView) view.findViewById(R.id.veterans_header_ctxv);
        veteransUrlLlay = (LinearLayout) view.findViewById(R.id.veterans_url_llay);
        veteransUrlCtxv = (CustomFontTextView) view.findViewById(R.id.veterans_url_ctxv);
        veteransTalkLlay = (RelativeLayout) view.findViewById(R.id.veterans_talk_llay);
        veteransTalkImv = (ImageView) view.findViewById(R.id.veterans_talk_imv);
        veteransTalkCtxv = (CustomFontTextView) view.findViewById(R.id.veterans_talk_ctxv);
        vetaransTtyCtxv = (CustomFontTextView) view.findViewById(R.id.vetarans_tty_ctxv);
        veteransDescriptionLlay = (LinearLayout) view.findViewById(R.id.veterans_description_llay);
        veteransDescriptionCtxv = (CustomFontTextView) view.findViewById(R.id.veterans_description_ctxv);


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
