package com.example.mobileshopapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;


public class FilterFragment extends Fragment {
    FlexboxLayout flexbox;
    Button btnFilter;
    RadioGroup radioGrp;
    FrameLayout frameLayout;
    private int selectedIndex = -1;
    final int[] index = new int[1];
    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        // ánh xạ
        flexbox = view.findViewById(R.id.flexbox);
        btnFilter = view.findViewById(R.id.btnfiler);
        frameLayout = view.findViewById(R.id.frameLayoutPrice);
        // khởi tạo dữ liệu category
        String[] cate = {"all", "Màn hình", "bàn phím", "chuột", "ổ cứng", "Quạt tản nhiệt"};
        final RadioButton[] checkedRadioButton = {null};

        // Duyệt qua danh sách các tùy chọn và tạo các `RadioButton`
        for (int i = 0; i < cate.length; i++) {
            String option = cate[i];

            // Tạo `RadioButton`
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(option);
            radioButton.setTextSize(17);
            radioButton.setButtonDrawable(null); // Loại bỏ biểu tượng radio mặc định
            radioButton.setTextColor(getResources().getColorStateList(R.color.radio_button_text_color));
            radioButton.setBackgroundResource(R.drawable.radio_button); // Áp dụng background tùy chỉnh
            radioButton.setPadding(20, 16, 20, 16); // Thêm padding

            // Tạo LayoutParams và thêm margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // Thiết lập margin
            params.setMargins(10, 8, 10, 8); // Thay đổi giá trị margin theo nhu cầu của bạn

            // Áp dụng LayoutParams cho RadioButton
            radioButton.setLayoutParams(params);

            // Lưu trữ index dưới dạng tag
            radioButton.setTag(i);

            // Xử lý sự kiện `onClick` cho từng `RadioButton`
            radioButton.setOnClickListener(v -> {
                if (checkedRadioButton[0] != null && checkedRadioButton[0] != radioButton) {
                    checkedRadioButton[0].setChecked(false); // Bỏ chọn `RadioButton` trước đó
                }
                checkedRadioButton[0] = radioButton; // Cập nhật `RadioButton` đã chọn hiện tại

                // Lấy index của `RadioButton` đã chọn
                selectedIndex = (int) radioButton.getTag();
            });
            // Thêm `RadioButton` vào `FlexboxLayout`
            flexbox.addView(radioButton);
        }

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedIndex != -1) {
                    Toast.makeText(getActivity(), "Selected index: " + selectedIndex, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // xử lý xử kiệt nhấn nút chọn giá
        radioGrp = view.findViewById(R.id.groupRadio);
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioAll)
                {
                    frameLayout.setVisibility(View.GONE);
                }else{
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
}