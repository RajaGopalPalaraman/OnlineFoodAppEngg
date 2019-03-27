package com.edot.onlinefoodappengg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.edot.models.DuplicateValueFoundException;
import com.edot.models.HelperUtil;
import com.edot.network.HttpGETClient;
import com.edot.network.NetworkHelperUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CityPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        new AsyncTask<Void,Void,View>(){
            @Override
            protected LinearLayout doInBackground(Void... integers) {
                String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/cities.php",null);
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        HashMap<String, HashMap<String, String>> map = HelperUtil.
                                jsonToHashMap(jsonObject.getJSONArray(CityLinearViewModel.FIELD)
                                ,1,CityLinearViewModel.ATTR_LIST);
                        if (map != null) {
                            LinearLayout l = (LinearLayout) new CityLinearViewModel(CityPage.this)
                                    .renderMap(map);
                            return l;
                        }
                    } catch (JSONException| DuplicateValueFoundException e) {
                        Log.d(AppConstants.LOG_TAG,"Exception in parsing City Data : "+
                                e.getLocalizedMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_city_page);
                ScrollView scrollView = CityPage.this.findViewById(R.id.cityViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(CityPage.this,R.string.somethingWentWrongCommon
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