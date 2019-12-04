package com.pacifyr.client.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.pacifyr.client.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/6/17.
 */

@SuppressWarnings("ALL")
public class Api {

    private String type;
    private Context context;

    //public final String URL = "http://dev.ceino.com:8080/topschool/api/v1/";

   /* public final String URL = "http://prod.snap-support.com:8080/topschool/api/v1/";
    public static String IMAGE_URL = "https://s3.amazonaws.com/topschoolprod/";*/

    //public static String IMAGE_URL = "https://s3.amazonaws.com/topschooltest/";

/*jp changes*/
  /*  public final String URL = "http://test.snap-support.com:8080/topschool/api/v1/";
    public static String IMAGE_URL = "https://s3.amazonaws.com/topschoolbetatest/";*/



    public final String URL = GLOBAL_IMAGE_URL;
    public static String IMAGE_URL = GLOBAL_IMAGE_URL;
    /*.......jp changes*/

    private String encType;
    public boolean progressOn = true;
    private String contentType = "application/json";
    private File photo;
   // private ContentBody foto;
    FileInputStream fi;
    byte[] databyte;
    File f;

    /*private ArrayList<ContentBody> ImageBitmapArray = new ArrayList<ContentBody>();
    private ArrayList<ContentBody> ImagePathArray = new ArrayList<ContentBody>();*/
    ;

    public static String schoolId;
    private String filepath;

    public interface FetchListener {
        public void onFetched(JSONObject result);

        public void onFetched(JSONArray result);


    }

    public Api(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        schoolId = sp1.getString("schoolId", null);
        type = sp1.getString("Type", null);
    }

    public void SchoolList(int limit, int skip, String query, FetchListener listener) {
        if (skip == 0)
            progressOn = true;
        else
            progressOn = false;

        String url = "";
        if (query != null && query.length() != 0) {
            String queryval = "";
            try {
                if (query != null && query.length() != 0)
                    queryval = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = URL + "search/schoollist?field=schnam&query=" + queryval + "&skip=" + skip + "&limit=" + limit;
        } else
//            url = URL + "classes/schoollist&skip=" + skip + "&limit=" + limit;
            url = URL + "search/schoollist?field=schnam&query=" + "" + "&skip=" + skip + "&limit=" + limit;

        new AsyncHTTPGet().execute((Object) url, null, (Object) listener);
    }

    class AsyncHTTPGet extends AsyncTask {

        private ProgressDialog pg;
        private String url;
        private ArrayList<String> params;
        private FetchListener listener;

        @Override
        protected void onPreExecute() {
            if (progressOn && context != null) {
                pg = ProgressDialog.show(context, "", "Loading...", true, true);
                pg.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        // TODO Auto-generated method stub
                        AsyncHTTPGet.this.cancel(!isCancelled());
                    }
                });
            }
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            url = arg0[0].toString();
            params = (ArrayList<String>) arg0[1];
            listener = (FetchListener) arg0[2];
            // TODO Auto-generated method stub
            LogUtils.LOGD("jithish","url....." + url);
            LogUtils.LOGD("jithish","params..." + params);


            if (url != null)
                return null;/*getData(url, params);*/
            else
                return null;
        }

        @Override
        protected void onPostExecute(Object args) {
            if (args != null) {
                Log.e("-----http get result---------" + url, args.toString());
                if (pg != null && pg.isShowing())
                    pg.cancel();

                JSONObject resultObject = null;
                JSONObject resultArray = null;
                try {
                    resultObject = new JSONObject(args.toString());
                    if (resultObject.has("results"))
                        //resultArray = resultObject.getJSONObject("results").getJSONArray("prayerrequest");
                        resultArray = resultObject;
                    else if (resultObject.has("badge"))
                        //resultArray = resultObject.getJSONObject("results").getJSONArray("prayerrequest");
                        resultArray = resultObject;

					/*else
                        Toast.makeText(context, "Sorry, no results found", Toast.LENGTH_LONG).show();*/
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                listener.onFetched(resultArray);
            }
        }

    }

    /*public String getData(String url, ArrayList<String> args) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        try {
            // Add your data
            String accesstoken= PrefManager.getInstance(getApplicationContext()).getAccessToken();
            if (args != null) {
                //url +="?";
                for (int i = 0; i < args.size(); i++) {
                    url += URLEncoder.encode(args.get(i), "UTF-8");
                    //url+= args.get(i);
                }
                Log.e("-----url-----------", url);
            }
            // Execute HTTP Get Request
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("topschool-app-id", "appid");
            httpGet.addHeader("topschool-app-key", "appkey");
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("AccessToken",""+accesstoken);

            HttpResponse response = httpclient.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append((line + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return String.valueOf(sb);


        } *//*catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
            // TODO Auto-generated catch block
        } *//*catch (IOException e) {
            e.printStackTrace();
            return null;
            // TODO Auto-generated catch block
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } catch (OutOfMemoryError oe) {
            // TODO: handle exception
            oe.printStackTrace();
        }

        return null;
    }*/

}
