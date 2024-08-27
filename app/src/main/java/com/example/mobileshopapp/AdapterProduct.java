package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterProduct extends BaseAdapter {
    private List<Product> products;
    private Context context;
    private LayoutInflater inflater;
    private NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    public AdapterProduct(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_product_list_manager, null);
        TextView name, price, count, id;
        ImageView img = view.findViewById(R.id.imageViewProduct);

        name = (TextView) view.findViewById(R.id.nameProduct);
        id = (TextView) view.findViewById(R.id.idProduct);
        count = (TextView) view.findViewById(R.id.countProduct);
        price = (TextView) view.findViewById(R.id.priceProduct);
        Picasso.get().load(products.get(i).getImage()).into(img);
        name.setText(setStringlimeted("Tên Sản phẩm: "+products.get(i).getName(),40));
        id.setText(setStringlimeted("Mã: "+products.get(i).getId(),18));
        count.setText("Số lượng: 10");
        String formattedNumber = formatter.format(products.get(i).getPrice());
        formattedNumber = formattedNumber.replace(',', '.');
        price.setText("Giá: "+formattedNumber + "đ") ;
        return view;
    }

    // hàm giới hạn chuổi
    public String setStringlimeted(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength) + "...";
        } else {
            return str;
        }
    }
}
