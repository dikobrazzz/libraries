package com.example.dikob.app4.mvp.model.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

public class User {
    @Expose
    private String login;

    @Expose
    private String avatarUrl;

    @Expose
    private List<String> repos;

    public List<String> getRepos() {
        return repos;
    }

    public void setRepos(List<String> repos) {
        this.repos = repos;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
