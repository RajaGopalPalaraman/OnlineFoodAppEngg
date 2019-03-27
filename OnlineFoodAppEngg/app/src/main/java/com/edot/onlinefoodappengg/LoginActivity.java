package com.edot.onlinefoodappengg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edot.network.NetworkHelperUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText userID;
    private EditText password;

    private RegisterTextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        registerTextView = findViewById(R.id.registration_text);
    }

    private void openRegistrationPage() {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void onLogin(final View view) {
        if(!(userID.getText().toString().isEmpty() || password.getText().toString().isEmpty())) {
            Log.d(AppConstants.LOG_TAG, "@onLogin");
            if ("admin".equals(userID.getText().toString())) {
                if ("admin".equals(password.getText().toString())) {
                    Toast.makeText(this, R.string.adminLoginSuccess, Toast.LENGTH_SHORT).show();
                    AppConstants.currentLoggedInUserID = "admin";
                    AppConstants.currentLoggedInUserName = "admin";
                    resetAll();
                    Intent intent = new Intent(LoginActivity.this,OrderPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.invalidLoginCredentials, Toast.LENGTH_SHORT).show();
                    resetPassword();
                }
            }
            else
            {
                view.setClickable(false);
                registerTextView.setClickable(false);
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        HashMap<String,String> paramsMap = new HashMap<>();
                        paramsMap.put("userid",userID.getText().toString());
                        paramsMap.put("password",password.getText().toString());
                        return NetworkHelperUtil.readData("http://autoiot2019-20.000webhostapp.com" +
                                "/FoodApp/loginEngg.php",paramsMap);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        final String name = "name";
                        final String hotelID = "hotelID";
                        super.onPostExecute(s);
                        view.setClickable(true);
                        registerTextView.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.has(name))
                            {
                                Toast.makeText(LoginActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                                AppConstants.currentLoggedInUserID = userID.getText().toString();
                                AppConstants.currentLoggedInUserName = jsonObject.getString(name);
                                resetAll();

                                String hotel = jsonObject.getString(hotelID);
                                if (hotel.isEmpty()) {
                                    Intent intent = new Intent(LoginActivity.this, CityPage.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    AppConstants.currentLoggedInHotelID = hotel;
                                    Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,R.string.invalidLoginCredentials,Toast.LENGTH_SHORT).show();
                                resetPassword();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        }
        else
        {
            Toast.makeText(this,R.string.loginFieldsEmpty,Toast.LENGTH_SHORT).show();
            resetPassword();
        }
    }

    public void onRegister(View view) {
        Log.d(AppConstants.LOG_TAG,"@onRegister");
        openRegistrationPage();
    }

    private void resetPassword()
    {
        password.setText("");
    }

    private void resetAll()
    {
        userID.setText("");
        password.setText("");
    }
}
