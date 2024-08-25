package com.example.mobileshopapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshopapp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private LinearLayout linearlistCate;
    private GridView gridView;
    List<Category> categories = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // ánh xạ
        linearlistCate = view.findViewById(R.id.linnerLayoutShowListCate);
        gridView = view.findViewById(R.id.gridViewHome);
        // lấy dữ liệu
        db.collection("Categories")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        categories.clear();
                        // Nạp dữ liệu vào list
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            Category c = new Category(id, name);
                            categories.add(c);
                        }
                        // Hiển thị dữ liệu
                        for (Category cate : categories) {
                            final Category item = cate;

                            TextView textView = new TextView(getActivity());
                            textView.setText(item.getName());
                            textView.setTextSize(16);
                            textView.setGravity(Gravity.CENTER);
                            textView.setPadding(5,0,0,0);
                            textView.setTextColor(Color.parseColor("#FFFFFF"));
                            textView.setPadding(10, 10, 10, 10);

                            // Thiết lập layout parameters cho TextView
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 5, 0);
                            textView.setLayoutParams(params);

                            // Thiết lập sự kiện click cho TextView
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Hiển thị id và name của item khi nhấn vào TextView
                                    Toast.makeText(getActivity(), "ID: " + item.getId() + ", Name: " + item.getName(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Thêm TextView vào LinearLayout
                            linearlistCate.addView(textView);
                        }
                    }
                });

        // dữ liệu product
        db.collection("Products")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        products.clear();
                        // Nạp dữ liệu vào list
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String company = document.getString("company");
                            String cate = document.getString("category");
                            String imageProduct = document.getString("image");
                            int price = document.getLong("price").intValue();
                            Product p = new Product(id, name, imageProduct,company,cate, price );
                            products.add(p);
                        }
                        GridAdapter gridAdapter = new GridAdapter(getActivity(), products);
                        gridView.setAdapter(gridAdapter);
                        // Hiển thị dữ liệu
                    }
                });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // chuyển trang product detail và truyền id
                chuyenDoiManHinh(new ProductDetailFragment(), products.get(i));
            }
        });

        return view;
    }

    private void chuyenDoiManHinh(Fragment fragment, Product product) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}