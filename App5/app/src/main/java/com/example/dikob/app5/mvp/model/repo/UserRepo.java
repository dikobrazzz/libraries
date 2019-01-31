package com.example.dikob.app5.mvp.model.repo;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import com.example.dikob.app5.mvp.model.api.ApiHolder;
import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.entity.room.RoomUser;
import com.example.dikob.app5.mvp.model.entity.room.db.UserDatabase;
import com.example.dikob.app5.ui.NetworkStatus;

import java.util.List;

public class UserRepo
{
    public Single<User> getUser(String username)
    {
        return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io());
    }

    public Single<List<Repository>> getUserRepos(User user)
    {
        return ApiHolder.getApi().getUserRepos(user.getReposUrl()).subscribeOn(Schedulers.io());
    }

    public Single<User> getUserByCashe(String username, ICashe cashe) {
        if (NetworkStatus.isOnline()) {
            return ApiHolder.getApi().getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> cashe.saveUser(user));
        } else {
            return cashe.loadUser(username);
        }
    }

    public Single<List<Repository>> getUserReposByCashe(User user, ICashe cashe) {
        if (NetworkStatus.isOnline()) {
            return ApiHolder.getApi().getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(repos -> cashe.saveRepository(user, repos));
        } else {
            return cashe.loadRepository(user);
        }
    }
}
