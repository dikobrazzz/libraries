package com.example.dikob.app4.mvp.model.api;

import com.example.dikob.app4.mvp.model.entity.User;
import com.example.dikob.app4.mvp.model.repo.RepoRequest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDataSource {
    @GET("/users/{user}")
    Single<User> getUser(@Path("user") String username);

    @GET("/users/{user}/repos")
    Observable<List<RepoRequest>> getRepos (@Path("user") String username);
}
