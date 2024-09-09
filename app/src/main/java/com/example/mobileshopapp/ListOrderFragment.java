package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListOrderFragment extends Fragment {

    private RecyclerView rvListOrder;
    private FirebaseFirestore db;
    private List<Order> ordersList = new ArrayList<>();
    private UserManager userManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_order, container, false);
        rvListOrder = view.findViewById(R.id.rv_list_order);

        db = FirebaseFirestore.getInstance();
        userManager = new UserManager(getContext());

        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        String idUser = userManager.getFirstUser().getIdUser();
        db.collection("Orders")
                .whereEqualTo("userId", idUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ordersList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String orderId = document.getId();
                            String orderDate = document.getString("orderDate");
                            Boolean state = document.getBoolean("isPaid");
                            double total = document.getDouble("total");

                            Order order = new Order(orderId, idUser, orderDate, state, total);
                            ordersList.add(order);
                        }
                        updateRecyclerView(); // Cập nhật RecyclerView với dữ liệu mới
                    } else {
                        Toast.makeText(getContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateRecyclerView() {
        Map<String, List<Order>> ordersByMonth = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

        for (Order order : ordersList) {
            // Sử dụng phương thức getOrderDateAsDate() đã cập nhật
            String month = monthFormat.format(order.getOrderDateAsDate());

            if (!ordersByMonth.containsKey(month)) {
                ordersByMonth.put(month, new ArrayList<>());
            }

            ordersByMonth.get(month).add(order);
        }

        // Cập nhật RecyclerView với Adapter mới
        AdapterDateOrder adapterDateOrder = new AdapterDateOrder(ordersByMonth, getContext());
        rvListOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListOrder.setAdapter(adapterDateOrder);
    }
}