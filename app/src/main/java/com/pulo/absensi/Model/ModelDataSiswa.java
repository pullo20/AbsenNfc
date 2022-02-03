package com.pulo.absensi.Model;

public class ModelDataSiswa {
    private String idsiswa, nama , jkl,kelas,stb;

    public ModelDataSiswa(){

    }
    public ModelDataSiswa(String nama,String jkl,String kelas,String stb)
    {
        this.nama = nama;
        this.jkl = jkl;
        this.kelas = kelas;
        this.stb = stb;
    }

    public String getIdsiswa() {
        return idsiswa;
    }

    public void setIdsiswa(String idsiswa) {
        this.idsiswa = idsiswa;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJkl() {
        return jkl;
    }

    public void setJkl(String jkl) {
        this.jkl = jkl;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getStb() {
        return stb;
    }

    public void setStb(String stb) {
        this.stb = stb;
    }
}
