package com.example.mobileshopapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminEmployeeMain extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseHelper dbHelper = new DatabaseHelper(AdminEmployeeMain.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_employee_main);
        mAuth = FirebaseAuth.getInstance();
        User user= dbHelper.getUserById(mAuth.getUid());
        // thêm điều hướng
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);

        // Nạp menu từ file XML
        if(user.getRole().equals("admin"))
        {
            bottomNavView.inflateMenu(R.menu.bottom_nav_menu_admin);
        }
        else{
            bottomNavView.inflateMenu(R.menu.bottom_nav_menu_employee);
        }

        // nghe sự kiện nhấn vào botton nav
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(R.id.Home == itemId) {
                    chuyenDoiManHinh(new HomeFragment());
                }else if (R.id.Product == itemId) {
//                    chuyenDoiManHinh(new FilterFragment());
                }else if (R.id.Statistic == itemId) {
//                    chuyenDoiManHinh(new FilterFragment());
                }else if (R.id.Order == itemId) {
//                    chuyenDoiManHinh(new CartFragment());
                }else if (R.id.Profile == itemId) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser == null){
                        chuyenDoiManHinh(new NotSignInFragment());
                    }else{
                        chuyenDoiManHinh(new ProfileFragment());
                    }
                }
                return true;
            }
        });
    }

    // hàm chuyển fragment
    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}