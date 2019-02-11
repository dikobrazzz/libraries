package com.example.dikob.app5.mvp.model.repo;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import com.example.dikob.app5.mvp.model.api.ApiService;
import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.ui.NetworkStatus;

import java.util.List;

public class UserRepo {
    private ICashe cashe;
    private ApiService api;

    public UserRepo(ICashe cashe, ApiService api) {
        this.cashe = cashe;
        this.api = api;
    }

    public Single<User> getUser(String username)
    {
        return api.getUser(username).subscribeOn(Schedulers.io());
    }

    public Single<List<Repository>> getUserRepos(User user)
    {
        return api.getUserRepos(user.getReposUrl()).subscribeOn(Schedulers.io());
    }

    public Single<User> getUserByCashe(String username) {
        if (NetworkStatus.isOnline()) {
            return api.getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> cashe.saveUser(user));
        } else {
            return cashe.loadUser(username);
        }
    }

    public Single<List<Repository>> getUserReposByCashe(User user) {
        if (NetworkStatus.isOnline()) {
            return api.getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(repos -> cashe.saveRepository(user, repos));
        } else {
            return cashe.loadRepository(user);
        }
    }
}
