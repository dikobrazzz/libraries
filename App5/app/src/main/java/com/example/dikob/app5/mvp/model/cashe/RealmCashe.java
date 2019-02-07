package com.example.dikob.app5.mvp.model.cashe;

import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.entity.realm.RealmRepository;
import com.example.dikob.app5.mvp.model.entity.realm.RealmUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class RealmCashe implements ICashe {
    @Override
    public User saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser == null) {
            realm.executeTransaction(innerRealm -> {
                RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
                newRealmUser.setReposUrl(user.getReposUrl());
            });
        } else {
            realm.executeTransaction(innerRealm -> {
                realmUser.setAvatarUrl(user.getAvatarUrl());
                realmUser.setReposUrl(user.getReposUrl());
            });
        }
        realm.close();
        return user;
    }

    @Override
    public List<Repository> saveRepository(User user, List<Repository> repos) {
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

        if (realmUser == null) {
            realm.executeTransaction(innerRealm -> {
                RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
                newRealmUser.setReposUrl(user.getReposUrl());
            });
        }

        realm.executeTransaction(innerRealm -> {
            realmUser.getRepos().deleteAllFromRealm();
            for (Repository repository : repos) {
                RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                realmRepository.setName(repository.getName());
                realmUser.getRepos().add(realmRepository);
            }
        });
        realm.close();
        return repos;
    }

    @Override
    public Single<User> loadUser(String username) {
        return Single.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(realmUser.getLogin(), realmUser.getAvatarUrl(), realmUser.getReposUrl()));
            }
            realm.close();
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Single<List<Repository>> loadRepository(User user) {
        return Single.create(emitter -> {

            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                List<Repository> repos = new ArrayList<>();
                for (RealmRepository realmRepository : realmUser.getRepos()) {
                    repos.add(new Repository(realmRepository.getId(), realmRepository.getName()));
                }
                emitter.onSuccess(repos);
            }
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
    }
}
