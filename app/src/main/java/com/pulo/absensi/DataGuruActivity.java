package com.pulo.absensi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pulo.absensi.Adapter.AdapterDataGuru;
import com.pulo.absensi.Model.ModelDataGuru;

import java.util.ArrayList;
import java.util.List;

public class DataGuruActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference dbr ;
    private List<ModelDataGuru> list = new ArrayList<>();
    private AdapterDataGuru adapterDataGuru;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_guru);

        recyclerView  = findViewById(R.id.ryc_guru);
        progressDialog = new ProgressDialog(DataGuruActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");

        adapterDataGuru = new AdapterDataGuru(getApplicationContext(),list);
//        userAdapter.setDialog(pos -> {
//            final CharSequence[] dialogItem = {"Hapus"};
//            AlertDialog.Builder dialog = new AlertDialog.Builder(DataActivity.this);
//            dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    deleteData(list.get(pos).getId());
//
//                }
//            });
//            dialog.show();
//        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapterDataGuru);
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getData(){
        progressDialog.show();
        db.collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            ModelDataGuru guru = new ModelDataGuru(document.getString("nama"), document.getString("email"),document.getString("telepon"),document.getString("nip"));
                            guru.setIdguru(document.getId());
                            list.add(guru);
                        }
                        if (list.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Data Masih Kosong!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Data Tersedia!", Toast.LENGTH_SHORT).show();
                        }
                        adapterDataGuru.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }
}