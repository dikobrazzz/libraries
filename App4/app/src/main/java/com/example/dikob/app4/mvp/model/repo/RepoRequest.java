package com.example.dikob.app4.mvp.model.repo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepoRequest {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
}
