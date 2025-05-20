package com.example.shoesstore.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shoesstore.dto.CategoryDto;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryDto> {
    public CategoryAdapter(Context context, List<CategoryDto> categories) {
        super(context, android.R.layout.simple_dropdown_item_1line, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,  ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());
        return view;
    }
}
