package com.example.dikob.app5.di;


import com.example.dikob.app5.CasheInstrumentedTest;
import com.example.dikob.app5.UserRepoInstrumentedTest;
import com.example.dikob.app5.di.modules.CasheModule;
import com.example.dikob.app5.di.modules.RepoModule;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {
        RepoModule.class,
        CasheModule.class
})
public interface TestInstrumentalComponent {
    void inject(UserRepoInstrumentedTest test);
    void inject(CasheInstrumentedTest test);
}
