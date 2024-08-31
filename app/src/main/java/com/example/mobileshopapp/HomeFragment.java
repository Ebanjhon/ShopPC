package com.example.mobileshopapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshopapp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private LinearLayout linearlistCate;
    private GridView gridView;
    List<Category> categories = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    EditText editText;
    String search;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    GridAdapter gridAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        editText = view.findViewById(R.id.searchProuct);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search = editText.getText().toString();
                    showProduct(categories.get(0), search);
                    return true;
                }
                return false;
            }
        });
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
                        Category c0 = new Category("", "Tất cả");
                        categories.add(c0);
                        search = editText.getText().toString();
                        showProduct(categories.get(0), search);
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
                                    Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
                                    search = editText.getText().toString();
                                    showProduct(item, search);
                                }
                            });
                            // Thêm TextView vào LinearLayout
                            linearlistCate.addView(textView);
                        }
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

    private void showProduct(Category item, String searchQuery){
        CollectionReference productsCollection = db.collection("Products");
        Query query;
        String categoryId = item.getId();

        if (!categoryId.isEmpty() && !searchQuery.isEmpty()) {
            // Lọc theo categoryId và tên sản phẩm
            query = productsCollection
                    .whereEqualTo("category", categoryId)
                    .whereGreaterThanOrEqualTo("name", searchQuery)
                    .whereLessThanOrEqualTo("name", searchQuery + "\uf8ff");
        } else if (!categoryId.isEmpty()) {
            // Lọc theo categoryId
            query = productsCollection.whereEqualTo("category", categoryId);
        } else if (!searchQuery.isEmpty()) {
            // Tìm kiếm theo tên sản phẩm mà không lọc categoryId
            query = productsCollection
                    .whereGreaterThanOrEqualTo("name", searchQuery)
                    .whereLessThanOrEqualTo("name", searchQuery + "\uf8ff");
        } else {
            // Lấy tất cả sản phẩm
            query = productsCollection;
        }

        // Thực hiện truy vấn và lắng nghe dữ liệu
        query.addSnapshotListener((value, error) -> {
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
                    String detail = document.getString("detail");
                    int price = document.getLong("price").intValue();
                    Product p = new Product(id, name, imageProduct, company, cate, detail, price);
                    products.add(p);
                }
                // Cập nhật Adapter với dữ liệu mới
                gridAdapter = new GridAdapter(getActivity(), products);
                gridView.setAdapter(gridAdapter);
            }
        });

    }
}