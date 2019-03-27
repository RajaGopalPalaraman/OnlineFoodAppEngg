package com.edot.onlinefoodappengg;

import android.content.Context;
import android.util.Log;

import com.edot.network.HttpPOSTClient;
import com.edot.network.NetworkHelperUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderHelper {

    public static boolean placeOrder(Context context,String userID,String hotelID,String tableID,ArrayList<ItemDataModel> items,int total)
    {
        Gson gson = new Gson();
        ItemsListModel model = new ItemsListModel();
        model.arrayList = items;
        String orderDetails = gson.toJson(model);

        HttpPOSTClient httpPOSTClient = new HttpPOSTClient();
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("hotelID", hotelID);
        paramsMap.put("tableID",tableID);
        paramsMap.put("userID", userID);
        paramsMap.put("orderDetails", orderDetails);
        paramsMap.put("totalCost", String.valueOf(total));
        if (httpPOSTClient.establishConnection("http://autoiot2019-20.000webhostapp.com/FoodApp/makeOrders.php"
                ,paramsMap))
        {
            InputStream inputStream = httpPOSTClient.getInputStream();
            byte[] bytes = new byte[20];
            try {
                int length = inputStream.read(bytes);
                httpPOSTClient.closeConnection();
                String s = new String(bytes,0,length, StandardCharsets.UTF_8);
                Log.d(AppConstants.LOG_TAG,"OrderHelper"+s);

                //Table Confirmation Handling
                paramsMap.put("orderID",s);
                s = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/tableBooker.php",paramsMap);

                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    return jsonObject.has("tableID");
                }
            } catch (Exception e) {
                Log.d(AppConstants.LOG_TAG,"Exception while reading data : "
                        +e.getLocalizedMessage());
            }
        }
        return false;
    }

    public static List<OrderedDataModel> getOrdersFromDB(Context context)
    {
        return getOrdersFromDB(context,null);
    }

    public static List<OrderedDataModel> getOrdersFromDB(Context context,String userID)
    {
        List<OrderedDataModel> orderList = null;
        HttpPOSTClient httpGETClient = new HttpPOSTClient();
        String url = "http://autoiot2019-20.000webhostapp.com/FoodApp/viewOrdersList.php";
        HashMap<String,String> paramsMap = new HashMap<>();
        if (userID != null)
        {
            paramsMap.put("user",userID);
        }
        if (httpGETClient.establishConnection
                (url,paramsMap))
        {
            InputStream inputStream = httpGETClient.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[500];
            int length;
            try {
                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }
                httpGETClient.closeConnection();
                String data = new String(outputStream.toByteArray(),StandardCharsets.UTF_8);
                if (!data.isEmpty()) {
                    orderList = new ArrayList<>();
                    for (String s : data.split("##")) {
                        OrderedDataModel order = new OrderedDataModel();
                        String[] s1 = s.split("#");
                        if (s1.length == 6) {
                            order.orderId = s1[0];
                            order.hotelId = s1[1];
                            order.userId = s1[2];
                            order.orderedItems = s1[3];
                            order.timeStamp = s1[4];
                            order.netCost = Integer.parseInt(s1[5]);
                            orderList.add(order);
                        }
                    }
                    return orderList;
                }
            } catch (IOException e)
            {
                Log.d(AppConstants.LOG_TAG,"Exception while reading data : "
                        +e.getLocalizedMessage());
            }
        }
        return orderList;
    }

    public static class OrderedDataModel
    {
        public String orderId;
        public String hotelId;
        public String userId;
        public String orderedItems;
        public int netCost;
        public String timeStamp;
    }

    public static class ItemsListModel
    {
        public ArrayList<ItemDataModel> arrayList;
    }

}
