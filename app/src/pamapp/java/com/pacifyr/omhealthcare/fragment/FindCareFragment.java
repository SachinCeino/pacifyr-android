package com.pacifyr.omhealthcare.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.pacifyr.omhealthcare.R;

/**
 * Created by vineesh on 13/09/2019.
 */

public class FindCareFragment extends Fragment {


    public FindCareFragment() {

    }

    private ViewPager mViewPager;

    private RadioGroup mSegmentedGroup;
    private RadioButton mRadioButtonA, mRadioButtonB;
    public static int TOUCH_COUNT = 0;
    RelativeLayout mRelativeLayout;
    CoordinatorLayout mCoordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_find_care, container, false);
        mSegmentedGroup = (RadioGroup) root.findViewById(R.id.segmentedgroup);
        mRadioButtonB = (RadioButton) root.findViewById(R.id.rb1);
        mRadioButtonA = (RadioButton) root.findViewById(R.id.rb3);

        fillColour_ForRadioButton(mRadioButtonA);
        fillNoColor_ForRadioButton(mRadioButtonB);

        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb1:

                        fillColour_ForRadioButton(mRadioButtonB);
                        fillNoColor_ForRadioButton(mRadioButtonA);

                        break;


                    case R.id.rb3:

                        fillColour_ForRadioButton(mRadioButtonA);
                        fillNoColor_ForRadioButton(mRadioButtonB);
                        break;

                }
            }
        });

        return root;
    }

    /**
     * set background
     * as filling a colour
     **/

    private void fillColour_ForRadioButton(RadioButton _RadioButton) {


        if (_RadioButton.getId() == R.id.rb1) {

            _RadioButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.segment_background_radiobutton_fill_right));
        } else {
            _RadioButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.segment_background_radiobutton_fill_left));

        }

        _RadioButton.setTextColor(getResources().getColor(R.color.RadioButtonSelectedColour));
    }

    /**
     * set background
     * as no colour
     **/

    private void fillNoColor_ForRadioButton(RadioButton _RadioButton) {
        if (_RadioButton.getId() == R.id.rb1) {

            _RadioButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.segment_background_radiobutton_empty_right_button));

        } else {

            _RadioButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.segment_background_radiobutton_empty_left_button));

        }

        _RadioButton.setTextColor(getResources().getColor(R.color.RadioButtonUnSelectedColour));
    }




   /* @SuppressLint("NewApi")
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mSegmentedGroup.check(R.id.rb1);
                fillColour_ForRadioButton(mRadioButtonB);
                fillNoColor_ForRadioButton(mRadioButtonA);
                break;

            case 1:
                mSegmentedGroup.check(R.id.rb3);
                fillColour_ForRadioButton(mRadioButtonA);
                fillNoColor_ForRadioButton(mRadioButtonB);

                break;
        }
    }*/

}

