package com.pacifyr.omhealthcare.events.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.events.fragment.EventsListFragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class EventActivity extends PacifyerActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Bundle bundle = getIntent().getExtras();
        titleText = "Events";
        onSetActionBar();

        /**show first fragment  **/
        Fragment mEventsListFragment = new EventsListFragment();
        ShowEventFragments(mEventsListFragment);


    }

    public void ShowEventFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.event_fragment_containe, fragment);
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

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.event_fragment_containe);
        if (f instanceof EventsListFragment)
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
                if (f instanceof EventsListFragment)
                    finish();

                else {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
    }
}
