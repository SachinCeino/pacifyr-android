package com.pacifyr.omhealthcare.events.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.events.fragment.EventsListFragment;
import com.pacifyr.omhealthcare.events.fragment.OfferssListFragment;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.events.model.ModelOffer;
import com.pacifyr.omhealthcare.network.GsonUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class OffersActivity extends PacifyerActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, OffersActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String titleText;
    private RelativeLayout empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        empty_layout = findViewById(R.id.empty_layout);
        empty_layout.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        titleText = "OFFERS";
        onSetActionBar();

        getWebData();

    }


    public void ShowOffersFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.offer_fragment_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    public void changeTitle(String title) {

        if (isValid(titleTxv) && isValid(title)) {
            titleTxv.setVisibility(View.VISIBLE);
            defSetText(titleTxv, title);
        }

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.offer_fragment_container);
        if (f instanceof EventsListFragment || empty_layout.getVisibility() == View.VISIBLE)
            finish();

        else {
            getSupportFragmentManager().popBackStackImmediate();
        }

    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    ///ACTION BAR FEATURES
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        if (isValid(titleText))
            titleTxv.setText(titleText);
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.event_fragment_containe);
                if (f instanceof OfferssListFragment || empty_layout.getVisibility() == View.VISIBLE)
                    finish();

                else {
                    getSupportFragmentManager().popBackStackImmediate();
                }

            }
        });
    }


    public void getWebData() {

        setLoading();
        showProgress();

        String token = getPrefManager().getAccessToken();

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {

                    hideProgress();
                    if (isValid(json)) {
                        ModelOffer data = GsonUtils.getInstance().gsonToModelOffer(json);
                        if (isValid(data)) {
                            List<ModelOffer.Result> listOffer = data.getResults();
                            /**show EventsListFragment  **/
                            if (isValid(listOffer)) {
                                empty_layout.setVisibility(View.GONE);
                                Fragment mOfferssListFragment = new OfferssListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", (Serializable) listOffer);
                                mOfferssListFragment.setArguments(bundle);
                                ShowOffersFragments(mOfferssListFragment);

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        if (isValid(status.getMessage())) {
                            empty_layout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } catch (Exception e) {

                    hideProgress();
                    empty_layout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "offer/list/byUser?userId=" + getPrefManager().getUserID() + "&skip=0&limit=10", JSONObject.class, cb);

        } catch (Exception ex) {
            hideProgress();

            ex.printStackTrace();
        }

    }

}
