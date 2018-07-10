package com.pacifyr.omhealthcare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.Payment.AddCreditCardActivity;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ListSavedCardAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class PaymentFragment extends BaseFragment {


    private CustomFontTextView paymentMethodCtxv;
    private CustomFontTextView credictCardCtxv;
    private PrefManager prefM;
    private String userToken,userid;

    private RecyclerView cardRcv;
    private ListSavedCardAdapter listSavedCardAdapter;

    public PaymentFragment() {
        // Required empty public constructor
    }


    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSavedCard();

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    private void getSavedCard() {

        prefM= PrefManager.getInstance(getActivity());
        userToken=prefM.getAccessToken();
        userid=prefM.getUserID();

        String url=GLOBAL_URL+"payment/savedcard/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                try {
                    if(isValid(json)) {
                        String jsonString = json.toString();
                        int l=jsonString.length();
                        if(l>3) {
                            credictCardCtxv.setVisibility(View.GONE);
                            cardRcv.setVisibility(View.VISIBLE);
                            listSavedCardAdapter = new ListSavedCardAdapter(getActivity(), json);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            cardRcv.setLayoutManager(mLayoutManager);
                            cardRcv.setItemAnimator(new DefaultItemAnimator());
                            cardRcv.setAdapter(listSavedCardAdapter);
                            //String message=json.getString("message");
                        }
                        else{
                            credictCardCtxv.setVisibility(View.VISIBLE);
                            cardRcv.setVisibility(View.GONE);
                        }
                    }else{
                        credictCardCtxv.setVisibility(View.VISIBLE);
                        cardRcv.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        aq.ajax(url,JSONArray.class, cb);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paymentMethodCtxv = (CustomFontTextView) view.findViewById(R.id.payment_method_ctxv);
        credictCardCtxv = (CustomFontTextView) view.findViewById(R.id.credict_card_ctxv);
        cardRcv = (RecyclerView) view.findViewById(R.id.card_rcv);

        credictCardCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),AddCreditCardActivity.class);
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
