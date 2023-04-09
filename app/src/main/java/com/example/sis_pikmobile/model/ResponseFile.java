package com.example.sis_pikmobile.model;

public class ResponseFile {
    String success, message;
    FileModel data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FileModel getData() {
        return data;
    }

    public void setData(FileModel data) {
        this.data = data;
    }
}
