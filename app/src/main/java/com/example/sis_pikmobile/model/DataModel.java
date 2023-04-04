package com.example.sis_pikmobile.model;

public class DataModel {
    String kegiatan, lokasi, tgl_mulai, tgl_selesai, waktu, kk, ktp, sp, status, penanggung_jawab;
    Integer id, pemohon_id, petugas_id;

    public String getKegiatan() {
        return kegiatan;
    }

    public String getPenanggung_jawab() {
        return penanggung_jawab;
    }

    public void setPenanggung_jawab(String penanggung_jawab) {
        this.penanggung_jawab = penanggung_jawab;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTgl_mulai() {
        return tgl_mulai;
    }

    public void setTgl_mulai(String tgl_mulai) {
        this.tgl_mulai = tgl_mulai;
    }

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public void setTgl_selesai(String tgl_selesai) {
        this.tgl_selesai = tgl_selesai;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getKk() {
        return kk;
    }

    public void setKk(String kk) {
        this.kk = kk;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPemohon_id() {
        return pemohon_id;
    }

    public void setPemohon_id(Integer pemohon_id) {
        this.pemohon_id = pemohon_id;
    }

    public Integer getPetugas_id() {
        return petugas_id;
    }

    public void setPetugas_id(Integer petugas_id) {
        this.petugas_id = petugas_id;
    }
}
