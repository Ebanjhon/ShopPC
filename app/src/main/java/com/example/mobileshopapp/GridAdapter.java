package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends BaseAdapter {
    Context context;
    List<Product> products = new ArrayList<>();
    LayoutInflater inflater;
    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

//    public GridAdapter(Context context, String[] nameProduct, String[] priceProduct, String[] imgProduct) {
//        this.context = context;
//        this.nameProduct = nameProduct;
//        this.priceProduct = priceProduct;
//        this.imgProduct = imgProduct;
//    }

    public GridAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    // hiển thị sản phẩm
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
        {
            view = inflater.inflate(R.layout.grid_products, null);
        }
        TextView btnAdd = view.findViewById(R.id.buttonAdd);
        ImageView imageView = view.findViewById(R.id.gridImg);
        TextView namePro = view.findViewById(R.id.gridNameProduct);
        TextView pricePro = view.findViewById(R.id.gridPriceProduct);

        Picasso.get().load(products.get(position).getImage()).into(imageView);
        namePro.setText(setNameProduct(products.get(position).getName(), 25));
        String formattedNumber = formatter.format(products.get(position).getPrice());
        formattedNumber = formattedNumber.replace(',', '.');
        pricePro.setText(formattedNumber + " VNĐ") ;

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xử lý thêm sản pẩm vào giỏ
                Toast.makeText(context, "add ok", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    // hàm giới hạn chuổi
    public String setNameProduct(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength) + "...";
        } else {
            return str;
        }
    }
}
