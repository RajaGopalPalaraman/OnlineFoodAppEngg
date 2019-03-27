package com.edot.onlinefoodappengg;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edot.models.HelperUtil;
import com.edot.network.HttpGETClient;
import com.edot.network.NetworkHelperUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ItemPage extends AppCompatActivity {

    public static final int REQ_CODE = 8;

    private ItemSelectionController controller;
    private String hotelID;
    private String hotelName;
    private String hotelComment;
    private String tableID;

    private final HashMap<String,String> pMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new ItemSelectionController(this);
        Intent intent = getIntent();
        hotelID = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(0));
        hotelName = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(1));
        hotelComment = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(2));
        tableID = (intent == null)? null : intent.getStringExtra(TableLinearViewModel.ATTR_LIST.get(0));
        String menuType = (intent == null)? null : intent.getStringExtra(TableLinearViewModel.ATTR_LIST.get(4));

        Log.d(AppConstants.LOG_TAG,"Id : " + hotelID);
        Log.d(AppConstants.LOG_TAG,"Name : " + hotelName);
        Log.d(AppConstants.LOG_TAG,"Comment : "+hotelComment);
        Log.d(AppConstants.LOG_TAG,"TableID : "+tableID);
        Log.d(AppConstants.LOG_TAG,"menuType : "+menuType);

        if(hotelID != null)
        {
            hotelID = hotelID.toLowerCase();
        }
        if(hotelName == null || hotelComment == null || tableID == null ||
                AppConstants.currentLoggedInUserID == null || menuType == null)
        {
            Toast.makeText(this,R.string.error_404,Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            pMap.put("hotelID", hotelID);
            pMap.put("tableID", tableID);
            pMap.put("userID", AppConstants.currentLoggedInUserID);
            showItems("breakfast".equals(menuType),"lunch".equals(menuType),"dinner".equals(menuType));
        }
    }

    public void navigateToBillPage(final View view)
    {
        Log.d(AppConstants.LOG_TAG,"Proceed button clicked : "+AppConstants.currentLoggedInUserID);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setContentView(R.layout.layout_loading);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                String data = NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/tableBlocker.php",pMap);
                if (data == null)
                {
                    return false;
                }
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (!jsonObject.has("tableID"))
                    {
                        return false;
                    }
                } catch (JSONException e) {
                    return false;
                }
                return OrderHelper.placeOrder(ItemPage.this,AppConstants.currentLoggedInUserID,
                        hotelID,tableID,controller.getListOfSelectedItems(),controller.getTotal());
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean)
                {
                    setContentView(R.layout.billlayout);
                    Button button = findViewById(R.id.billLayoutPayButton);
                    button.setText(getResources().getString(R.string.payAmount,controller.getTotal()));
                }
                else
                {
                    Toast.makeText(ItemPage.this, R.string.orderFailed, Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        }.execute();
    }

    private void showItems(final boolean b1,final boolean b2,final boolean b3)
    {
        new AsyncTask<String,Void,View>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setContentView(R.layout.layout_loading);
            }

            @Override
            protected LinearLayout doInBackground(String... strings) {
                HttpGETClient httpGETClient = new HttpGETClient();
                if (httpGETClient.establishConnection("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/"+strings[0]+".properties")) {
                    HashMap<String, HashMap<String, String>> map = HelperUtil.readProperties(httpGETClient
                                    .getInputStream(), ItemLinearViewModel.FIELD,
                            ItemLinearViewModel.ATTR_LIST);
                    httpGETClient.closeConnection();
                    LinearLayout l = (LinearLayout) new ItemLinearViewModel(ItemPage.this,
                            controller,b1,b2,b3).renderMap(map);
                    return l;
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_item_page);
                controller.setRootView((RelativeLayout)findViewById(R.id.itemPageRootLayout));
                View rootView = findViewById(R.id.itemPageRootLayout);
                TextView textView = findViewById(R.id.itemPagehotelTitleTextView);
                textView.setText(hotelName);
                textView = findViewById(R.id.itemPagehotelCommentTextView);
                textView.setText(hotelComment);
                LinearLayout scrollView = findViewById(R.id.itemViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(ItemPage.this,R.string.somethingWentWrongCommon
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    scrollView.addView(view);
                }
            }
        }.execute(hotelID);
    }

    public void onMenu(View view) {
        int id = view.getId();
        showItems(id == R.id.breakfast, id == R.id.lunch, id == R.id.dinner);
    }

    public void showSuccess(View view) {
        EditText editText1 = findViewById(R.id.billLayoutCardNo);
        EditText editText2 = findViewById(R.id.billLayoutCardCVV);
        if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty()) {
            Toast.makeText(this,R.string.allMandatoryFields,Toast.LENGTH_SHORT).show();
        }
        else
        {
            setContentView(R.layout.booking_success);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/tableUnlocker.php",pMap);
                return null;
            }
        }.execute();
    }
}
