package com.ceino.utilitylibrary.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.LogUtilsutility;
import com.ceino.utilitylibrary.PagerEnabledSlidingPaneLayout;
import com.ceino.utilitylibrary.R;


/**
 * Created by ceino on 27/1/17.
 */

public class BaseSideMenuActivity extends BaseAppCompactActivity {

    private RelativeLayout sidemenuActionBar;
    private PagerEnabledSlidingPaneLayout slidingPane;
    private LinearLayout leftPane;
   /* private RelativeLayout bannerRlay;
    private CircleImageView parentCimv;
    private CustomFontTextView parentNameCtxv;
    private RelativeLayout sideInviteRlay;
    private DeviceFitImageView sideInviteDimv;
    private CustomFontTextView sideInviteDtxv;
    private RelativeLayout sideAccountRlay;
    private DeviceFitImageView sideAccountDimv;
    private CustomFontTextView sideAccountDtxv;
    private RelativeLayout sideSupportRlay;
    private DeviceFitImageView sideSupportDimv;
    private CustomFontTextView sideSupportDtxv;
    private RelativeLayout sideSchoolRlay;
    private DeviceFitImageView sideSchoolDimv;
    private CustomFontTextView sideSchoolDtxv;
    private RelativeLayout sideClassRlay;
    private DeviceFitImageView sideClassDimv;
    private CustomFontTextView sideClassDtxv;*/
    //private RecyclerView childRcv;
    private FrameLayout sidemenuContentFlay;
    private LinearLayout left_content_pane_llay;
    public boolean isOpenPane=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_sidemenu);
        try {
        sidemenuActionBar = (RelativeLayout) findViewById(R.id.sidemenu_action_bar);
            sidemenuContentFlay = (FrameLayout) findViewById(R.id.sidemenu_content_flay);
            left_content_pane_llay = (LinearLayout) findViewById(R.id.left_content_pane_llay);
        slidingPane = (PagerEnabledSlidingPaneLayout) findViewById(R.id.sliding_pane);

        leftPane = (LinearLayout) findViewById(R.id.left_pane);
     /*   bannerRlay = (RelativeLayout) findViewById(R.id.banner_rlay);
        parentCimv = (CircleImageView) findViewById(R.id.parent_cimv);
        parentNameCtxv = (CustomFontTextView) findViewById(R.id.parent_name_ctxv);
        sideInviteRlay = (RelativeLayout) findViewById(R.id.side_invite_rlay);
        sideInviteDimv = (DeviceFitImageView) findViewById(R.id.side_invite_dimv);
        sideInviteDtxv = (CustomFontTextView) findViewById(R.id.side_invite_dtxv);
        sideAccountRlay = (RelativeLayout) findViewById(R.id.side_account_rlay);
        sideAccountDimv = (DeviceFitImageView) findViewById(R.id.side_account_dimv);
        sideAccountDtxv = (CustomFontTextView) findViewById(R.id.side_account_dtxv);
        sideSupportRlay = (RelativeLayout) findViewById(R.id.side_support_rlay);
        sideSupportDimv = (DeviceFitImageView) findViewById(R.id.side_support_dimv);
        sideSupportDtxv = (CustomFontTextView) findViewById(R.id.side_support_dtxv);
        sideSchoolRlay = (RelativeLayout) findViewById(R.id.side_school_rlay);
        sideSchoolDimv = (DeviceFitImageView) findViewById(R.id.side_school_dimv);
        sideSchoolDtxv = (CustomFontTextView) findViewById(R.id.side_school_dtxv);
        sideClassRlay = (RelativeLayout) findViewById(R.id.side_class_rlay);
        sideClassDimv = (DeviceFitImageView) findViewById(R.id.side_class_dimv);
        sideClassDtxv = (CustomFontTextView) findViewById(R.id.side_class_dtxv);*/
      ///  childRcv = (RecyclerView) findViewById(R.id.child_rcv);


            slidingPane.setParallaxDistance(200);
            slidingPane.setSliderFadeColor(getResources().getColor(R.color.transparent_black_40));
        controlPane();
            showPane();
            hidePane();
          //  setValues();
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA onCreate exception "+e.getMessage());
        }
    }

    public void showPane() {
        isOpenPane=false;
        controlPane();
    }
    public void hidePane() {
        isOpenPane=true;
        controlPane();
    }

  /*  private void setValues() {
        try {
            results= PrefManager.getInstance(getApplicationContext()).getLoggedUser();
            if(isValid(results))
            {
                defSetText(parentNameCtxv, results.getFname()+" "+ results.getLname());
                if(isValid(results.getImage()))
                    setAqueryImage(parentCimv,results.getImage(), R.drawable.placeholder_user);
                    //  Picasso.with(context).load(NetworkService.GLOBAL_IMAGE_URL + user.getImage()).placeholder(R.drawable.avatar_user).into(holder.avatar_cimv);
                else {
                    Picasso.with(getApplicationContext()).load(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).into(parentCimv);
                }
            }


        }catch (Exception e)
        {
            LogUtils.LOGD("exception","BSMA setValues exception "+e.getMessage());
        }

    }*/


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtilsutility.LOGD("jithish","BSMA onRestart");
        isOpenPane=true;
        controlPane();
    }

    public void controlPane() {
        try {
            LogUtilsutility.LOGD("jithish","BSMA controlPane click ");
          if(isOpenPane)
           slidingPane.closePane();
            else
              slidingPane.openPane();
            isOpenPane=!isOpenPane;
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA controlPane exception "+e.getMessage());
        }
    }
    public void setContentPaneVoid(int layoutid) {
        View view;
        try {
            view=  getLayoutInflater().inflate(layoutid, sidemenuContentFlay);
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA setContentPane exception "+e.getMessage());
        }

    }
    public View setContentPane(int layoutid) {
        View view=null;
        try {
        view=  getLayoutInflater().inflate(layoutid, sidemenuContentFlay);
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA setContentPane exception "+e.getMessage());
        }
        return  view;
    }
    public View setChildClassesListPane(int layoutid) {
        View view=null;
        try {
            view=  getLayoutInflater().inflate(layoutid, left_content_pane_llay);
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA setChildClassesListPane exception "+e.getMessage());
        }
        return  view;
    }


    public void setActionbarPane(int layoutid) {
        try {
            getLayoutInflater().inflate(layoutid, sidemenuActionBar);
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception","BSMA setActionbarPane exception "+e.getMessage());
        }
    }






}
