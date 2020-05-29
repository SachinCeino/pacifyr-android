package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.model.ModelSpecialityByCategory;
import com.pacifyr.client.model.ModelSpecialityByEmotion;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pacifyr.client.home.ConnectToPacifyrActivity.customerEmotion;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

/**
 * Created by ceino on 19/12/17.
 */

public class EmotionSpecialityAdapter extends RecyclerView.Adapter<EmotionSpecialityAdapter.MyViewHolder>{

    ArrayList<ModelSpecialityByEmotion.Results> list;
    Context context;
    Activity activity;
    private String userRole;
    public SwipeDeckConnectPacifyrAdapter adapter;
    //    SwipeDeck cardStack;
    RecyclerView imgRcv;
    public static  String reason_forcall=null;
    //ImageView emotionDimv;

    private PrefManager prefM;
    private String userID,acessToken;
    private String companyID;
    private String customerRole;
    private boolean employeeFlag;
    private boolean showAllFlag;


    MainTextView noPacifyrCtxv;

    private String category;

    public EmotionSpecialityAdapter(Context context, ArrayList<ModelSpecialityByEmotion.Results> mListEmotions) {
        this.context=context;
        list=mListEmotions;
        reason_forcall=null;
        prefM= PrefManager.getInstance(context);
        userID = prefM.getUserID();
        acessToken = prefM.getAccessToken();
        companyID = prefM.getCompanyId();
        customerRole = prefM.getCustomerRole();
        userRole =prefM.getCustomerRole();
    }

