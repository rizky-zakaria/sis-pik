package com.example.sis_pikmobile.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {

     @SerializedName("id")
     private String id;

     @SerializedName("email")
     private String email;

     @SerializedName("username")
     private String username;

     @SerializedName("password")
     private String password;

     @SerializedName("role")
     private String role;

     @SerializedName("name")
     private String name;

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public String getRole() {
          return role;
     }

     public void setRole(String role) {
          this.role = role;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }
}
