package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.help.HelpsViewPagerAdapter;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.google.android.material.tabs.TabLayout;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
public class HelpActvity extends PacifyrActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, HelpActvity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String titleText;

    private TabLayout _mTabLayout_help;
    private ViewPager __mViewPager_MyInterviews;
    private HelpsViewPagerAdapter _MyInterViewsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        _mTabLayout_help = findViewById(R.id._mTabLayout_help);
        __mViewPager_MyInterviews = findViewById(R.id._mViewPager_MyInterviews);

        titleText ="HELP";

        onSetActionBar();

        _MyInterViewsViewPagerAdapter = new HelpsViewPagerAdapter(getSupportFragmentManager());
        __mViewPager_MyInterviews.setAdapter(_MyInterViewsViewPagerAdapter);
        _mTabLayout_help.setupWithViewPager(__mViewPager_MyInterviews);


    }
    public void changeActionBarTitleForFragments(String mActionBarTitle)
    {
        defSetText(titleTxv, mActionBarTitle);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
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
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (MainTextView) findViewById(R.id.left_txv);
        titleTxv = (MainTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (MainTextView) findViewById(R.id.right_txv);
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

                finish();

            }
        });
    }


}
