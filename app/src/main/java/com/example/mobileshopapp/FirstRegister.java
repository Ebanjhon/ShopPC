package com.example.mobileshopapp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstRegister extends Fragment {
    private TextView txtBtnContinute, birth, txtBtnPicker;
    private EditText firstname, lastname, address, email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_first_register, container, false);
        // ánh xạ
        firstname = view.findViewById(R.id.firstName);
        lastname = view.findViewById(R.id.lastName);
        address = view.findViewById(R.id.address);
        birth = view.findViewById(R.id.birthdate);
        email = view.findViewById(R.id.email);
        txtBtnContinute = view.findViewById(R.id.txtToSecondFM);
        txtBtnPicker = view.findViewById(R.id.btnShowCalender);
        // ham click
        txtBtnContinute.setOnClickListener(v -> {
            // thực hiện check dữ liệu đầu vào
            String fname, lname,adrss, eml, date;
            fname = firstname.getText().toString();
            lname = lastname.getText().toString();
            adrss = address.getText().toString();
            date = birth.getText().toString();
            eml = email.getText().toString();

            if (eml.isEmpty()||fname.isEmpty()||lname.isEmpty()||adrss.isEmpty()){
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (date == "00/00/0000"){
                Toast.makeText(getActivity(), "Hãy chọn ngày sinh lại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo Bundle và thêm dữ liệu
            Bundle bundle = new Bundle();
            bundle.putString("firstname", fname);
            bundle.putString("lastname", lname);
            bundle.putString("address", adrss);
            bundle.putString("email", eml);
            bundle.putString("birthday", date);

            // Tìm Fragment đã tồn tại hoặc tạo mới nếu chưa tồn tại
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Fragment secondFragment = getParentFragmentManager().findFragmentByTag("SecondFragmentTag");
            if (secondFragment == null) {
                secondFragment = new SecondRegister();
            }
            // Đặt Bundle vào Fragment
            secondFragment.setArguments(bundle);
            // Thay thế Fragment hiện tại bằng SecondFragment
            transaction.replace(R.id.fragment_container, secondFragment, "SecondFragmentTag");
            // Thêm giao dịch vào Back Stack nếu cần
            transaction.addToBackStack(null);
            // Kết thúc giao dịch
            transaction.commit();
        });

        // hàm gọi datepicker
        txtBtnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        return view;
    }

    // gọi hàm show dialog
    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birth.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        }, 2024, 0, 0);
        dialog.show();
    }
}