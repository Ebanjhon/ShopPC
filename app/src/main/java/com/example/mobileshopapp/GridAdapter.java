package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridAdapter extends BaseAdapter {
    Context context;
    String[] nameProduct, priceProduct, imgProduct;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] nameProduct, String[] priceProduct, String[] imgProduct) {
        this.context = context;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.imgProduct = imgProduct;
    }

    @Override
    public int getCount() {
        return nameProduct.length;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
        {
            view = inflater.inflate(R.layout.grid_products, null);
        }

        ImageView imageView = view.findViewById(R.id.gridImg);
        TextView namePro = view.findViewById(R.id.gridNameProduct);
        TextView pricePro = view.findViewById(R.id.gridPriceProduct);

        Picasso.get().load(imgProduct[position]).into(imageView);
        namePro.setText(nameProduct[position]);
        pricePro.setText(priceProduct[position]);

        return view;
    }
}
