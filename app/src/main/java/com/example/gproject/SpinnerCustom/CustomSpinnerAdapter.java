package com.example.gproject.SpinnerCustom;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gproject.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<CustomSpinnerItem> {

    public CustomSpinnerAdapter(Context context, ArrayList<CustomSpinnerItem> itemList) {
        super(context, 0, itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_drop_down_layout, parent, false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.iv_drop_down);
        TextView textView = convertView.findViewById(R.id.tv_drop_down);

        CustomSpinnerItem currentItem = getItem(position);

        if(currentItem != null){
            imageView.setImageResource(currentItem.getProviderImage());
            textView.setText(currentItem.getProviderName());
        }

        return convertView;

    }
}
