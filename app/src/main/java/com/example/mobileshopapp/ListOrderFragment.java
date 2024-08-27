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

public class ListOrderFragment extends Fragment {

    private RecyclerView rv_list_order;
    private AdapterDateOrder adapterDateOrder;
    private Order order;
    public ListOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_list_order = view.findViewById(R.id.rv_list_order);

        order = new Order();
        adapterDateOrder = new AdapterDateOrder(order.orders(), getActivity());
        rv_list_order.setAdapter(adapterDateOrder);
        rv_list_order.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}