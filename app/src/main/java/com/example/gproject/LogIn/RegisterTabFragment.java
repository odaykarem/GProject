package com.example.gproject.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gproject.Activities.MainActivity;
import com.example.gproject.Activities.URLConnector;
import com.example.gproject.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.gproject.Models.UserModel;

public class RegisterTabFragment extends Fragment {
    private EditText reg_first_name, reg_middle_name,
            reg_last_name, reg_mobile_number,
            reg_phone_number,reg_national_number,reg_address,
            reg_password, reg_password_confirm;

    private Button register_btn;
    private TextInputLayout  txtInLayoutRegPassword;
    private ProgressBar progressBar;

  //  private static  String URL_REGIST = "http://192.168.1.104/GProgect_API/register.php";
    private static String URL_REGIST = URLConnector.BASE_URL + "/" + URLConnector.BASE_API_FOLDER + "/" + URLConnector.REGISTER_URL;

    private boolean requestState;
    @Nullable
    @Override
    //Hint :: Phone Number for Land phone //
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_test, container, false);
        reg_first_name = view.findViewById(R.id.reg_first_name);
        reg_middle_name = view.findViewById(R.id.reg_middle_name);
        reg_last_name = view.findViewById(R.id.reg_last_name);
        reg_mobile_number = view.findViewById(R.id.reg_mobile_number);
        reg_phone_number =  view.findViewById(R.id.reg_phone_number);
        reg_national_number =  view.findViewById(R.id.reg_national_number);
        reg_address =  view.findViewById(R.id.reg_address);
        reg_password = view.findViewById(R.id.reg_password);
        reg_password_confirm = view.findViewById(R.id.reg_password_confirm);
        register_btn = view.findViewById(R.id.reg_register_btn);
        txtInLayoutRegPassword = view.findViewById(R.id.txtInLayoutRegPassword);
        progressBar = view.findViewById(R.id.register_progressbar);
        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(CheckValidateInfo()){
                    Toast.makeText(getContext(), "check done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
             //         sendRequest();

//                }else{
//                    progressBar.setVisibility(View.GONE);
//                    register_btn.setVisibility(View.VISIBLE);
//                    return;
//                }
            }
        });
    }

        private  void sendRequest() {

        requestState = false;
        UserModel.firstName = reg_first_name.getText().toString().trim();
        UserModel.middleName  = reg_middle_name.getText().toString().trim();
        UserModel.lastName = reg_last_name.getText().toString().trim();
        UserModel.mobileNumber = reg_mobile_number.getText().toString().trim();
        UserModel.phoneNumber = reg_phone_number.getText().toString().trim();
        UserModel.nationalNumber = reg_national_number.getText().toString().trim();
        UserModel.address = reg_address.getText().toString().trim();
        UserModel.password = reg_password.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);

                            if(success.equals("1")){
                                Toast.makeText(getContext(), "Register Success", Toast.LENGTH_LONG).show();
                                requestState = true;
                            }else{
                                if(message.contains("Duplicate entry")){
                                Toast.makeText(getContext(), "This mobile number is already exist", Toast.LENGTH_LONG).show();

                            }

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);
                            requestState = false;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), UserModel.firstName+" "+UserModel.middleName + " "+ UserModel.lastName, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Register Error2! " + error.toString()
                                + "\nStatus Code " + error.networkResponse.statusCode
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        register_btn.setVisibility(View.VISIBLE);
                        requestState = false;

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstName", UserModel.firstName);
                params.put("middleName", UserModel.middleName);
                params.put("lastName", UserModel.lastName);
                params.put("mobileNumber", UserModel.mobileNumber);
                params.put("phoneNumber", UserModel.phoneNumber);
                params.put("nationalNumber", UserModel.nationalNumber);
                params.put("address", UserModel.address);
                params.put("password", UserModel.password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private boolean CheckValidateInfo() {
         progressBar.setVisibility(View.VISIBLE);
         register_btn.setVisibility(View.GONE);
        if (reg_first_name.getText().toString().trim().isEmpty()) {

            reg_first_name.setError("Please fill out this field");
            progressBar.setVisibility(View.VISIBLE);
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (reg_first_name.getText().toString().trim().matches(regex)) {
                reg_first_name.setError("Please fill out this field with valid first name");
                return false;
            }
        }
        if (reg_middle_name.getText().toString().trim().isEmpty()) {

            reg_middle_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (reg_middle_name.getText().toString().trim().matches(regex)) {
                reg_middle_name.setError("Please fill out this field with valid first name");
                return false;
            }
        }
        if (reg_last_name.getText().toString().trim().isEmpty()) {

            reg_last_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (reg_last_name.getText().toString().trim().matches(regex)) {
                reg_last_name.setError("Please fill out this field with valid first name");
                return false;
            }
        }
        if (reg_mobile_number.getText().toString().trim().isEmpty()) {

            reg_mobile_number.setError("Please fill out this field");
            return false;
        } else {
            if(!checkPhoneNumber(reg_mobile_number.getText().toString().trim())){
                reg_mobile_number.setError("Please fill out this field with valid phone number");
            }
        }
        if (reg_national_number.getText().toString().trim().isEmpty()) {
            reg_national_number.setError("Please fill out this field");
            return false;
        } else {

        }
        if (reg_address.getText().toString().trim().isEmpty()) {
            reg_address.setError("Please fill out this field");
            return false;
        } else {

        }
        if (reg_password.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_password.setError("Please fill out this field");
            return false;
        } else {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
            //Here you can write the codes for checking password
        }
        if (reg_password_confirm.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_password_confirm.setError("Please fill out this field");
            return false;
        } else {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
        }
        if (!reg_password.getText().toString().trim().isEmpty()) {
            if (!reg_password.getText().toString().trim().matches(reg_password_confirm.getText().toString().trim()) ) {
                reg_password_confirm.setError("Please fill out this  with the same password");
                reg_password_confirm.setText("");
                return false;
            }
        }
        return true;

    }

    private boolean checkPhoneNumber(String phoneNumber){

        //check length of number and if it starts with "09"
        if (phoneNumber.length() != 10 || phoneNumber.charAt(0)!= '0' || phoneNumber.charAt(1) != '9') {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneNumber);
        return !matcher.find();

    }


}
