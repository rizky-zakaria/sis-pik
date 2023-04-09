package com.example.sis_pikmobile.model;

import java.util.List;

public class ResponseVerifikasi {
    Integer success;
    String message;
    VerifikasiModel data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VerifikasiModel getData() {
        return data;
    }

    public void setData(VerifikasiModel data) {
        this.data = data;
    }
}
