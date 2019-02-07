package com.example.dikob.app5.di.modules;

import com.example.dikob.app5.mvp.model.api.ApiService;
import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.repo.UserRepo;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {

    @Singleton
    @Provides
    public UserRepo userRepo (@Named("room") ICashe cashe, ApiService api){
        return new UserRepo(cashe, api);
    }
}
