package com.edot.onlinefoodappengg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.edot.models.HelperUtil;
import com.edot.network.NetworkHelperUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppConstants.currentLoggedInHotelID != null) {
            setContentView(R.layout.layout_loading);
            new AsyncTask<Void, Void, View>() {
                @Override
                protected View doInBackground(Void... voids) {
                    String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                            "FoodApp/tableStatus.php?hotelID="+AppConstants.currentLoggedInHotelID, null);
                    if (data == null) {
                        return null;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        HashMap<String, HashMap<String, String>> map = HelperUtil.
                                jsonToHashMap(jsonObject.getJSONArray(ManagerLinearViewModel.FIELD)
                                        ,6,ManagerLinearViewModel.ATTR_LIST);
                        if (map != null) {
                            if (map.isEmpty())
                            {
                                return null;
                            }
                            for (String key : map.keySet())
                            {
                                HashMap<String,String> innerMap = map.get(key);
                                String temp = innerMap.get(ManagerLinearViewModel.ATTR_LIST.get(2));
                                innerMap.put(ManagerLinearViewModel.ATTR_LIST.get(2),"Rs."+temp);
                                temp = innerMap.get(ManagerLinearViewModel.ATTR_LIST.get(3));

                                Gson gson = new Gson();
                                OrderHelper.ItemsListModel items= gson.fromJson(temp,
                                        OrderHelper.ItemsListModel.class);
                                StringBuilder builder = new StringBuilder();
                                boolean appenComma = false;

                                for (ItemDataModel item: items.arrayList)
                                {
                                    if (appenComma)
                                    {
                                        builder.append(", ");
                                    }

                                    builder.append(item.getName());
                                    builder.append(" X ");
                                    builder.append(item.getQuantity());
                                    appenComma = true;
                                }

                                innerMap.put(ManagerLinearViewModel.ATTR_LIST.get(3),builder.toString());
                            }
                            View linearLayout = new ManagerLinearViewModel(ManagerActivity.this).renderMap(map);
                            return linearLayout;
                        }
                    } catch (Exception e) {
                        Log.d(AppConstants.LOG_TAG,"ManagerActivity : "+e.getLocalizedMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(View view) {
                    super.onPostExecute(view);
                    setContentView(R.layout.activity_order_page);
                    ScrollView scrollView = findViewById(R.id.orderViewParentLayout);
                    if (view == null) {
                        Toast.makeText(ManagerActivity.this, R.string.noOrderFound, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    scrollView.addView(view);
                }
            }.execute();
        }
        else
        {
            Toast.makeText(ManagerActivity.this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
