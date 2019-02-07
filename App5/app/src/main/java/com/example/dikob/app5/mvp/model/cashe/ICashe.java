package com.example.dikob.app5.mvp.model.cashe;

import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public interface ICashe {

    User saveUser(User user);
    List<Repository> saveRepository(User user, List<Repository> repos);

    Single<User> loadUser(String username);
    Single<List<Repository>> loadRepository(User user);
}
