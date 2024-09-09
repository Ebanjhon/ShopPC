package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleAccountFragment extends Fragment {

    EditText txtUserName, txtFirstname, txtLastname, txtEmail, txtPhone, txtAddress, txtBirthday, txtPassword;
    Spinner roleSpinner;
    Button btnSave, btnCancel;
    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role_account, container, false);
        txtFirstname = view.findViewById(R.id.editTextFirstName);
        txtLastname = view.findViewById(R.id.editTextLastname);
        txtUserName = view.findViewById(R.id.editTextUserName);
        txtEmail = view.findViewById(R.id.editTextUserEmail);
        txtPhone = view.findViewById(R.id.editTextPhone);
        txtAddress = view.findViewById(R.id.editTextAddress);
        txtBirthday = view.findViewById(R.id.editTextBirthday);
        txtPassword = view.findViewById(R.id.editTextPassword);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        btnSave = view.findViewById(R.id.saveButton);
        btnCancel = view.findViewById(R.id.cancelButton);

        // Sử dụng ArrayAdapter với mảng từ strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.roles_array, android.R.layout.simple_spinner_item);
        // Định dạng hiển thị cho mỗi mục trong Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("user");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
//                Toast.makeText(getActivity(),"Created new user successful",Toast.LENGTH_SHORT).show();
                clearFields();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new AcountFragment());
            }
        });
        return view;
    }
    private  void saveUser(){
        String firstname = txtFirstname.getText().toString().trim();
        String lastname = txtLastname.getText().toString().trim();
        String username = txtUserName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String birthday = txtBirthday.getText().toString().trim();
        String password = txtPassword.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        // Kiểm tra nếu độ dài số điện thoại không phải là 10 số
//        if (phone.length() != 10) {
//            Toast.makeText(getContext(), "Phone number must be exactly 10 digits", Toast.LENGTH_LONG).show();
//            return;
//        }
        if(TextUtils.isEmpty(firstname) ||
                TextUtils.isEmpty(firstname) ||
                TextUtils.isEmpty(lastname) ||
                TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(birthday) ||
                TextUtils.isEmpty(role) ||
                TextUtils.isEmpty(password))
        {
            Toast.makeText(getContext(),"Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    // Save additional user data to Firebase Database
                    String userID = user.getUid();
                    User newUser = new User(userID,username,role,firstname,lastname,email,phone,address,birthday);
                    userRef.child(userID).setValue(newUser)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getActivity(),"Created new user successful",Toast.LENGTH_SHORT).show();
                                clearFields();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(),"Failed to assign role ", Toast.LENGTH_LONG).show());
                }
            }
            else {
                Toast.makeText(getContext(),"Failed to create new user " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //    private  void clearFields()
//    {
//        txtFirstname.setText("");
//        txtLastname.setText("");
//        txtUserName.setText("");
//        txtEmail.setText("");
//        txtPhone.setText("");
//        txtAddress.setText("");
//        txtBirthday.setText("");
//        txtPassword.setText("");
//        roleSpinner.setSelection(0);
//    }
    private void clearFields() {
        try {
            txtFirstname.setText("");
            txtLastname.setText("");
            txtUserName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            txtBirthday.setText("");
            txtPassword.setText("");
            roleSpinner.setSelection(0);
            Log.d("ClearFields", "Fields cleared successfully");
        } catch (Exception e) {
            Log.d("ClearFields", "Error clearing fields: " + e.getMessage());
        }
    }

    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}