package com.edot.onlinefoodappengg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.edot.network.NetworkHelperUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        new AsyncTask<Void,Void,View>()
        {
            @Override
            protected View doInBackground(Void... voids) {
                if (AppConstants.currentLoggedInUserID == null)
                {
                    return null;
                }
                List<OrderHelper.OrderedDataModel> list= OrderHelper.getOrdersFromDB(UserHistoryActivity.this
                        ,AppConstants.currentLoggedInUserID);
                if (list == null)
                {
                    return null;
                }
                HashMap<String, HashMap<String,String>> map = generateMap(list);
                View linearLayout = new UserHistoryLinearViewModel(UserHistoryActivity.this).renderMap(map);
                return linearLayout;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_order_page);
                ScrollView scrollView = findViewById(R.id.orderViewParentLayout);
                if (view == null) {
                    Toast.makeText(UserHistoryActivity.this,R.string.noOrderFound,Toast.LENGTH_SHORT).show();
                    return;
                }
                scrollView.addView(view);
            }
        }.execute();
    }

    private HashMap<String,HashMap<String,String>> generateMap(List<OrderHelper.OrderedDataModel> list) {
        final String id = "id";
        final String name = "name";
        final String area = "area";

        HashMap<String,HashMap<String,String>> hotelsNap = new HashMap<>();
        HashMap<String,HashMap<String,String>> map = new HashMap<>();

        for (OrderHelper.OrderedDataModel order : list)
        {
            HashMap<String,String> childMap = new HashMap<>();
            HashMap<String,String> hotelInfoMap;
            if (!hotelsNap.containsKey(order.hotelId))
            {
                String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com" +
                        "/FoodApp/hotelInfo.php?hotelID="+order.hotelId,null);
                if (data == null)
                {
                    return null;
                }
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    hotelInfoMap = new HashMap<>();
                    hotelInfoMap.put(name,jsonObject.getString(name));
                    hotelInfoMap.put(area,jsonObject.getString(area));
                    hotelsNap.put(order.hotelId,hotelInfoMap);
                } catch (Exception e) {
                    return null;
                }
            }
            else {
                hotelInfoMap = hotelsNap.get(order.hotelId);
            }

            Gson gson = new Gson();
            OrderHelper.ItemsListModel items= gson.fromJson(order.orderedItems,
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

            childMap.put(OrderLinearViewModel.ATTR_LIST.get(0),hotelInfoMap.get(name));
            childMap.put(OrderLinearViewModel.ATTR_LIST.get(1),hotelInfoMap.get(area));
            childMap.put(OrderLinearViewModel.ATTR_LIST.get(2),"Rs."+order.netCost);
            childMap.put(OrderLinearViewModel.ATTR_LIST.get(3),builder.toString());
            childMap.put(OrderLinearViewModel.ATTR_LIST.get(4),order.userId);
            childMap.put(OrderLinearViewModel.ATTR_LIST.get(5),order.timeStamp);

            map.put(String.valueOf(order.orderId),childMap);
        }
        return map;
    }
}