package com.example.mobileshopapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryFragment extends Fragment {
    Button btnCreateUpdate;
    ImageButton imgCancel;
    EditText txtEditCate;
    Category cate = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        btnCreateUpdate = view.findViewById(R.id.btnCreateUpdateCate);
        imgCancel = view.findViewById(R.id.btnCancelEdit);
        txtEditCate = view.findViewById(R.id.editTextCategory);
        Category c1 = new Category("1", "Bàn phím");
        Category c2 = new Category("2", "Ghế");
        Category c3 = new Category("3", "CPU");
        Category c4 = new Category("4", "Mainboar");
        Category c5 = new Category("5", "Card");
        Category c6 = new Category("6", "SSD");
        final Category[][] cate = {{c1, c2, c3, c4, c5, c6}};

        ListView listv = (ListView) view.findViewById(R.id.listViewCate);
        CategoryAdaptor cateAdaptor = new CategoryAdaptor(getActivity().getApplicationContext(), cate[0], view);
        listv.setAdapter(cateAdaptor);

        // xử lý khi có nhấn vào nút

        btnCreateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cateAdaptor.category == null)
                {
                    Toast.makeText(getActivity(), "Create", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), cateAdaptor.getCategory().getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // tắt edit
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCancel.setVisibility(View.GONE);
                btnCreateUpdate.setText("Create");
                Drawable drawable = btnCreateUpdate.getBackground();
                DrawableCompat.setTint(drawable, Color.parseColor("#2D77E5"));
                btnCreateUpdate.setBackground(drawable);
                txtEditCate.setText("");
                cateAdaptor.setCategory(null);
            }
        });

        return view;
    }
}