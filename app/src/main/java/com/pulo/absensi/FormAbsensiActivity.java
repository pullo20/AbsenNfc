package com.pulo.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class FormAbsensiActivity extends AppCompatActivity {
    public static String Pnama,Pmatpel,Pkelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_absensi);

        EditText nama = findViewById(R.id.nama_guru);
        Spinner matpel = findViewById(R.id.matpel);
        Spinner spinner_kelas = findViewById(R.id.spinner_absenkelas);
        findViewById(R.id.btn_absen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pnama = nama.getText().toString();
                Pmatpel = matpel.getSelectedItem().toString();
                Pkelas = spinner_kelas.getSelectedItem().toString();
                startActivity(new Intent(getApplicationContext(), DaftarAbsensiActivity.class));
            }
        });
    }
}