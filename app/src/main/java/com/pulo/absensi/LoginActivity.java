package com.pulo.absensi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText email,pass;
    String id,semail,spass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//                if (firebaseAuth.getCurrentUser() != null)
//        {
//            startActivity(new Intent(getApplicationContext(),DasboardActivity.class));
//            finish();
//        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Validasi data...");
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semail = email.getText().toString();
                spass = pass.getText().toString();
                if (semail.equals("") || spass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Data Masih Kosong..!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    LoginCek(semail,spass);
                }
            }
        });
        findViewById(R.id.btn_daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
    private void LoginCek(String email, String pass) {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        id = firebaseAuth.getCurrentUser().getUid();
                        DocumentReference docRef = db.collection("Users").document(id);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.getString("status").equals("admin")) {
                                        startActivity(new Intent(getApplicationContext(), DasboardActivity.class));
                                        finish();
                                        Toast.makeText(getApplicationContext(), "Login Sebagai Admin", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), FormAbsensiActivity.class));
                                        finish();
                                        Toast.makeText(getApplicationContext(), "Login Sebagai Guru", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
//                                        Log.d(TAG, "No such document");
                                    }
                                    progressDialog.dismiss();
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed.!!\nEmail Dan Password Tidak Di Temukan", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}