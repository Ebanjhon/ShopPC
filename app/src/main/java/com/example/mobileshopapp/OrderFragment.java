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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu từ Firebase
        db.collection("Orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String orderId = document.getId();
                            String orderDate = document.getString("orderDate");
                            String idUser = document.getString("userId");
                            Boolean state = document.getBoolean("isPaid");
                            double total = document.getDouble("total");

                            Order order = new Order(orderId, idUser, orderDate, state, total);
                            orderList.add(order);
                        }
                        // Hiển thị thông báo thành công
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}