package com.example.mobileshopapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class CategoryAdaptor extends BaseAdapter {
    Context context;
    Category[] cate;
    LayoutInflater inflater;
    View v;
    Category category = null;

    // constructor
    public CategoryAdaptor(Context context, Category[] cate, View view) {
        this.context = context;
        this.cate = cate;
        this.inflater = LayoutInflater.from(context);
        this.v = view;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getCount() {
        return cate.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_category, null);

        TextView txtview = (TextView) view.findViewById(R.id.nameCate);
        txtview.setText(cate[i].getName());

        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.editCate);
        ImageButton btnCancelEdit = (ImageButton) v.findViewById(R.id.btnCancelEdit);
        Button btnUpdateOrCreate = (Button) v.findViewById(R.id.btnCreateUpdateCate);

        EditText editTextCate = v.findViewById(R.id.editTextCategory);
        // khi bấm item để chỉnh sửa
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCate.setText(cate[i].getName());
                btnCancelEdit.setVisibility(View.VISIBLE);
                btnUpdateOrCreate.setText("Update");
                Drawable drawable = btnUpdateOrCreate.getBackground();
                DrawableCompat.setTint(drawable, Color.parseColor("#FF5733"));
                btnUpdateOrCreate.setBackground(drawable);
                setCategory(cate[i]);
            }
        });

        return view;
    }
}
