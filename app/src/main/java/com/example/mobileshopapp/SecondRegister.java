package com.example.mobileshopapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SecondRegister extends Fragment {
    ProgressBar progressBar;
    private TextView txtBack;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private EditText username,phone, pass, passcheck;
    private DatabaseReference mDatabase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_second_register, container, false);
        txtBack = view.findViewById(R.id.txtBackFM);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        pass = view.findViewById(R.id.pass);
        passcheck = view.findViewById(R.id.passAgain);
        btnRegister = view.findViewById(R.id.btnRegister);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressBar = view.findViewById(R.id.progressBarRegister);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // hàm gọi đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiếm tra đầu vào
                String urname, password, passwordCheck, numPhone;
                urname = username.getText().toString();
                numPhone = phone.getText().toString();
                password = pass.getText().toString();
                passwordCheck = passcheck.getText().toString();
                if(urname.isEmpty()||password.isEmpty()||passwordCheck.isEmpty()||numPhone.isEmpty())
                {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(passwordCheck))
                {
                    Toast.makeText(getActivity(), "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // lấy dữ liệu được truyền vào
                Bundle data = getArguments();
                if (data != null) {
                    String firstname = data.getString("firstname");
                    String lastname = data.getString("lastname");
                    String address = data.getString("address");
                    String email = data.getString("email");
                    String birthday = data.getString("birthday");

                    // gọi hàm đăng ký ng dùng
                    progressBar.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.GONE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    btnRegister.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        createDataUser(uid, firstname, lastname, address, birthday, numPhone, urname);
                                        Toast.makeText(getActivity(), "Đăng ký Thành công", Toast.LENGTH_SHORT).show();
                                        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                                        // tạo đối tượng user
                                        User u = new User(mAuth.getUid(), urname
                                                ,"client", firstname
                                                ,lastname, email
                                                ,numPhone, ""
                                                ,address, birthday);
                                        // lưu dữ liệu váo sql
                                        dbHelper.addUser(u);
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        // hàm gọi lại Fragment firstRegister
        txtBack.setOnClickListener(v->{
            // Tìm Fragment đã tồn tại hoặc tạo mới nếu chưa tồn tại
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Fragment firstFragment = getParentFragmentManager().findFragmentByTag("FirstFragmentTag");

            if (firstFragment == null) {
                firstFragment = new FirstRegister();
            }

            // Thay thế Fragment hiện tại bằng firstFragment
            transaction.replace(R.id.fragment_container, firstFragment, "FirstFragmentTag");

            // Thêm giao dịch vào Back Stack nếu cần
            transaction.addToBackStack(null);

            // Kết thúc giao dịch
            transaction.commit();
        });
        return view;
    }

    // gọi hàm thêm dữ liệu user vào database
    private void createDataUser(String uid, String firstname, String lastname, String address, String birthday, String phone, String username) {
        // Tạo một đối tượng Map để lưu dữ liệu người dùng
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("role", "client");
        user.put("lastname", lastname);
        user.put("firstname",firstname);
        user.put("phone", phone);
        user.put("address", address);
        user.put("birthday", birthday);

        // Sử dụng set() với document ID tùy chỉnh (ở đây là uid)
        db.collection("Users")
                .document(uid)  // Đặt document ID theo uid
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Nếu thành công
                })
                .addOnFailureListener(e -> {
                    // Nếu thất bại
                });
    }

}