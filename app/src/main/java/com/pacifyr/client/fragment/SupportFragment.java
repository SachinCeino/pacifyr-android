package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


public class SupportFragment extends BaseFragment {

    private RelativeLayout sendRlay;
    private MainTextView sendMsgCtxv;
    private CustomEditText messageEdit;

    private String messageText;

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetailWeb(getUserID(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                Auth.httpStatusFilter(getContext(), status.getCode());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
       // ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_support_title));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendRlay = view.findViewById(R.id.send_rlay);
        sendMsgCtxv = view.findViewById(R.id.send_msg_ctxv);
        messageEdit = view.findViewById(R.id.message_edt);

        messageText=messageEdit.getText().toString();

        sendRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText=messageEdit.getText().toString();
                sendFeedbackMessage();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void sendFeedbackMessage() {
        try {
            if (isValid(messageText)) {
                sendRlay.setClickable(false);
                PrefManager prefM = PrefManager.getInstance(getContext());
                String token = prefM.getAccessToken();
                String userid = prefM.getUserID();
                String url = GLOBAL_URL + "report";
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            sendRlay.setClickable(true);
                            String js = json.toString();
                            setStatus("Your message is sent successfully", R.color.md_green_400);
                            messageEdit.setText("");
                            hideProgress();
                            showStatus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideStatus();
                                    // postEventOnMainThread(new ActionbarEvent("MY DASHBOARD"));
                                }
                            }, 2000);

                        } catch (Exception e) {
                            sendRlay.setClickable(true);
                            hideProgress();
                            e.printStackTrace();//RespSearchPacifyrWOption
                        }
                    }
                };
                try {
                    AQuery aq = new AQuery(getContext());
                    cb.header("Content-Type", "application/json");
                    cb.header("AccessToken", token);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("userId", userid);
                    jsonObject.putOpt("text", messageText);
//jsonObject.putOpt("price_start", 500);
//jsonObject.putOpt("price_end", 2000);

                    aq.post(url, jsonObject, JSONObject.class, cb);
//aq.ajax(url,JSONObject.class, cb);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendRlay.setClickable(true);
                }
            } else {
                showIOSDialogOK("Alert", "Please enter your message", "Ok");
            }
        }
        catch(Exception exx)
        {
            sendRlay.setClickable(true);
            exx.printStackTrace();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_support_title));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

}
