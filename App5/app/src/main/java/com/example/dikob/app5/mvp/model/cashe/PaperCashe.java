package com.example.dikob.app5.mvp.model.cashe;

import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Single;

public class PaperCashe implements ICashe {
    @Override
    public User saveUser(User user) {
        Paper.book("users").write(user.getLogin(), user);
        return user;
    }

    @Override
    public List<Repository> saveRepository(User user, List<Repository> repos) {
        Paper.book("repos").write(user.getLogin(), repos);
        return repos;
    }

    @Override
    public Single<User> loadUser(String username) {
        if(!Paper.book("users").contains(username)){
            return Single.error(new RuntimeException("No such user in cache"));
        }
        return Single.fromCallable(() -> Paper.book("users").read(username));
    }

    @Override
    public Single<List<Repository>> loadRepository(User user) {
        if(!Paper.book("repos").contains(user.getLogin())){
            return Single.error(new RuntimeException("No repos for such user in cache"));
        }
        return Single.fromCallable(() -> Paper.book("repos").read(user.getLogin()));
    }
}
