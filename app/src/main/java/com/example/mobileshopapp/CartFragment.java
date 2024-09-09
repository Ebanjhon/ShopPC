package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CartFragment extends Fragment {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private TextView total;
    private DatabaseHelper dbHelper;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnPay;
    private List<Cart> cartProduct = new ArrayList<>();
    @Override
    public void onStart() {
        super.onStart();
        // ánh xạ
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            chuyenDoiManHinh(new CartNotLoginFragment());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btnPay = view.findViewById(R.id.btnPay);
        recyclerView = view.findViewById(R.id.cartRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        total = view.findViewById(R.id.txtTong);
        cartManager = new CartManager(getContext());
        List<Cart> initialCartList = cartManager.getAllItemCart();
        cartAdapter = new CartAdapter(getContext(), initialCartList, total);
        recyclerView.setAdapter(cartAdapter);
        dbHelper = new DatabaseHelper(requireContext());
        User user = dbHelper.getUser();

        // Gọi hàm tính tổng giá sau khi adapter đã được thiết lập
        cartAdapter.totalPriceCart(initialCartList);

        // Hàm thanh toán
        btnPay.setOnClickListener(view1 -> {
            if(initialCartList.size() == 0) {
                Toast.makeText(getActivity(), "Bạn chưa chọn sản phẩm nào!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tạo đối tượng Order
            List<Map<String, Object>> cartList = new ArrayList<>();
            for (Cart cart : initialCartList) {
                Map<String, Object> cartMap = new HashMap<>();
                cartMap.put("id", cart.getIdCart());
                cartMap.put("quantity", cart.getQuantity());
                cartList.add(cartMap);
            }
            // Lấy thời gian hiện tại và định dạng
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateTime = sdf.format(new Date());

            // Lưu đơn hàng vào Firestore
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("userId", user.getIdUser());
            orderMap.put("total", cartAdapter.getTotalPriceProduct());
            orderMap.put("carts", cartList);
            orderMap.put("orderDate", currentDateTime); // Thêm ngày giờ
            orderMap.put("isPaid", false);

            // Lưu đơn hàng vào Firestore
            db.collection("Orders")
                    .add(orderMap)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Đã lưu đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                        cartManager.clearCart();
                        chuyenDoiManHinh(new HomeFragment());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Đã lưu đơn hàng thất bại!", Toast.LENGTH_SHORT).show();
                    });
        });

        return view;
    };

    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    };
};