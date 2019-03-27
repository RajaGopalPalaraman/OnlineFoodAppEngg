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

public class HotelsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);

        new AsyncTask<String,Void,View>(){
            @Override
            protected LinearLayout doInBackground(String... strings) {
                String areaID = getIntent().getStringExtra(AreaLinearViewModel.ATTR_LIST.get(0));
                String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/hotels.php?areaID="+areaID,null);
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        HashMap<String, HashMap<String, String>> map = HelperUtil.
                                jsonToHashMap(jsonObject.getJSONArray(HotelLinearViewModel.FIELD)
                                        ,0,HotelLinearViewModel.ATTR_LIST);
                        if (map != null) {
                            LinearLayout l = (LinearLayout) new HotelLinearViewModel(HotelsPage.this)
                                    .renderMap(map);
                            return l;
                        }
                    } catch (JSONException | DuplicateValueFoundException e) {
                        Log.d(AppConstants.LOG_TAG,"Exception in parsing Area Data : "+
                                e.getLocalizedMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_hotels_page);
                TextView textView = findViewById(R.id.hotelTitleTextVView);
                String cityChosen = getIntent().getStringExtra(AreaLinearViewModel.ATTR_LIST.get(1));
                textView.setText(getResources().getString(R.string.selectHotel,cityChosen));
                ScrollView scrollView = findViewById(R.id.hotelViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(HotelsPage.this,R.string.somethingWentWrongCommon
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    scrollView.addView(view);
                }
            }
        }.execute();

    }

    public void onHistory(View view)
    {
        Intent intent = new Intent(this,UserHistoryActivity.class);
        startActivity(intent);
    }

}
