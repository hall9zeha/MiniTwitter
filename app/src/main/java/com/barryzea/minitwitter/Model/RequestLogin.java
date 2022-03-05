package com.barryzea.minitwitter.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLogin {

    @SerializedName("email")
    @Expose
    private String email;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public RequestLogin(){

    }

    @SerializedName("password")
    @Expose

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