    public EmotionSpecialityAdapter(Context context,Activity activity, ArrayList<ModelSpecialityByEmotion.Results> mListEmotions) {
        this.context=context;
        list=mListEmotions;

        this.activity=activity;
        prefM = PrefManager.getInstance(context);
        userID = prefM.getUserID();
        acessToken = prefM.getAccessToken();
        customerRole = prefM.getCustomerRole();
        companyID = prefM.getCompanyId();
        reason_forcall=null;
        userRole =prefM.getCustomerRole();
        ModelSpecialityByEmotion.Results mTempObject = null;
        for (int i = 0; i < list.size(); i++) {
            ModelSpecialityByEmotion.Results object = list.get(i);

            if (isValid(object)) {
                if (object.getTitle().equalsIgnoreCase("Others")) {
                    mTempObject = object;
                    list.remove(object);
                }
            }

        }
        if (isValid(mTempObject)) {
            list.add(mTempObject);
            notifyDataSetChanged();
            mTempObject = null;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_emotion_detail_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.commentCtx.setText(list.get(position).getTitle());

        if (list.get(position).getClicked()){
            holder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
            holder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_bg));
            holder.commentCtx.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.commentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* for (int i=0;i<list.size();i++)
                {
                    if (i==position){
                        list.get(position).setClicked(true);
                        holder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
                        holder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));
                        //holder.emotionIMG.clearColorFilter();
                        //selEmotion = emotions.get(position);
                        //setSelectedIndex(position);
                    }
                    else {
                        list.get(i).setClicked(false);
                        holder.commentCtx.setTextColor(context.getResources().getColor(R.color.black));
                        holder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_bg));

                    }

                }
                notifyDataSetChanged();*/
                holder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
                holder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));
                for(int i=0;i<list.size();i++)
                {
                    if(i==position){
                        list.get(position).setClicked(true);
                    }
                    else{
                        list.get(i).setClicked(false);
                    }
                }
                notifyDataSetChanged();



                activity.findViewById(R.id.select_emotion_detail_rlay).setVisibility(View.GONE);
                activity.findViewById(R.id.select_list_pacifyr_rlay).setVisibility(View.VISIBLE);



                category = list.get(position).getTitle();
                customerEmotion=list.get(position).getTitle();


                imgRcv = activity.findViewById(R.id.image_rcv);
                Button button1 = activity.findViewById(R.id.button1);
                Button button2 = activity.findViewById(R.id.button2);
                Button button3 = activity.findViewById(R.id.button3);
                Button button4 = activity.findViewById(R.id.button4);
                noPacifyrCtxv = activity.findViewById(R.id.no_pacifyr_ctxv);
                button3.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_click_btn);
                button3.setClickable(true);
                button2.setClickable(true);
                button1.setClickable(true);
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                reason_forcall = list.get(position).getTitle();

                updateUserEmotion();



                if(NetworkService.isConnectSinglePacifyer)
                {

                }
                else {
                    category = list.get(position).getTitle();
                    //showCategories(category);

                    LinearLayout showallLlay = activity.findViewById(R.id.showall_llay);

                    if(customerRole.equals("customer")){
                        employeeFlag = false;
                        showallLlay.setVisibility(View.GONE);
                    }
                    else if(customerRole.equals("employee")|| customerRole.equals("unpaidEmployee")){
                        employeeFlag = true;
                        showallLlay.setVisibility(View.VISIBLE);
                        if(customerRole.equals("unpaidEmployee")){
                            employeeFlag = false;
                        }
                    }
                    else if(customerRole.equals("paidMember")){
                        employeeFlag = false;
                        showallLlay.setVisibility(View.GONE);
                    }

                    /**Update Speciality Adapter
                     * *
                     * default selection is 0
                     * then search pacifyr will load ininitialy first category
                     * update speciality according to showAll flag change
                     * call shw all flag false iniitially**/
                    ((ConnectToPacifyrActivity)activity).UpdateSpecialityAdapter(false);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * using latest api specialities/byCategory?category=all&includeChargeablePacifyrs=
     * we calling this api in connect pacifyr screen**/
/*
    public void showCategories(String category) {
        try {
            String url = GLOBAL_URL + "specialities/byCategory?category=" + category;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String js = json.toString();

                        ModelSpecialityByCategory model = GsonUtils.getInstance().gsonToModelSpecialityByCategory(json);
                        ModelSpecialityByCategory.Results[] item = model.getResults();

                        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(item, context);
                       */
/* String[] array = item[0].getCategory();
                        webSearchPacifyrWOptions(array);*//*

                        imgRcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        imgRcv.setAdapter(categoryListAdapter);
                        categoryListAdapter.addListener((ConnectToPacifyrActivity) activity);
                        categoryListAdapter.setSelected(0);

                    */
/* String imgURL = GLOBAL_IMAGE_URL + res[i].getImage();
                    String title = res[i].getTitle();
                    if (isValidhtml(imgURL))
                        Picasso.with(context).load(imgURL).placeholder(R.drawable.placeholder_user).into(emotionDimv);*//*



                        //emotionDimv  = (ImageView) activity.findViewById(R.id.emotion_dimv);

                        //modelAllEmployees= GsonUtils.getInstance().gsonToMListEmployees(json);
                        //postEventOnMainThread(new NotifyEvent(true, false));
                        //EventBus.getDefault().post(new EventMessage(getString(R.string.list_all_employees)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (noPacifyrCtxv != null) {
                            noPacifyrCtxv.setVisibility(View.VISIBLE);
                        }

                    }
                }
            };
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", acessToken);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    /*private void setAdapterSearchOption() {

        if(!NetworkService.isConnectSinglePacifyer) {
            if (isValid(pSearchPacifyrWOptionsResponse)) {
                adapter = new SwipeDeckConnectPacifyrAdapter(pSearchPacifyrWOptionsResponse.getResultsList(), context,activity);
                cardStack.setAdapter(adapter);
            }
        }

    }*/

    public void updateUserEmotion(){
        //call_emotion

            try {
                PostRegisterModel postRegisterModel=new PostRegisterModel(PacifyrActivity.call_emotion.getId(), EmotionSpecialityAdapter.reason_forcall);
                Gson gson = new Gson();
                String json = gson.toJson(postRegisterModel);
                String url = GLOBAL_URL + "user/editprofile/" + userID;
                if (isValid(postRegisterModel)) {
                    com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                if (isValid(result)) {
                                    final JSONObject jsonObject = new JSONObject(result.toString());
                                    String js = result.toString();
                                    String phone = jsonObject.getString("phoneNumber");
                                    if (isValid(phone)) {
                                        //Toast.makeText(getApplicationContext(), "Updated profile", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception eee) {
                                Toast.makeText(context, "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                                eee.printStackTrace();
                            }
                        }
                    };
                    Ion.with(context).load(url).setHeader("AccessToken", acessToken).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    /*public void webSearchPacifyrWOptions(String[] emotion) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray mJSONArray = new JSONArray(Arrays.asList(emotion));
            jsonObject.putOpt("specialities",mJSONArray);
            //aq.ajax(url,JSONObject.class, cb);
            searchPacifyr(jsonObject);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    /*public void searchPacifyr(JSONObject jsonObject){
        try {

            String url = GLOBAL_URL + getRoleSearchMap().get(userRole);
            AQuery aq = new AQuery(context);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        pSearchPacifyrWOptionsResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        //pSearchPacifyrWOptionsResponse.toString();
//                        setAdapterSearchOption();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            };
            jsonObject.putOpt("companyId",companyID);
            jsonObject.putOpt("includeChargeablePacifyrs",showAllFlag);
            jsonObject.putOpt("initiatorId", userID);
            jsonObject.putOpt("country", prefM.getCountry());

            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", acessToken);
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }*/

    /*public void webSearchPacifyrWOptions(String emotion) {
        try {

            JSONObject jsonObject = new JSONObject();
            List<String> list = new ArrayList<String>();
            list.add(emotion);
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities",array);
            searchPacifyr(jsonObject);
            //aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MainTextView commentCtx;
        public LinearLayout commentLlay;

        public MyViewHolder(View view) {
            super(view);
            commentCtx = view.findViewById(R.id.comment_ctxv);
            commentLlay = view.findViewById(R.id.comment_llay);
        }
    }

    public HashMap<String , String> getRoleSearchMap() {
        HashMap<String , String> roleSearchApiMap = new HashMap<String , String>();

        roleSearchApiMap.put("customer", "search/pacifyrs");
        roleSearchApiMap.put("employee", "search/pacifyrs/forEmployees");
        roleSearchApiMap.put("unpaidEmployee", "search/pacifyrs/forEmployees");
        roleSearchApiMap.put("paidMember", "search/pacifyrs/forPaidMembers");

        return roleSearchApiMap;
    }
}
