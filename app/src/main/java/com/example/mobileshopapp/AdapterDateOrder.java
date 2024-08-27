package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDateOrder extends RecyclerView.Adapter<AdapterDateOrder.ViewHolder> {
    private List<Order> listOrder;
    private Context context;
    private DatabaseHelper dbHelper;
    private Map<String, List<Order>> groupedOrders;

    public AdapterDateOrder(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.groupedOrders = groupOrdersByMonth(listOrder);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String month = (new ArrayList<>(groupedOrders.keySet())).get(position);
        holder.tvDate.setText("Tháng " + month);

        List<Order> ordersForMonth = groupedOrders.get(month);

        AdapterOrder orderAdapter = new AdapterOrder(ordersForMonth, context);
        holder.rvOrder.setLayoutManager(new LinearLayoutManager(context));
        holder.rvOrder.setAdapter(orderAdapter);
    }

    @Override
    public int getItemCount() {
        return groupedOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        RecyclerView rvOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.textViewDate);
            rvOrder = itemView.findViewById(R.id.recyclerViewOrder);
        }
    }
    private Map<String, List<Order>> groupOrdersByMonth(List<Order> orders) {
        Map<String, List<Order>> groupedOrders = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        for (Order order : orders) {
            String month = sdf.format(Long.parseLong(order.getOrderDate()));
            if (!groupedOrders.containsKey(month)) {
                groupedOrders.put(month, new ArrayList<>());
            }
            groupedOrders.get(month).add(order);
        }

        return groupedOrders;
    }
}
