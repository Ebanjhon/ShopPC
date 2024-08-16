package com.example.mobileshopapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileDetailFragment extends Fragment {
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        // Khởi tạo DatabaseHelper với context của Activity chứa Fragment
        dbHelper = new DatabaseHelper(requireContext());
        btnLogout = view.findViewById(R.id.btnlgout);
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                dbHelper.deleteTable();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
//                chuyenDoiManHinh(new NotSignInFragment());
            }
        });
        // lấy user
        User user= dbHelper.getUser();
        TextView username, address, role, birthdate, phone, email;
        username = view.findViewById(R.id.username);
        role = view.findViewById(R.id.role);
        birthdate = view.findViewById(R.id.birth);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);
//
        username.setText(user.getUsername());
        role.setText(user.getRole());
        birthdate.setText(user.getBirthDate());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());

        return view;
    }

    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}