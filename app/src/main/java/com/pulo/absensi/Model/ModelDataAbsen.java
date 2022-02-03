package com.pulo.absensi.Model;

public class ModelDataAbsen {

    private String idabsen, ket, matpel , nama,kelas,tgl;

    public ModelDataAbsen(){

    }
    public ModelDataAbsen(String nama,String kelas,String matpel,String ket,String tgl)
    {
        this.nama = nama;
        this.matpel = matpel;
        this.kelas = kelas;
        this.ket = ket;
        this.tgl = tgl;
    }

    public String getIdabsen() {
        return idabsen;
    }

    public void setIdabsen(String idabsen) {
        this.idabsen = idabsen;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getMatpel() {
        return matpel;
    }

    public void setMatpel(String matpel) {
        this.matpel = matpel;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
}

