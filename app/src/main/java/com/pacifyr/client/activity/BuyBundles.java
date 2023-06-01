package com.pacifyr.client.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.SessionPackAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.SessionPackInput;
import com.pacifyr.client.model.SessionPacks;
import com.pacifyr.client.network.WebService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.PaginationScrollListener;
import com.pacifyrapp.utilitylibrary.model.response.ResponseMessage;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyBundles extends PacifyrAppCompactActivity {

    private static final String TAG = BuyBundles.class.getSimpleName();
    private final int limit = 10;
    private String providerId;
    private String providerName;
    private TextView title_txv;
    private View left_llay;
    private WebService webService;
    private int skip = 0;
    private TextView label_error;
    private String userId;
    private RecyclerView list_bundles;
    private SessionPackAdapter adapter;
    private boolean isLastPage = false;
    private boolean isLoading = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_bundles);
        userId = PrefManager.getInstance(this).getUserID();
        title_txv = findViewById(R.id.title_txv);
        left_llay = findViewById(R.id.left_llay);
        label_error = findViewById(R.id.labeL_error);
        list_bundles = findViewById(R.id.list_bundles);

        LinearLayoutManager manager = new LinearLayoutManager(BuyBundles.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        list_bundles.setLayoutManager(manager);
        list_bundles.addItemDecoration(new DividerItemDecoration(BuyBundles.this, RecyclerView.VERTICAL));

        webService = new WebService(this);
        if (getIntent().getExtras() != null) {
            providerId = getIntent().getExtras().getString("providerId");
            providerName = getIntent().getExtras().getString("providerName");

            if (providerName != null) {
                title_txv.setText(providerName);
            }
            if (providerId != null) {
                setLoading();
                showProgress();
                webService.getBundles(skip, limit, providerId, new Callback<SessionPacks>() {
                    @Override
                    public void onResponse(Call<SessionPacks> call, Response<SessionPacks> response) {
                        hideProgress();
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Get bundles successful");
                            adapter = new SessionPackAdapter(response.body().getResults(), new SessionPackAdapter.BuyListener() {
                                @Override
                                public void onBuy(String sessionPackId, boolean subscribed) {
                                    if (!subscribed) {
                                        buy(sessionPackId, false);
                                    } else {
                                        showIOSDialogOKCancel(getString(R.string.title_info), getString(R.string.session_pack_already_purchased), getString(R.string.buy_again), new iOSDialogClickListener() {
                                            @Override
                                            public void onClick(iOSDialog dialog) {
                                                buy(sessionPackId, false);
                                            }
                                        }, getString(R.string.cancel));
                                    }
                                }

                                @Override
                                public void onSubscribe(String sessionPackId, boolean subscribed) {
                                    if (!subscribed) {
                                        buy(sessionPackId, true);
                                    } else {
                                        showIOSDialogOKCancel(getString(R.string.title_info), getString(R.string.session_pack_already_purchased), getString(R.string.buy_again), new iOSDialogClickListener() {
                                            @Override
                                            public void onClick(iOSDialog dialog) {
                                                buy(sessionPackId, false);
                                            }
                                        }, getString(R.string.cancel));
                                    }
                                }
                            });
                            list_bundles.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Get bundles failed");
                            label_error.setVisibility(View.VISIBLE);
                            label_error.setText(getString(R.string.no_session_bundles_found));
                        }
                    }

                    @Override
                    public void onFailure(Call<SessionPacks> call, Throwable t) {
                        hideProgress();
                        Log.e(TAG, "Get bundles failed, error : " + t.getMessage());
                        label_error.setVisibility(View.VISIBLE);
                        label_error.setText(getString(R.string.no_session_bundles_found));
                    }
                });

                list_bundles.addOnScrollListener(new PaginationScrollListener(manager) {
                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;
                        skip += limit;

                        webService.getBundles(skip, limit, providerId, new Callback<SessionPacks>() {
                            @Override
                            public void onResponse(Call<SessionPacks> call, Response<SessionPacks> response) {
                                isLoading = false;
                                if (response.isSuccessful() && response.body().getResults().size() > 0) {
                                    Log.d(TAG, "Get bundles successful");
                                    if (adapter != null) {
                                        adapter.setItems(response.body().getResults(), false);
                                    }
                                } else if (response.body().getResults().size() == 0) {
                                    Log.d(TAG, "Get bundles failed");
                                    isLastPage = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<SessionPacks> call, Throwable t) {
                                isLoading = false;
                                isLastPage = true;
                                Log.e(TAG, "Get bundles failed, error : " + t.getMessage());
                            }
                        });

                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });
            }
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(android.R.id.content);
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    private void buy(String sessionPackId, boolean isSubscribed) {
        showProgress();
        SessionPackInput input = new SessionPackInput(userId, sessionPackId, isSubscribed);
        WebService webService = new WebService(BuyBundles.this);
        webService.subscribeBundle(input, new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    String message = "";
                    if (isSubscribed) {
                        message = getString(R.string.session_pack_subscribed);
                    } else {
                        message = getString(R.string.session_pack_purchased);
                    }
                    showIOSDialogOK(getString(R.string.success), message, getString(R.string.dlg_ok), new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    showIOSDialogOK(getString(R.string.error), getString(R.string.session_pack_purchase_failed), getString(R.string.dlg_ok), new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "Bundle purchase error : " + t.getMessage());
                showIOSDialogOK(getString(R.string.error), getString(R.string.session_pack_purchase_failed), getString(R.string.dlg_ok), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
