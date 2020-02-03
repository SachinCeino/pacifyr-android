package com.pacifyr.client.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.home.DoctorsActivity;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DoctorsSearchResultActivity;
import com.pacifyr.client.utility.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;


/**
 * Created by vineesh on 13/09/2019.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public class DoctorsFilterFragment extends BaseFragment {


    public DoctorsFilterFragment() {

    }

    private CrystalRangeSeekbar distanceSeekBar, freerangeSeekbar;
    private TextView textView_distance_min;
    private TextView textView_distance_max;
    private TextView textView_fee_min;
    private TextView textView_fee_max;
    private LinearLayout LinearLayout_Submit;
    private String mPriceEnd, mPriceStart, mDistanceStart, mDistanceEnd;
    private String mSpeciality;
    private RespSearchPacifyrWOption mSearchPacifyrWOptionsResponse;
    private TextView txtv_gender;
    private EditText currentLocation;
    private String latitude,longitude;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_filter_doctors, container, false);


        freerangeSeekbar = root.findViewById(R.id.freerangeSeekbar);
        distanceSeekBar = root.findViewById(R.id.distanceSeekBar);
        textView_distance_min = root.findViewById(R.id.textView_distance_min);
        textView_distance_max = root.findViewById(R.id.textView_distance_max);
        textView_fee_min = root.findViewById(R.id.textView_fee_min);
        textView_fee_max = root.findViewById(R.id.textView_fee_max);
        LinearLayout_Submit = root.findViewById(R.id.LinearLayout_Submit);
        txtv_gender=root.findViewById(R.id.txtv_gender);
        currentLocation=root.findViewById(R.id.currentLocation);
        currentLocation.setText(DoctorsActivity.currentLocation);

        /** get Speciality data From Bundle**/
        Bundle mBundle = getArguments();
        mSpeciality = mBundle.get("Speciality").toString();
        if (isValid(getActivity().getIntent().getExtras().get("latitude"))) {
            latitude = getActivity().getIntent().getExtras().get("latitude").toString();
        }
        if (isValid(getActivity().getIntent().getExtras().get("longitude"))) {
            longitude =getActivity(). getIntent().getExtras().get("longitude").toString();
        }


        freerangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                textView_fee_min.setText(String.valueOf(minValue.intValue()));
                textView_fee_max.setText(String.valueOf(maxValue.intValue()));

                mPriceStart = String.valueOf(minValue.intValue());
                mPriceEnd = String.valueOf(maxValue.intValue());

            }
        });


        distanceSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                textView_distance_min.setText(String.valueOf(minValue.intValue()));
                textView_distance_max.setText(String.valueOf(maxValue.intValue()));

                mDistanceStart = String.valueOf(minValue.intValue());
                mDistanceEnd = String.valueOf(maxValue.intValue());

            }
        });


        LinearLayout_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubmitFilterDataAPI();
            }
        });


        txtv_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogueWithList(view);
            }
        });


        return root;
    }

    private void showDialogueWithList(View view) {

        switch (view.getId())
        {
            case R.id.txtv_gender:
                getGenderListItems(txtv_gender);
                break;
           /* case R.id.CustomEditText_gender:
                getGenderListItems(CustomEditText_gender);
                break;
            case R.id.CustomEditText_organization:
                getOrganizationListItems(CustomEditText_organization);
                break;
            case R.id.CustomEditText_race:
                getRaceListItems(CustomEditText_race);
                break;
            case R.id.CustomEditText_region:
                getRegionListItems(CustomEditText_region);
                break;*/
        }
    }


    private void getGenderListItems(TextView customEditText_gender) {

        String title="Select your gender";
        ArrayList<String> genderList=new ArrayList();
        genderList.add("Male");
        genderList.add("Female");
        showListPopup(title,genderList,customEditText_gender);
    }


    private void showListPopup(String title,ArrayList<String> list,TextView customEditText_view) {

        // setup the alert builder
        String[] dataList=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        if (isValid(list))
        {
            dataList=new String[list.size()];
            for (int i=0;i<list.size();i++)
            {
                dataList[i]=list.get(i);
            }
        }
        String[] finalDataList = dataList;
        builder.setItems(dataList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customEditText_view.setText(finalDataList[which]+"");
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void SubmitFilterDataAPI() {

        try {


            setLoading("loading...");
            showProgress();
            String url = GLOBAL_URL + "search/pacifyrs/byLocation";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        mSearchPacifyrWOptionsResponse = gsonToRespSearchPacifyrWOption(json);
                        mSearchPacifyrWOptionsResponse.toString();
                        String js = json.toString();
                        hideProgress();
                        sendEventToActvityAndCloseCurrentFragment(mSearchPacifyrWOptionsResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getPrefManager().getAccessToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", getPrefManager().getUserID());
            jsonObject.putOpt("latitude", latitude);
            jsonObject.putOpt("longitude", longitude);
            jsonObject.putOpt("price_start", mPriceStart);
            jsonObject.putOpt("price_end", mPriceEnd);
            jsonObject.putOpt("distance_start", mDistanceStart);
            jsonObject.putOpt("distance_end", mDistanceEnd);
            jsonObject.putOpt("visitType", "office");

            /** Specialities Array post***/

            List<String> list = new ArrayList<String>();
            list.add(mSpeciality);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendEventToActvityAndCloseCurrentFragment(RespSearchPacifyrWOption mSearchPacifyrWOptionsResponse) {
        EventBus.getDefault().post(mSearchPacifyrWOptionsResponse);
        getActivity().onBackPressed();
    }


    public RespSearchPacifyrWOption gsonToRespSearchPacifyrWOption(JSONObject json) {

        RespSearchPacifyrWOption search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            search = gson.fromJson(jsonString, RespSearchPacifyrWOption.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return search;
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

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }
}

