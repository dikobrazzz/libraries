package com.example.dikob.app5.di;

import com.example.dikob.app5.di.modules.TestCiceroneModule;
import com.example.dikob.app5.di.modules.TestRepoModule;
import com.example.dikob.app5.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        TestRepoModule.class,
        TestCiceroneModule.class})
public interface TestComponent {
    void inject(MainPresenter presenter);
}
