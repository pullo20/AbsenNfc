package com.pulo.absensi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText nama,pass,email,tlp,nip;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    String snama,spass,semail,stlp,snip;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan data...");;
//        if (mAuth.getCurrentUser() != null)
//        {
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//            finish();
//        }
        nama = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        tlp = findViewById(R.id.tlp);
        nip = findViewById(R.id.nip);


        findViewById(R.id.btn_daftar).setOnClickListener(v -> {
            snama = nama.getText().toString();
            spass = pass.getText().toString();
            semail = email.getText().toString();
            stlp = tlp.getText().toString();
            snip = nip.getText().toString();
            if (snama.equals("") || spass.equals("") || semail.equals("") || stlp.equals("") || snip.equals("") )
            {
                Toast.makeText(getApplicationContext(), "Data Tidak Lengkap..!! \n Silahkan Lengakapi Data Anda", Toast.LENGTH_SHORT).show();
            }else
            {
                daftarakun();
            }
        });

    }
    private void daftarakun()
    {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(semail,spass).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                userId = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("Users").document(userId);
                Map<String,Object> user = new HashMap<>();
                user.put("nip",snip);
                user.put("nama",snama);
                user.put("email",semail);
                user.put("password",spass);
                user.put("telepon",stlp);
                user.put("status","guru");
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Berhasil..\nSilahkan Login..!", Toast.LENGTH_SHORT).show();
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: "+userId);

                    }
                });

            }else {
                Toast.makeText(getApplicationContext(), "Data gagal ditambah!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}