package com.example.dikob.app4.mvp.model.repo;

import com.example.dikob.app4.mvp.model.api.ApiHolder;
import com.example.dikob.app4.mvp.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UsersRepo {

    public Single<User> getUser(String username){
        return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io());
    }

    public Observable<List<RepoRequest>> getUsersRepos(String username) {
        return ApiHolder.getApi().getRepos(username).subscribeOn(Schedulers.io());
    }
}
