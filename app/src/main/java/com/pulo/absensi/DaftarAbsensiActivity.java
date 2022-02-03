package com.pulo.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pulo.absensi.Model.ModelDataGuru;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DaftarAbsensiActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = DaftarAbsensiActivity.class.getSimpleName();
    private NfcAdapter mNfcAdapter;
    ProgressDialog progressDialog ;
    String idcard;
    TextView header;
    Calendar c1 = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_absensi);

        progressDialog = new ProgressDialog(DaftarAbsensiActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");
        initNFC();
        header = findViewById(R.id.head_absen);


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
        Log.d(TAG, "onNewIntent: "+intent.getAction());
        if(tag != null) {
            Ndef ndef = Ndef.get(tag);
            progressDialog.show();
            try {
                ndef.connect();
                baca_data(ndef);
                getData();
//                simpanDataCard(ndef,  Objects.requireNonNull(idcard));
            } catch (Exception e) {
                header.setText(e.toString());
                Toast.makeText(getApplicationContext(), e.toString() + "ini", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        }
    }
    private void baca_data(Ndef ndef) throws IOException, FormatException {
        NdefMessage ndefMessage = ndef.getNdefMessage();
        idcard = new String(ndefMessage.getRecords()[0].getPayload());
        Log.d(TAG, "readFromNFC: " + idcard);
//        getData();
    }
    private void getData() {
        progressDialog.show();
        db.collection("Siswa").document(idcard)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (Objects.requireNonNull(document).exists() && Objects.equals(document.getString("kelas"), FormAbsensiActivity.Pkelas)) {

                            String waktu = sdf1.format(c1.getTime());
                            String nama = document.getString("nama");
                            String kelas = document.getString("kelas");
                            String matpel = FormAbsensiActivity.Pmatpel;
                            SaveData(nama,kelas,matpel,waktu);
                            header.setText("Succes..");
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                            header.setText("Kelas Tidak Cocok..");

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }
    private void SaveData(String name,String kelas, String matpel, String tgl)
    {
        progressDialog.show();
        Map<String,Object> absen = new HashMap<>();
        absen.put("nama",name);
        absen.put("kelas",kelas);
        absen.put("mata pelajaran",matpel);
        absen.put("keterangan","Hadir");
        absen.put("tanggal",tgl);
        db.collection("Absensi")
                .add(absen)
                .addOnSuccessListener(documentReference -> {
                    idcard = documentReference.getId();
                    Toast.makeText(getApplicationContext(), "Absen Succes..!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Data Gagal Disinpan!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

}