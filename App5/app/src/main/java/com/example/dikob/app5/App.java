package com.example.dikob.app5;

import android.app.Application;

import com.example.dikob.app5.di.AppComponent;
import com.example.dikob.app5.di.DaggerAppComponent;
import com.example.dikob.app5.di.modules.AppModule;
import com.example.dikob.app5.mvp.model.entity.room.db.UserDatabase;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class App extends Application {
    static private App instance;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Timber.plant(new Timber.DebugTree());
        UserDatabase.create(this);
        Paper.init(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
    }

    public static App getInstance() {
        return instance;
    }
}
