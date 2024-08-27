package com.example.mobileshopapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private List<Order> listOrder;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterOrder(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = listOrder.get(position);
        fetchProductData(order, holder);
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvPrice, tvState, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.textViewShop);
            tvPrice = itemView.findViewById(R.id.textViewOrderTotalValue);
            tvState = itemView.findViewById(R.id.textViewOrderStatus);
            tvDate = itemView.findViewById(R.id.textViewOrderTimeValue);
        }
    }

    private void fetchProductData(Order order, ViewHolder holder) {
        Log.d("TAGAPI", "fetchProductData: " + order.getProductID());
        db.collection("Products")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        // Nạp dữ liệu vào list
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            Log.d("TAGAPI", id);
                            if (Objects.equals(id, order.getProductID())) {
                                String name = document.getString("name");
                                int price = document.getLong("price").intValue();
                                holder.tvProductName.setText(name);
                                holder.tvPrice.setText(price + "đ");
                                holder.tvState.setText(order.getState());
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                holder.tvDate.setText(formatter.format(Long.parseLong(order.getOrderDate())));
                            }
                        }
                        // Hiển thị dữ liệu
                    }
                });
    }
}
