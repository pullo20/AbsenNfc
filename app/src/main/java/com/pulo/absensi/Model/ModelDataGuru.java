package com.pulo.absensi.Model;

public class ModelDataGuru {
    private String idguru, nama, email , telepon,nip;

    public ModelDataGuru(){

    }
    public ModelDataGuru(String nama,String email,String tlp,String nip)
    {
        this.nama = nama;
        this.email = email;
        this.telepon = tlp;
        this.nip = nip;
    }

    public String getIdguru() {
        return idguru;
    }

    public void setIdguru(String idguru) {
        this.idguru = idguru;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
}
