package com.pulo.absensi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pulo.absensi.Adapter.AdapterDataAbsen;
import com.pulo.absensi.Adapter.AdapterDataGuru;
import com.pulo.absensi.Model.ModelDataAbsen;
import com.pulo.absensi.Model.ModelDataGuru;

import java.util.ArrayList;
import java.util.List;

public class DataAbsenActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference dbr ;
    private List<ModelDataAbsen> list = new ArrayList<>();
    private AdapterDataAbsen adapterDataAbsen;
    private ProgressDialog progressDialog;
    TextView stts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_absen);

        recyclerView  = findViewById(R.id.ryc_tabel_absen);
        stts = findViewById(R.id.status);
        progressDialog = new ProgressDialog(DataAbsenActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");

        adapterDataAbsen = new AdapterDataAbsen(getApplicationContext(),list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapterDataAbsen);
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getData(){
        list.clear();
        progressDialog.show();
        db.collection("Absensi")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            ModelDataAbsen absen = new ModelDataAbsen(document.getString("nama"), document.getString("kelas"),document.getString("mata pelajaran"),document.getString("keterangan"),document.getString("tanggal"));
                            absen.setIdabsen(document.getId());
                            if (FilterDataActivity.Pkls.equals(absen.getKelas()) && FilterDataActivity.Pmatpel.equals(absen.getMatpel()) && FilterDataActivity.Ptgl.equals(absen.getTgl()))
                            {
                                list.add(absen);
                            }
                        }
                        if (list.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.GONE);
                            stts.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(getApplicationContext(), "Data berhasil di ambil!", Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.VISIBLE);
                            stts.setVisibility(View.GONE);
                        }
                        adapterDataAbsen.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }
}