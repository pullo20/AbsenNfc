package com.pulo.absensi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pulo.absensi.Adapter.AdapterDataGuru;
import com.pulo.absensi.Adapter.AdapterDataSiswa;
import com.pulo.absensi.Model.ModelDataGuru;
import com.pulo.absensi.Model.ModelDataSiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataSiswaActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference dbr ;
    private final List<ModelDataSiswa> list = new ArrayList<>();
    private final AdapterDataSiswa adapterDataSiswa = new AdapterDataSiswa(DataSiswaActivity.this,list);
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_siswa);

        recyclerView  = findViewById(R.id.ryc_siswa);
        progressDialog = new ProgressDialog(DataSiswaActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapterDataSiswa);
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getData(){
        progressDialog.show();
        db.collection("Siswa")
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                            ModelDataSiswa siswa = new ModelDataSiswa(document.getString("nama"),document.getString("jenis kelamin"),document.getString("kelas"),document.getString("stambuk"));
                            siswa.setIdsiswa(document.getId());
                            list.add(siswa);
                        }
                        if (list.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Data Masih Kosong!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Data Tersedia!", Toast.LENGTH_SHORT).show();
                        }
                        adapterDataSiswa.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }
}