package com.example.dikob.app5;

import android.util.Log;

import com.example.dikob.app5.di.DaggerTestInstrumentalComponent;
import com.example.dikob.app5.di.TestInstrumentalComponent;
import com.example.dikob.app5.di.modules.ApiModule;
import com.example.dikob.app5.di.modules.CasheModule;
import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.cashe.RealmCashe;
import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.repo.UserRepo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;

public class CasheInstrumentedTest {
    Realm realm;
    private TestScheduler scheduler;
    User user1;
    User user2;

    @Inject @Named("realm")
    ICashe cashe;

    User user = new User("somelogin", "someava", "somerepo");

    @BeforeClass
    public static void setupClass() throws IOException {
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        Timber.d("tear down class");
    }

    @Before
    public void setup(){
        Timber.d("setup");
        scheduler = new TestScheduler();
        TestInstrumentalComponent component = DaggerTestInstrumentalComponent.builder()
                .casheModule(new CasheModule(){
                    @Override
                    public ICashe realmCashe() {
                        cashe = super.realmCashe();
                        return cashe;
                    }
                }).build();
        component.inject(this);
    }

    @After
    public void tearDown(){
        Timber.d("tear down");
    }

    @Test
    public void saveUser(){
        User userr = cashe.saveUser(user);
                assertEquals(userr.getLogin(), user.getLogin());
        assertEquals(userr.getAvatarUrl(), user.getAvatarUrl());
        assertEquals(userr.getReposUrl(), user.getReposUrl());
    }

    @Test
    public void loadUser(){
        cashe.saveUser(user);
        Single<User> userr = cashe.loadUser(user.getLogin());
        userr.subscribe(user3 -> {
            assertEquals(user3.getLogin(), user.getLogin());
            user1 = user3;
        });
        user1.getLogin();

        //        TestInstrumentalComponent component = DaggerTestInstrumentalComponent.builder()
//                .casheModule(new CasheModule(){
//                    @Override
//                    public ICashe realmCashe() {
//                        cashe = super.realmCashe();
//
//                        cashe.saveUser(user);
//                        TestScheduler testScheduler = new TestScheduler();
//        cashe.loadUser("someuser")
//                .subscribeOn(testScheduler)
//                .map(new Function<User, Object>() {
//
//                    @Override
//                    public Object apply(User userr) throws Exception {
//                        user1 = userr;
//                        return null;
//                    }
//                });
//        if (user2.equals(user1)){
//            Timber.d("kkkk");
//        }
//        return cashe;
//
//    }
//})
//        .build();
//        component.inject(this);
//        observer.awaitTerminalEvent();
//                        observer.assertValueCount(1);
//                        assertEquals(observer.values().get(0).getLogin(), "someuser");
//                        assertEquals(observer.values().get(0).getAvatarUrl(), "someava");
//                        assertEquals(observer.values().get(0).getReposUrl(), "somerepo");
//                        return cashe;
    }

    @Test
    public void saveAndLoadUserRepo(){
        User user = new User("somelogin", "someava", "somerepo");
        List<Repository> repositories = new ArrayList<>();
        repositories.add(new Repository("1", "repo"));
        TestInstrumentalComponent component = DaggerTestInstrumentalComponent.builder()
                .casheModule(new CasheModule(){
                    @Override
                    public ICashe realmCashe() {
                        super.realmCashe().loadRepository(user).map(list -> {
                           assertEquals( list.get(0).getId(), "1");
                            assertEquals( list.get(0).getName(), "repo");
                            return list;
                        });
                        return super.realmCashe();
                    }
                })
                .build();
        component.inject(this);
    }


}
