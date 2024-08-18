package com.example.mobileshopapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshopapp.databinding.ActivityMainBinding;

public class HomeFragment extends Fragment {
    private LinearLayout linearlistCate;
    private GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // ánh xạ
        linearlistCate = view.findViewById(R.id.linnerLayoutShowListCate);
        gridView = view.findViewById(R.id.gridViewHome);
        // khoi tạo giá trị
        Category[] items = {
                new Category("1", "Bánh mỳ"),
                new Category("2", "Bánh chocoby"),
                new Category("3", "Bánh cuốn"),
                new Category("4", "Bánh chả"),
                new Category("5", "Bánh bông lan"),
                new Category("6", "Bánh quế"),
                new Category("7", "Bánh tít")
        };

        // Vòng lặp để tạo và thêm các TextView vào LinearLayout
        for (int i = 0; i < items.length; i++) {
            final Category item = items[i];

            TextView textView = new TextView(getActivity());
            textView.setText(item.getName());
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5,0,0,0);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setPadding(10, 10, 10, 10);

            // Thiết lập layout parameters cho TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 5, 0);
            textView.setLayoutParams(params);

            // Thiết lập sự kiện click cho TextView
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị id và name của item khi nhấn vào TextView
                    Toast.makeText(getActivity(), "ID: " + item.getId() + ", Name: " + item.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            // Thêm TextView vào LinearLayout
            linearlistCate.addView(textView);
        }

        // khởi tạo data product
        String[] proName = {"Capy ngầu", "Capy cool", "Capy tốt nghiệp", "Capy g chữ trên ", "Capy ngộ 0", "Capy nhân viên", "Capy chạy deadline"
                , "Capy 6 mũi", "Capy trộm nc", "Capy chef", "Người hầu", "MC Capy", "Capy deadline"};
        String[] proPrice = {"10.000.000k", "3000k", "300USD","1000k", "300k", "700k","1000k", "3070k", "300k","2000k", "3000k", "89USD", "666K"};
        String[] proImg = {"https://i.pinimg.com/736x/a8/d8/94/a8d894d33567cb0d0ae9108f004cc9c6.jpg"
                ,"https://i.pinimg.com/564x/40/10/f3/4010f31f2de3d89a28083f589253d555.jpg"
                ,"https://i.pinimg.com/736x/ca/34/01/ca34019485c3041131d4a0c0417a9b26.jpg"
                ,"https://i.pinimg.com/736x/0e/25/56/0e2556a840aa7bc5dbad602230126856.jpg"
                ,"https://i.pinimg.com/736x/82/29/01/822901fd7f4ba1f31ca4da57ea23170b.jpg"
                ,"https://i.pinimg.com/736x/eb/91/0f/eb910f72d18176bad9efeae7276be542.jpg"
                ,"https://i.pinimg.com/736x/ee/03/54/ee0354caeffff198a793a2e5aff5d0c9.jpg"
                ,"https://i.pinimg.com/736x/91/48/e2/9148e20a3ca1e7751c688a61389b42b5.jpg"
                ,"https://i.pinimg.com/736x/07/15/6c/07156c681344cc751637e14537e19782.jpg"
                ,"https://i.pinimg.com/736x/59/d5/15/59d5155480e835cd0f8001d89c6965c4.jpg"
                ,"https://i.pinimg.com/736x/de/5f/2c/de5f2c4afd7e4916b6c76b21ada61092.jpg"
                ,"https://i.pinimg.com/736x/08/6d/7e/086d7e81426d6d5872dac620e5021972.jpg"
                ,"https://i.pinimg.com/736x/5f/da/f5/5fdaf552b62936beee90bd5818f9902c.jpg"};
        GridAdapter gridAdapter = new GridAdapter(getActivity(), proName, proPrice,proImg);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), proName[i], Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}