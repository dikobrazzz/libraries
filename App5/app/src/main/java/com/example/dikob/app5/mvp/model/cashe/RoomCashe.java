package com.example.dikob.app5.mvp.model.cashe;

import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.entity.room.RoomRepository;
import com.example.dikob.app5.mvp.model.entity.room.RoomUser;
import com.example.dikob.app5.mvp.model.entity.room.db.UserDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RoomCashe implements ICashe {
    @Override
    public User saveUser(User user) {
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                .findByLogin(user.getLogin());

        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
        }

        roomUser.setAvatarUrl(user.getAvatarUrl());
        roomUser.setReposUrl(user.getReposUrl());

        UserDatabase.getInstance().getUserDao()
                .insert(roomUser);

        return user;
    }

    @Override
    public List<Repository> saveRepository(User user, List<Repository> repos) {
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                .findByLogin(user.getLogin());

        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
            roomUser.setAvatarUrl(user.getAvatarUrl());
            roomUser.setReposUrl(user.getReposUrl());
            UserDatabase.getInstance()
                    .getUserDao()
                    .insert(roomUser);
        }


        if (!repos.isEmpty()) {
            List<RoomRepository> roomRepositories = new ArrayList<>();
            for (Repository repository : repos) {
                RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                roomRepositories.add(roomRepository);
            }

            UserDatabase.getInstance()
                    .getRepositoryDao()
                    .insert(roomRepositories);
        }

        return repos;
    }

    @Override
    public Single<User> loadUser(String username) {
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                    .findByLogin(username);
            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Single<List<Repository>> loadRepository(User user) {
        return Single.create(emitter -> {
                RoomUser roomUser = UserDatabase.getInstance()
                        .getUserDao()
                        .findByLogin(user.getLogin());

                if(roomUser == null){
                    emitter.onError(new RuntimeException("No such user in cache"));
                } else {
                    List<RoomRepository> roomRepositories = UserDatabase.getInstance().getRepositoryDao()
                            .getAll();

                    List<Repository> repos = new ArrayList<>();
                    for (RoomRepository roomRepository: roomRepositories){
                        repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                    }

                    emitter.onSuccess(repos);
                }
            }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>)(Class)List.class);
    }
}
