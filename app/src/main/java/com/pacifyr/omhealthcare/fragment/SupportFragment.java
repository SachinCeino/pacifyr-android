package com.pacifyr.omhealthcare.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.utility.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


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

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_support_title));

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
    }
    private void sendFeedbackMessage() {
        try {
            if (isValid(messageText)) {

                PrefManager prefM = PrefManager.getInstance(getContext());
                String token = prefM.getAccessToken();
                String userid = prefM.getUserID();
                String url = GLOBAL_URL + "report";
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
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
                }
            } else {
                showIOSDialogOK("Alert", "Please enter your message", "Ok");
            }
        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }
  /*  private void sendFeedbackMessage() {
        if(isValid(messageText)) {

            PrefManager prefM = PrefManager.getInstance(getContext());
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();

            String url = GLOBAL_URL + "report";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String js = json.toString();
                        setStatus("Your message send Succesfully", R.color.md_green_400);
                        showStatus();
                        messageEdit.setText("");

                        hideStatus();


                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        UserDashboardFragment fragment=new UserDashboardFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment,fragment).commit();

                    } catch (Exception e) {
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
            }
        }
        else{
            setStatus("Please enter a your message", R.color.md_amber_800);
            showStatus();
            hideStatus();
        }
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
