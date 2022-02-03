package com.pulo.absensi;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pulo.absensi.Model.ModelDataGuru;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TambahSiswaActivty extends AppCompatActivity {
    EditText nama,stb;
    Spinner jkl,kelas;
    String sstb = "",snama = "",sjkl= "Jenis Kelamin",skelas= "Pilih Kelas";
    Button simpan;
    TextView info;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = TambahSiswaActivty.class.getSimpleName();
    private NfcAdapter mNfcAdapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String idcard = null;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_siswa);

        nama = findViewById(R.id.namalengkap);
        jkl = findViewById(R.id.spinner_jkl);
        kelas = findViewById(R.id.spinner_kelas);
        stb = findViewById(R.id.stb);

        progressDialog = new ProgressDialog(TambahSiswaActivty.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");
        initNFC();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        info =  findViewById(R.id.info);
       simpan =  findViewById(R.id.btn_sinpanData);
       simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snama = nama.getText().toString();
                sjkl = jkl.getSelectedItem().toString();
                skelas = kelas.getSelectedItem().toString();
                sstb = stb.getText().toString();

                if (snama.equals("") || sjkl.equals("Jenis Kelamin") || skelas.equals("Pilih Kelas") || sstb.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Data Belum Lengkap..\nSilahkan Lengkapi Data..!!",Toast.LENGTH_LONG).show();
                }else {
                    SaveData(sstb,snama,sjkl,skelas);
                }
            }
        });

    }
    @SuppressLint("CommitPrefEdits")
    private void shared(){

    }
    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
    @SuppressLint({"SetTextI18n", "MissingSuperCall"})
    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "onNewIntent: " + intent.getAction());
        if (tag != null) {
            Ndef ndef = Ndef.get(tag);
            if (snama.equals("") || sjkl.equals("Jenis Kelamin") || skelas.equals("Pilih Kelas") || sstb.equals("")) {
                Toast.makeText(getApplicationContext(), "Isi Data & Simpan Data Terlebih Dahulu..!!", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.show();
                try {
                    ndef.connect();
                    simpanDataCard(ndef, Objects.requireNonNull(idcard));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString() + "ini", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        }
    }
    private void simpanDataCard(Ndef ndef, String pesan) {
        if (ndef != null) {
                try {
                    NdefRecord mimeRecord = NdefRecord.createMime("text/plain", pesan.getBytes(Charset.forName("US-ASCII")));
                    ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                    Toast.makeText(getApplicationContext(), "Registrasi Kartu Succes!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), TambahSiswaActivty.class));
                } catch (IOException | FormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString() + "Registrasi Kartu Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    private void SaveData(String stb,String name, String jkl,String kelas)
    {
        progressDialog.show();
        Map<String,Object> userCard = new HashMap<>();
        userCard.put("stambuk",stb);
        userCard.put("nama",name);
        userCard.put("jenis kelamin",jkl);
        userCard.put("kelas",kelas);
        db.collection("Siswa")
                .add(userCard)
                .addOnSuccessListener(documentReference -> {
                    idcard = documentReference.getId();
                    Toast.makeText(getApplicationContext(), "Data Disimpan!", Toast.LENGTH_SHORT).show();
                   offButton();
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Data Gagal Disimpan!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }
    private void offButton()
    {
        nama.setVisibility(View.GONE);
        jkl.setVisibility(View.GONE);
        kelas.setVisibility(View.GONE);
        info.setVisibility(View.VISIBLE);
        simpan.setVisibility(View.GONE);
    }

}