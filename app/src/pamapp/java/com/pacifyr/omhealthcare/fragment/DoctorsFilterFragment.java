package com.pacifyr.omhealthcare.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.home.DoctorsSearchResultActivity;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

/**
 * Created by vineesh on 13/09/2019.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public class DoctorsFilterFragment extends Fragment {


    public DoctorsFilterFragment() {

    }
    private RangeSeekBar distanceSeekBar,freerangeSeekbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filter_doctors, container, false);
        freerangeSeekbar=root.findViewById(R.id.freerangeSeekbar);
        distanceSeekBar=root.findViewById(R.id.distanceSeekBar);


        distanceSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {

            }
        });

       // Get noticed while dragging
        distanceSeekBar.setNotifyWhileDragging(true);

        freerangeSeekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
            }
        });

        // Get noticed while dragging
        freerangeSeekbar.setNotifyWhileDragging(true);


        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((DoctorsSearchResultActivity) getActivity()).changeTitle("FILTER");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((DoctorsSearchResultActivity) getActivity()).changeTitle("SEARCH RESULT");
    }

}

