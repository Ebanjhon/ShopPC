package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>  {
    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    Context context;
    TextView totalPrice;
    private List<Cart> cartList;
    private double totalPriceProduct;
    CartManager cartMana;
    public CartAdapter(Context context, List<Cart> cartList, TextView total) {
        this.cartList = cartList;
        this.context = context;
        cartMana = new CartManager(context);
        this.totalPrice = total;
    }

    public double getTotalPriceProduct() {
        return totalPriceProduct;
    }

    public void setTotalPriceProduct(double totalPriceProduct) {
        this.totalPriceProduct = totalPriceProduct;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);
        totalPriceCart(cartList);
        holder.productName.setText(cartItem.getName());
        String formattedNumber = formatter.format(cartItem.getPrice());
        formattedNumber = formattedNumber.replace(',', '.');
        holder.productPrice.setText(formattedNumber + "đ");
        holder.productQuantity.setText(String.format("x%d", cartItem.getQuantity()));
        Picasso.get().load(cartItem.getImage()).into(holder.productImage);

        holder.btnIncrease.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartList.get(position).setQuantity(cartItem.getQuantity());
            holder.productQuantity.setText(String.format("x%d", cartItem.getQuantity()));
            cartMana.updateCartItemQuantity(cartItem.getIdCart(),cartItem.getQuantity());
            totalPriceCart(cartList);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if(cartItem.getQuantity() == 1){return;}
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartList.get(position).setQuantity(cartItem.getQuantity());
            holder.productQuantity.setText(String.format("x%d", cartItem.getQuantity()));
            cartMana.updateCartItemQuantity(cartItem.getIdCart(),cartItem.getQuantity());
            totalPriceCart(cartList);
        });

        holder.btnDelete.setOnClickListener(v -> {
            cartMana.removeCartItem(cartItem.getIdCart());
            // Xóa item khỏi danh sách và thông báo cho adapter
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            // cập nhật giá
            totalPriceCart(cartList);
        });
        totalPriceCart(cartList);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQuantity, btnDelete;
        ImageButton btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btn_deleteProduct);
        }
    }

    // hàm tính tổng cart
    public void totalPriceCart(List<Cart> cartList){
        double total = 0.0;
        for (Cart cart : cartList) {
            total += cart.getPrice() * cart.getQuantity();
        }
        setTotalPriceProduct(total);
        // Định dạng số với dấu phân cách hàng nghìn và ký tự tiền tệ
        DecimalFormat formatter = new DecimalFormat("#,###.##đ");
        totalPrice.setText( formatter.format(total));
    }
}