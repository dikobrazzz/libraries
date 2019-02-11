package com.example.dikob.app5.di;

import com.example.dikob.app5.di.modules.ApiModule;
import com.example.dikob.app5.di.modules.AppModule;
import com.example.dikob.app5.di.modules.CasheModule;
import com.example.dikob.app5.di.modules.CiceroneModule;
import com.example.dikob.app5.di.modules.ImageLoaderModule;
import com.example.dikob.app5.di.modules.RepoModule;
import com.example.dikob.app5.mvp.presenter.MainPresenter;
import com.example.dikob.app5.ui.activity.MainActivity;
import com.example.dikob.app5.ui.fragment.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        ImageLoaderModule.class,
        RepoModule.class,
        CiceroneModule.class
        })
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(MainPresenter presenter);
    void inject(MainFragment fragment);
}
