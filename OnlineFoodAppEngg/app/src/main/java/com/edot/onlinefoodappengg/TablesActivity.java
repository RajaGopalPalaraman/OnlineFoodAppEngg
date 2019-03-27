package com.edot.onlinefoodappengg;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.edot.models.DuplicateValueFoundException;
import com.edot.models.HelperUtil;
import com.edot.network.NetworkHelperUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TablesActivity extends AppCompatActivity {

    private String hotelID;
    private String hotelName;
    private String hotelComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        hotelID = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(0));
        hotelName = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(1));
        hotelComment = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(2));

        Log.d(AppConstants.LOG_TAG,"Id : " + hotelID);
        Log.d(AppConstants.LOG_TAG,"Name : " + hotelName);
        Log.d(AppConstants.LOG_TAG,"Comment : "+hotelComment);

        if(hotelID != null)
        {
            hotelID = hotelID.toLowerCase();
        }
        if(hotelName == null || hotelComment == null)
        {
            Toast.makeText(this,R.string.error_404,Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.layout_loading);
        new AsyncTask<Void,Void,View>(){
            @Override
            protected View doInBackground(Void... integers) {
                String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/tableInfo.php?hotelID="+hotelID,null);
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        HashMap<String, HashMap<String, String>> map = HelperUtil.
                                jsonToHashMap(jsonObject.getJSONArray(TableLinearViewModel.FIELD)
                                        ,0,TableLinearViewModel.ATTR_LIST);
                        if (map != null) {
                            if (map.isEmpty())
                            {
                                TextView textView = new TextView(TablesActivity.this);
                                textView.setText("No Tables Available");
                                return textView;
                            }
                            LinearLayout l = (LinearLayout) new TableLinearViewModel(TablesActivity.this
                            ,hotelName,hotelID,hotelComment).renderMap(map);
                            return l;
                        }
                    } catch (JSONException | DuplicateValueFoundException e) {
                        Log.d(AppConstants.LOG_TAG,"Exception in parsing Tables Data : "+
                                e.getLocalizedMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_tables);
                ScrollView scrollView = TablesActivity.this.findViewById(R.id.hotelViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(TablesActivity.this,R.string.somethingWentWrongCommon
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    scrollView.addView(view);
                }
            }
        }.execute();
    }
}
