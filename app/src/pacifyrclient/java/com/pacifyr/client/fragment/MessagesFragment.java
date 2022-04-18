package com.pacifyr.client.fragment;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.twiliochat.QuickstartConversationsManager;
import com.pacifyr.client.twiliochat.TwilioChatListAdapter;
import com.pacifyr.client.twiliochat.TwilioChatParticipants;
import com.pacifyr.client.twiliochat.TwilioConversationActivity;
import com.pacifyr.client.twiliochat.TwilioConversationCreate;
import com.pacifyr.client.twiliochat.TwilioCoversationListActivity;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends BaseFragment {
    private RelativeLayout sendRlay;
    private MainTextView sendMsgCtxv;
    private CustomEditText messageEdit;

    private String messageText;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();
        // ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_support_title));

        if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
            ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
            return;
        }
        else
        {
            getConversationParticipantsList();
        }

    }

    private TextView mEmptyLayout;
    private RecyclerView RecyclerView_chatParticipants;
    private String userid ="";
    private PrefManager prefM;

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;
    private final QuickstartConversationsManager quickstartConversationsManager = new QuickstartConversationsManager();
    private TwilioChatListAdapter mTwilioChatListAdapter;
    int currentVisiblePosition = 0;
    private List<TwilioConversationCreate> results = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_messages, container, false);

        mEmptyLayout=root.findViewById(R.id.mEmptyLayout);
        RecyclerView_chatParticipants=root.findViewById(R.id.RecyclerView_chatParticipants);

        prefM = PrefManager.getInstance(getActivity());
        userid=prefM.getUserID();

        //retrieveTokenFromServer();
        setUpParticipantsListAdapter();
        return root;
    }

    public void getConversationParticipantsList(){
        setLoading();
        showProgress();

        String userToken=prefM.getAccessToken();
        String url = NetworkService.GLOBAL_URL+"messages/customer?skip=0&limit=25";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    Log.d("TWILIO", "Response from server: " + json);
                    Log.d("TWILIO", "Rqust user list userToken: " + userToken+ "url: " +url);
                    Gson gson = new Gson();
                    if (isValid(json))
                    {
                        TwilioChatParticipants mTwilioChatParticipants = gson.fromJson(json.toString(), TwilioChatParticipants.class);
                        if (isValid(mTwilioChatParticipants) && isValidSize(mTwilioChatParticipants.getResults()))
                        {
                            RecyclerView_chatParticipants.getRecycledViewPool().clear();
                            mEmptyLayout.setVisibility(View.GONE);
                            results.clear();
                            results.addAll(mTwilioChatParticipants.getResults());
                            mTwilioChatListAdapter.notifyDataSetChanged();
                            //setUpParticipantsListAdapter(mTwilioChatParticipants.getResults());
                        }
                        else
                        {
                            mEmptyLayout.setVisibility(View.VISIBLE);
                        }

                    }


                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        try {
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }


    private void setUpParticipantsListAdapter() {
        mTwilioChatListAdapter=new TwilioChatListAdapter(getActivity(),results,userid);
        mLinearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        RecyclerView_chatParticipants.setLayoutManager(mLinearLayoutManager);
        RecyclerView_chatParticipants.setAdapter(mTwilioChatListAdapter);

    }

    public void clearUnreadCount(TwilioConversationCreate item, int position){
        if (ConnectionCheckBroadcast.CheckInternetConnection(getActivity())) {
            if (isValid(item.getTwilioConversationId()))
            {
                results.get(position).getUnreadStatus().clear();
                mTwilioChatListAdapter.notifyItemChanged(position);

                TwilioConversationActivity.startWithoutFlag(getActivity(),item);

            }

        }else {

            ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
        }

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.messages));
            //((DashboardActivity) getActivity()).changeBottomMenuIcon(R.id.bnm_messages);
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
