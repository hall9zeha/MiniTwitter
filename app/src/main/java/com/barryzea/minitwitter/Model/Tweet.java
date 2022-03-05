package com.barryzea.minitwitter.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Tweet {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("likes")
    @Expose
    private ArrayList<Likes>likes;
    @SerializedName("user")
    @Expose
    private User user;

    public Tweet(Long id, String mensaje, ArrayList<Likes> likes, User user) {
        this.id = id;
        this.mensaje = mensaje;
        this.likes = likes;
        this.user = user;
    }

    public Tweet() {
    }
    public Tweet(Tweet newTweet){
        this.id = newTweet.id;
        this.mensaje = newTweet.mensaje;
        this.likes = newTweet.likes;
        this.user = newTweet.user;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
