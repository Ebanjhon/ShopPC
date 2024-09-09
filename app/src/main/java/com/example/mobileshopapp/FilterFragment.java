package com.example.mobileshopapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends Fragment {
    FlexboxLayout flexbox, flexBoxCompany;
    Button btnFilter;
    RadioGroup radioGrp;
    FrameLayout frameLayout;
    ListView listViewProduct;
    AdapterFilter adapterFilter;
    private int selectedIndex = -1;
    final int[] index = new int[1];
    private List<Product> productList = new ArrayList<>();
    private List<Company> companies = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String selectedCategoryId = "";
    private String selectedCompanyId = "";

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        // ánh xạ
        flexbox = view.findViewById(R.id.flexbox);
        flexBoxCompany = view.findViewById(R.id.flexboxCompany);
        btnFilter = view.findViewById(R.id.btnfiler);
        frameLayout = view.findViewById(R.id.frameLayoutPrice);
        listViewProduct = view.findViewById(R.id.listViewProduct);

//        adapterFilter = new AdapterFilter(filteredProducts,getContext(),inflater);

//        listViewProduct.setAdapter(adapterFilter);

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

                                for (QueryDocumentSnapshot document : value) {
                                    String id = document.getId();
                                    String name = document.getString("name");
                                    Category c = new Category(id, name);
                                    categories.add(c);
                                }
                                // khởi tạo dữ liệu category
                                //String[] cate = {"all", "Màn hình", "bàn phím", "chuột", "ổ cứng", "Quạt tản nhiệt"};
                                // Tạo HorizontalScrollView
                                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());
                                // Tạo LinearLayout để chứa các RadioButton
                                LinearLayout linearLayout = new LinearLayout(getActivity());
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                // Khởi tạo mảng String với kích thước bằng số lượng Category trong danh sách
                                String[] categoryNames = new String[categories.size()];

                                // Duyệt qua danh sách Category và lấy tên của mỗi Category
                                for (int i = 0; i < categories.size(); i++) {
                                    categoryNames[i] = categories.get(i).getName();
                                }


                                final RadioButton[] checkedRadioButton = {null};

                                // Duyệt qua danh sách các tùy chọn và tạo các `RadioButton`
                                for (int i = 0; i < categoryNames.length; i++) {
                                    String option = categoryNames[i];

                                    // Tạo `RadioButton`
                                    RadioButton radioButton = new RadioButton(getActivity());
                                    radioButton.setText(option);
                                    radioButton.setTextSize(17);
                                    radioButton.setButtonDrawable(null); // Loại bỏ biểu tượng radio mặc định
                                    radioButton.setTextColor(getResources().getColorStateList(R.color.radio_button_text_color));
                                    radioButton.setBackgroundResource(R.drawable.radio_button); // Áp dụng background tùy chỉnh
                                    radioButton.setPadding(20, 16, 20, 16); // Thêm padding

                                    // Tạo LayoutParams và thêm margin
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    // Thiết lập margin
                                    params.setMargins(10, 8, 10, 8); // Thay đổi giá trị margin theo nhu cầu của bạn

                                    // Áp dụng LayoutParams cho RadioButton
                                    radioButton.setLayoutParams(params);

                                    // Lưu trữ index dưới dạng tag
                                    radioButton.setTag(i);

                                    // Xử lý sự kiện `onClick` cho từng `RadioButton`
                                    radioButton.setOnClickListener(v -> {
                                        if (checkedRadioButton[0] != null && checkedRadioButton[0] != radioButton) {
                                            checkedRadioButton[0].setChecked(false); // Bỏ chọn `RadioButton` trước đó
                                        }
                                        checkedRadioButton[0] = radioButton; // Cập nhật `RadioButton` đã chọn hiện tại

                                        // Lấy index của `RadioButton` đã chọn
                                        selectedIndex = (int) radioButton.getTag();
                                        selectedCategoryId = categories.get(selectedIndex).getId();;
                                    });
                                    linearLayout.addView(radioButton);

                                }
                                horizontalScrollView.addView(linearLayout);
                                flexbox.addView(horizontalScrollView);
                            }
                        });

        db.collection("Companies")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        companies.clear();
                        // Nạp dữ liệu vào list
                        Company c0 = new Company("", "Tất cả");
                        companies.add(c0);

                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            Company c = new Company(id, name);
                            companies.add(c);
                        }
                        // Tạo HorizontalScrollView
                        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());
                        // Tạo LinearLayout để chứa các RadioButton
                        LinearLayout linearLayout = new LinearLayout(getActivity());
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        // Khởi tạo mảng String với kích thước bằng số lượng Company trong danh sách
                        String[] companiesNames = new String[companies.size()];

                        // Duyệt qua danh sách Company và lấy tên của mỗi Company
                        for (int i = 0; i < companies.size(); i++) {
                            companiesNames[i] = companies.get(i).getName();
                        }


                        final RadioButton[] checkedRadioButton = {null};

                        // Duyệt qua danh sách các tùy chọn và tạo các `RadioButton`
                        for (int i = 0; i < companiesNames.length; i++) {
                            String option = companiesNames[i];

                            // Tạo `RadioButton`
                            RadioButton radioButton = new RadioButton(getActivity());
                            radioButton.setText(option);
                            radioButton.setTextSize(17);
                            radioButton.setButtonDrawable(null); // Loại bỏ biểu tượng radio mặc định
                            radioButton.setTextColor(getResources().getColorStateList(R.color.radio_button_text_color));
                            radioButton.setBackgroundResource(R.drawable.radio_button); // Áp dụng background tùy chỉnh
                            radioButton.setPadding(20, 16, 20, 16); // Thêm padding

                            // Tạo LayoutParams và thêm margin
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            // Thiết lập margin
                            params.setMargins(10, 8, 10, 8); // Thay đổi giá trị margin theo nhu cầu của bạn

                            // Áp dụng LayoutParams cho RadioButton
                            radioButton.setLayoutParams(params);

                            // Lưu trữ index dưới dạng tag
                            radioButton.setTag(i);

                            // Xử lý sự kiện `onClick` cho từng `RadioButton`
                            radioButton.setOnClickListener(v -> {
                                if (checkedRadioButton[0] != null && checkedRadioButton[0] != radioButton) {
                                    checkedRadioButton[0].setChecked(false); // Bỏ chọn `RadioButton` trước đó
                                }
                                checkedRadioButton[0] = radioButton; // Cập nhật `RadioButton` đã chọn hiện tại

                                // Lấy index của `RadioButton` đã chọn
                                selectedIndex = (int) radioButton.getTag();
                                selectedCompanyId = companies.get(selectedIndex).getId();
                            });
                            linearLayout.addView(radioButton);

                        }
                        horizontalScrollView.addView(linearLayout);
                        flexBoxCompany.addView(horizontalScrollView);
                    }
                });

        db.collection("Products")
                        .addSnapshotListener(((value, error) -> {
                            if (error != null) {
                                Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(value!=null)
                            {
                                productList.clear();

                                for (QueryDocumentSnapshot document : value) {
                                    String id = document.getId();
                                    String name = document.getString("name");
                                    String image = document.getString("image");
                                    String company = document.getString("company");
                                    String category = document.getString("category");
                                    String detail = document.getString("detail");
                                    int price = document.getLong("price").intValue();
                                    Product p = new Product(id,name,image,company,category,detail,price);
                                    productList.add(p);
                                }

                            }
                        }));

        btnFilter.setOnClickListener(v -> {
            filteredProducts.clear();

            // Get selected category
            //String selectedCategory = selectedIndex != -1 ? categories.get(selectedIndex).getName() : "all";

            // Get price range
            String priceFromText = ((EditText) view.findViewById(R.id.priceFrom)).getText().toString();
            String priceToText = ((EditText) view.findViewById(R.id.priceTo)).getText().toString();

            double priceFrom = priceFromText.isEmpty() ? 0 : Double.parseDouble(priceFromText);
            double priceTo = priceToText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(priceToText);

            // Filter products based on category and price range
            for (Product product : productList) {
                boolean matchesCategory = selectedCategoryId.isEmpty() || product.getCategory().equals(selectedCategoryId);
                boolean matchesCompany = selectedCompanyId.isEmpty() || product.getCompany().equals(selectedCompanyId);
                boolean matchesPrice = product.getPrice() >= priceFrom && product.getPrice() <= priceTo;

                if (matchesCategory && matchesCompany && matchesPrice) {
                    filteredProducts.add(product);
                }
            }
            adapterFilter = new AdapterFilter(filteredProducts, getContext(),inflater);
            listViewProduct.setAdapter(adapterFilter);
            // Notify the adapter of the changes
            adapterFilter.notifyDataSetChanged();


            // Display a Toast message
            Toast.makeText(getActivity(), "Filtered " + filteredProducts.size() + " products", Toast.LENGTH_SHORT).show();
        });
        // xử lý xử kiệt nhấn nút chọn giá
        radioGrp = view.findViewById(R.id.groupRadio);
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioAll)
                {
                    frameLayout.setVisibility(View.GONE);
                }else{
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

}
