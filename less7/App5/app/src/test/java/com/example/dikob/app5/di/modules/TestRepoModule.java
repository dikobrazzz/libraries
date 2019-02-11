package com.example.dikob.app5.di.modules;

import com.example.dikob.app5.mvp.model.repo.UserRepo;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;

@Module
public class TestRepoModule {

    @Provides
    public UserRepo userRepo(){
        return Mockito.mock(UserRepo.class);
    }
}
