package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Category> categories = new ArrayList<>();
    private List<Company> companies = new ArrayList<>();
    private Button btnCraete, btnFilter;
    private List<Product> products = new ArrayList<>();
    private String cateID;
    private String compaID;
    private ListView listProducts;
    private EditText txtSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        btnCraete = view.findViewById(R.id.buttonAddProduct);
        listProducts = view.findViewById(R.id.listViewProduct);
        btnFilter = view.findViewById(R.id.buttonFilterProduct);
        txtSearch = view.findViewById(R.id.searchProduct);
        btnCraete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new CreateEditProductFragment());
            }
        });

        // Dữ liệu cho Spinner company
        CollectionReference companiesCollection = db.collection("Companies");
        // Lấy tất cả tài liệu từ collection "Users"
        companiesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> compa = new ArrayList<>();
                    companies.clear();
                    companies.add(new Company("0", "Tất cả", ""));
                    compa.add("All");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String img = document.getString("image");
                        compa.add(name);
                        Company c = new Company(id, name, img);
                        companies.add(c);
                    }
                    Spinner spinnerCompa = view.findViewById(R.id.spinnerCompa);
                    ArrayAdapter<String> adapterCompany = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, compa);
                    adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCompa.setAdapter(adapterCompany);
                    // Thêm OnItemSelectedListener để lấy index của mục đang chọn
                    spinnerCompa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position != 0)
                                compaID = companies.get(position).getId();
                            else
                                compaID = "";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Xử lý khi không có mục nào được chọn (tùy chọn)
                        }
                    });
                }
            }
        });

        // Dữ liệu cho Spinner cate
        CollectionReference cateCollection = db.collection("Categories");
        // Lấy tất cả tài liệu từ collection "Users"
        cateCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> cate = new ArrayList<>();
                    categories.clear();
                    categories.add(new Category("0","Tất cả"));
                    cate.add("All");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        cate.add(name);
                        Category c = new Category(id, name);
                        categories.add(c);
                    }
                    Spinner spinnerCate = view.findViewById(R.id.spinnerCategory);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cate);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCate.setAdapter(adapter);

                    // Thêm OnItemSelectedListener để lấy index của mục đang chọn
                    spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position != 0)
                                cateID = categories.get(position).getId();
                            else
                                cateID = "";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Xử lý khi không có mục nào được chọn (tùy chọn)
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // hiển thị danh sách sản phẩm
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
                            String detail = document.getString("detail");
                            int price = document.getLong("price").intValue();
                            Product p = new Product(id, name, imageProduct,company,cate, detail, price );
                            products.add(p);
                        }
                        // hiển thị dữ liệu
                        AdapterProduct adapterProduct = new AdapterProduct(getContext(),products);
                        listProducts.setAdapter(adapterProduct);
                    }
                });

        // lấy dữ liệu có chọn lọc
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = txtSearch.getText().toString();
                // Khởi tạo một truy vấn cơ bản
                Query query = db.collection("Products");

                // Lọc theo category nếu không trống
                if (!cateID.isEmpty()) {
                    query = query.whereEqualTo("category", cateID);
                }

                if (!compaID.isEmpty()) {
                    query = query.whereEqualTo("company", compaID);
                }

                // Thực thi truy vấn với điều kiện đã thiết lập
                query.addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        products.clear(); // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String company = document.getString("company");
                            String cate = document.getString("category");
                            String imageProduct = document.getString("image");
                            String detail = document.getString("detail");
                            int price = document.getLong("price").intValue();

                            // Kiểm tra nếu name chứa chuỗi con
                            if (search != null && !search.isEmpty() && name != null && name.contains(search.toLowerCase())) {
                                Product p = new Product(id, name, imageProduct, company, cate, detail, price);
                                products.add(p);
                            }else{
                                Product p = new Product(id, name, imageProduct, company, cate, detail, price);
                                products.add(p);
                            }
                        }
                        AdapterProduct adapterProduct = new AdapterProduct(getContext(), products);
                        listProducts.setAdapter(adapterProduct);
                    }
                });

            }
        });


        return view;
    }

    private void chuyenDoiManHinh(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}