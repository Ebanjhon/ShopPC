package com.example.mobileshopapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CategoryAdaptor extends BaseAdapter {
    Context context;
    private List<Category> cate;
    LayoutInflater inflater;
    View v;
    Category category = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // constructor
    public CategoryAdaptor(Context context, List<Category> cate, View view) {
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
        return cate.size();
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
        txtview.setText(cate.get(i).getName());
        ImageButton btnDelete = view.findViewById(R.id.deleteCate);

        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.editCate);
        ImageButton btnCancelEdit = (ImageButton) v.findViewById(R.id.btnCancelEdit);
        Button btnUpdateOrCreate = (Button) v.findViewById(R.id.btnCreateUpdateCate);

        EditText editTextCate = v.findViewById(R.id.editTextCategory);
        // khi bấm item để chỉnh sửa
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCate.setText(cate.get(i).getName());
                btnCancelEdit.setVisibility(View.VISIBLE);
                btnUpdateOrCreate.setText("Update");
                Drawable drawable = btnUpdateOrCreate.getBackground();
                DrawableCompat.setTint(drawable, Color.parseColor("#FF5733"));
                btnUpdateOrCreate.setBackground(drawable);
                setCategory(cate.get(i));
            }
        });

        // hàm xóa dư lieu
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn có chắc chắn muốn xóa!");

                // Thêm nút "OK"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xóa document có ID được cung cấp
                        db.collection("Categories")
                                .document(cate.get(i).getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Xử lý khi xóa thành công
                                    Toast.makeText(context.getApplicationContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi xóa thất bại
                                });
                    }
                });

                // Thêm nút "Cancel"
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Tạo và hiển thị AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return view;
    }
}
