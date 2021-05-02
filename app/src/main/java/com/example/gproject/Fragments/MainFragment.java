package com.example.gproject.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.gproject.Activities.LogInActivity;
import com.example.gproject.R;
import com.example.gproject.SpinnerCustom.CustomSpinnerAdapter;
import com.example.gproject.SpinnerCustom.CustomSpinnerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    EditText phoneBox;
    Button sendButton;
    Spinner spinner;
    Spinner customSpinner;
    String spinnerSelectedItem = "";

    String selectedProvider = "";

    private ArrayList<CustomSpinnerItem> mCustomSpinnerItems;
    private CustomSpinnerAdapter mCustomSpinnerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        phoneBox = view.findViewById(R.id.phoneBox);
        sendButton = view.findViewById(R.id.sendButton);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        customSpinner = (Spinner) view.findViewById(R.id.custom_spinner);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final String[] spinnerCategories = getResources().getStringArray(R.array.categories);
        final ArrayList<String> categoriesList = new ArrayList<>(Arrays.asList(spinnerCategories));

        //Custom spinner
        initCustomSpinnerList();
        mCustomSpinnerAdapter = new CustomSpinnerAdapter(getContext(), mCustomSpinnerItems);
        customSpinner.setAdapter(mCustomSpinnerAdapter);
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerItem customSpinnerItem = (CustomSpinnerItem) parent.getItemAtPosition(position);
                selectedProvider = customSpinnerItem.getProviderName();
                Toast.makeText(getContext(), selectedProvider, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoriesList);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
                String number = phoneBox.getText().toString().trim();

                if (!checkPhoneNumber(number)){
                    Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }
                TelecomManager telecomManager = (TelecomManager) getActivity().getSystemService(Context.TELECOM_SERVICE);

//                //To call from SIM 1
//                Uri uri = Uri.fromParts("tel",number, "");
//                Bundle extras = new Bundle();  extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,primaryPhoneAccountHandle);
//                telecomManager.placeCall(uri, extras);


                String mtnCode = "*150*75169*"+ number + "*"+ spinnerSelectedItem + "#";
                String syriatelCode = "*150*1*74862*1*"+ spinnerSelectedItem + "*" + number + "*" + number + "#";
                Toast.makeText(getContext(), syriatelCode, Toast.LENGTH_LONG).show();
                System.out.println(syriatelCode);

                //To call from SIM 2
                Uri uri1 = Uri.parse("tel:" + Uri.encode( syriatelCode ));
                final int REQUEST_PHONE_CALL = 1;
                Bundle extras = new Bundle();
                /**
                 * ApI > = 26
                 *
                 *        TelephonyManager telMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                 *        int simState = telMgr.getSimState(1);
                 */
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, getSimHandler(telecomManager,  1) );

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    telecomManager.placeCall(uri1, extras);
                }
            }
        });
    }

    //Initializing of custom spinner
    private void initCustomSpinnerList() {
        mCustomSpinnerItems = new ArrayList<>();
        mCustomSpinnerItems.add(new CustomSpinnerItem("MTN", R.drawable.ic_mtn ));
        mCustomSpinnerItems.add(new CustomSpinnerItem("Syriatel", R.drawable.ic_baseline_input_phone ));

    }

    //spinner function
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        spinnerSelectedItem = parent.getItemAtPosition(position).toString().trim();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + spinnerSelectedItem, Toast.LENGTH_LONG).show();

    }

    //spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @TelecomManager Object
     * @int value represent  0 for main sim card , any number greater than 0 for second sim card
     * if value is negative it will use main sim
     *
      */
    private PhoneAccountHandle getSimHandler(TelecomManager telecomManager, int simNumber) {
        int mDefaultSimNum = 0;
        if(simNumber <= 0){
            simNumber = mDefaultSimNum;
        }

        //To find SIM ID
        String primarySimId = "", secondarySimId = "";
        SubscriptionManager subscriptionManager = (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 2);

        }
        List<SubscriptionInfo> subList = subscriptionManager.getActiveSubscriptionInfoList();
        int index = -1;
        for (SubscriptionInfo subscriptionInfo : subList) {
            index++;
            if (index == 0) {
                primarySimId = subscriptionInfo.getIccId();
            } else {
                secondarySimId = subscriptionInfo.getIccId();
            }
        }
        // TO CREATE PhoneAccountHandle FROM SIM ID
        List<PhoneAccountHandle> list = telecomManager.getCallCapablePhoneAccounts();
        PhoneAccountHandle primaryPhoneAccountHandle = null, secondaryPhoneAccountHandle = null;
        for (PhoneAccountHandle phoneAccountHandle : list) {
            if (phoneAccountHandle.getId().contains(primarySimId)) {
                primaryPhoneAccountHandle = phoneAccountHandle;
            }
            if (phoneAccountHandle.getId().contains(secondarySimId)) {
                secondaryPhoneAccountHandle = phoneAccountHandle;
            }
        }
        if(simNumber == 0) {
            return primaryPhoneAccountHandle;
        }else {
            return secondaryPhoneAccountHandle;
        }
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
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