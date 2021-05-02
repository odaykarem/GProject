package com.example.gproject.LogIn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gproject.Activities.URLConnector;
import com.example.gproject.Activities.URLConnector;
import com.example.gproject.Models.UserModel;
import com.example.gproject.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginTabFragment extends Fragment {

    private EditText loginMobileNumber;
    private ProgressBar loginProgressBar;
    private Button loginButton;
    private TextInputLayout loginMobileNumberLayout;
    private TextView loginRegisterText;


    private static String URL_LOGIN = URLConnector.BASE_URL + "/" + URLConnector.BASE_API_FOLDER + "/" + URLConnector.LOGIN_URL;
    boolean noNumberFound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_login, container, false);
        loginMobileNumber = view.findViewById(R.id.login_mobile_phone);
        loginProgressBar = view.findViewById(R.id.login_progressbar);
        loginButton = view.findViewById(R.id.login_btn);
        loginMobileNumberLayout = view.findViewById(R.id.login_mobile_number_layout);
        loginRegisterText = view.findViewById(R.id.login_register_link);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = loginMobileNumber.getText().toString().trim();
                if (!mobileNumber.isEmpty()) {
                    logIn(mobileNumber);
                } else {
                    loginMobileNumberLayout.setError("Please enter a mobile number");

                }
            }
        });
    }

    private void logIn(String mobileNumber) {
        loginProgressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        UserModel.mobileNumber = loginMobileNumber.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    UserModel.user_id = object.getInt("user_id");
                                    UserModel.firstName = object.getString("first_name").trim();
                                    UserModel.middleName = object.getString("middle_name").trim();
                                    UserModel.lastName = object.getString("last_name").trim();
                                    UserModel.mobileNumber = object.getString("mobile_number").trim();
                                    UserModel.phoneNumber = object.getString("phone_number").trim();
                                    UserModel.nationalNumber = object.getString("national_number").trim();
                                    UserModel.address = object.getString("address").trim();
                                    UserModel.password = object.getString("password").trim();

                                    Toast.makeText(getContext(), "LogIn Success \n" +
                                            "Your Name:" + UserModel.firstName + " "
                                            + UserModel.lastName, Toast.LENGTH_LONG).show();
                                    loginButton.setVisibility(View.VISIBLE);
                                    loginProgressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();                            loginButton.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "LogIn Error2! " + error.toString()
                                + "\nStatus Code " + error.networkResponse.statusCode
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();                        loginButton.setVisibility(View.VISIBLE);
                        loginProgressBar.setVisibility(View.GONE);

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
            //    if (!noNumberFound) {
                    params.put("mobileNumber", UserModel.mobileNumber);

                    return params;
//                } else {
//                    return checkParams(params);
//                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }
}
