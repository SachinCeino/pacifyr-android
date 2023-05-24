package com.pacifyr.client.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.SessionPacks;
import com.pacifyr.client.network.WebService;
import com.pacifyr.client.utility.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionBundles extends BaseFragment {

    private static final String TAG = SessionBundles.class.getSimpleName();
    private final int skip = 0;
    private final int limit = 10;
    private WebService webService;
    private RecyclerView session_bundles_list;
    private View label_error;
    private String providerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            providerId = getArguments().getString("providerId", null);
        }

        webService = new WebService(getActivity());
        if (providerId != null) {
            webService.getBundles(skip, limit, providerId, new Callback<SessionPacks>() {
                @Override
                public void onResponse(Call<SessionPacks> call, Response<SessionPacks> response) {
                    if (response.isSuccessful()) {
                        SessionPacks body = response.body();
                        Log.d(TAG, "Response : " + body.getResults().size());
                    } else {
                        Log.d(TAG, "Response : no data found");
                        label_error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<SessionPacks> call, Throwable t) {
                    Log.d(TAG, "Response : no data found Err: " + t.getMessage());
                    label_error.setVisibility(View.VISIBLE);
                }
            });
        } else {
            label_error.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.session_bundles, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        session_bundles_list = view.findViewById(R.id.recycler_session_bundles);
        label_error = view.findViewById(R.id.labeL_error);
        session_bundles_list.setLayoutManager(manager);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.bundles));
            ((DashboardActivity) getActivity()).hideConnectButton();
        }
    }
}
