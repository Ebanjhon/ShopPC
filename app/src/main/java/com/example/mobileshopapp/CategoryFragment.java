package com.example.mobileshopapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {
    Button btnCreateUpdate;
    ImageButton imgCancel;
    EditText txtEditCate;
    List<Category> category = new ArrayList<>();
    // tạo đối tượng firebase store để tương tác với cơ sowr dữn liệu
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CategoryAdaptor cateAdaptor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        btnCreateUpdate = view.findViewById(R.id.btnCreateUpdateCate);
        imgCancel = view.findViewById(R.id.btnCancelEdit);
        txtEditCate = view.findViewById(R.id.editTextCategory);
        ListView listv = (ListView) view.findViewById(R.id.listViewCate);
        // hàm lấy dữ liệu category
        db.collection("Categories")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        category.clear();
                        // Nạp dữ liệu vào list
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            Category c = new Category(id, name);
                            category.add(c);
                        }
                        // Hiển thị dữ liệu
                        cateAdaptor = new CategoryAdaptor(getActivity(), category, view);
                        listv.setAdapter(cateAdaptor);
                    }
                });

        // xử lý khi có nhấn vào nút cập nhật hoặc tạo
        btnCreateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> cate = new HashMap<>();
                cate.put("name",txtEditCate.getText().toString());
                if(cate.get("name").toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Tên danh mục bị trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cateAdaptor.category == null)
                {
                    db.collection("Categories").add(cate).addOnSuccessListener(aVoid -> {
                                // Xử lý khi lưu dữ liệu thành công
                                Toast.makeText(getActivity(), "Đã thêm thành công", Toast.LENGTH_SHORT).show();
                                txtEditCate.setText("");
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý khi lưu dữ liệu thất bại
                                System.err.println("Lỗi khi thêm dữ liệu: " + e.getMessage());
                            });
                }else{
                    // Cập nhật dữ liệu
                    db.collection("Categories")
                            .document( cateAdaptor.getCategory().getId())
                            .update(cate)
                            .addOnSuccessListener(aVoid -> {
                                // Xử lý khi cập nhật thành công
                                Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                txtEditCate.setText("");
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý khi cập nhật thất bại
                                Toast.makeText(getActivity(), cateAdaptor.getCategory().getName(), Toast.LENGTH_SHORT).show();
                            });
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