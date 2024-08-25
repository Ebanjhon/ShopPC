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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompanyAdaptor extends BaseAdapter {
    Context context;
    private List<Company> compa;
    LayoutInflater inflater;
    View v;
    Company company = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CompanyAdaptor(Context context, List<Company> compa, View v) {
        this.context = context;
        this.compa = compa;
        this.inflater = LayoutInflater.from(context);
        this.v = v;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int getCount() {
        return compa.size();
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
        view = inflater.inflate(R.layout.activity_custom_company, null);
        // hiển thị dữ liệu cho từng hàng
        ImageView imgLogo = (ImageView) view.findViewById(R.id.imgLogoCompany);
        Picasso.get()
                .load(compa.get(i).getImage())
                .into(imgLogo);
        TextView txtview = (TextView) view.findViewById(R.id.nameCom);
        txtview.setText(compa.get(i).getName());
        ImageButton btnDelete = view.findViewById(R.id.deleteCom);

        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.editCom);
        ImageButton btnCancelEdit = (ImageButton) v.findViewById(R.id.btnCancelEdit);
        Button btnUpdateOrCreate = (Button) v.findViewById(R.id.btnCreateUpdateCom);
        ImageView viewImg = (ImageView) v.findViewById(R.id.imageViewLogo);
        EditText editTextCom = v.findViewById(R.id.editTextCom);

        // xử lý sự kiện btn
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCom.setText(compa.get(i).getName());
                btnCancelEdit.setVisibility(View.VISIBLE);
                btnUpdateOrCreate.setText("Update");
                Drawable drawable = btnUpdateOrCreate.getBackground();
                DrawableCompat.setTint(drawable, Color.parseColor("#FF5733"));
                btnUpdateOrCreate.setBackground(drawable);
                company = (compa.get(i));
                Picasso.get()
                        .load(compa.get(i).getImage())
                        .into(viewImg);
            }
        });

        // hàm xóa item
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
                        db.collection("Companies")
                                .document(compa.get(i).getId())
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
