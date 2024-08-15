package com.example.mobileshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAdminEmployee extends AppCompatActivity {
    TextView btnBack;
    Button btnLogin;
    EditText eml, pass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginAdminEmployee.this, AdminEmployeeMain.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_admin_employee);
        mAuth = FirebaseAuth.getInstance();
        btnBack = findViewById(R.id.txtback);
        btnLogin = findViewById(R.id.btnLogin);
        eml = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passwordLogin);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // xử lý đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = eml.getText().toString();
                password = pass.getText().toString();
                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginAdminEmployee.this, "Vui Lòng nhập đầy đủ thông tin!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    btnLogin.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        //check role
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DocumentReference docRef = db.collection("Users").document(user.getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()&&!(document.getString("role").equals("client"))) {
                                                        DatabaseHelper dbHelper = new DatabaseHelper(LoginAdminEmployee.this);
                                                        // tạo đối tượng user
                                                        User user = new User(mAuth.getUid(), document.getString("username")
                                                                , document.getString("role"), document.getString("firstname")
                                                                ,document.getString("lastname"), email
                                                                ,document.getString("phone"), document.getString("avatar")
                                                                ,document.getString("address"), document.getString("birthday"));
                                                        // lưu dữ liệu váo sql
                                                        dbHelper.addUser(user);
                                                        Toast.makeText(LoginAdminEmployee.this, "Đăng nhập thành công!",
                                                                Toast.LENGTH_SHORT).show();
                                                        // chuyển trang
                                                        Intent intent = new Intent(LoginAdminEmployee.this, AdminEmployeeMain.class);
                                                        startActivity(intent);
                                                    } else {
                                                        mAuth.signOut();
                                                        // nếu đăng nhập bị lỗi
                                                        Toast.makeText(LoginAdminEmployee.this, "Không tìm thấy tài khoản!",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginAdminEmployee.this, "Mật khẩu hoặc Email không đúng!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}