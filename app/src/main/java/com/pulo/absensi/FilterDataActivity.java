package com.pulo.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterDataActivity extends AppCompatActivity {
    TextView tgl;
    Spinner kelas,matpel;
    public static String Pmatpel,Pkls,Ptgl;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_data);

        matpel = findViewById(R.id.filter_matpel);
        kelas = findViewById(R.id.filter_kelas);
        tgl  = findViewById(R.id.filter_tgl);
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        findViewById(R.id.btn_lihat_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pmatpel = matpel.getSelectedItem().toString();
                Pkls = kelas.getSelectedItem().toString();
                Ptgl = tgl.getText().toString();
                startActivity(new Intent(getApplicationContext(),DataAbsenActivity.class));
                finish();
            }
        });


    }
    private void showDateDialog() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(FilterDataActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy");

                tgl.setText(sdf1.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}