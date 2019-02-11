package com.example.dikob.app5;

import com.example.dikob.app5.di.DaggerAppComponent;
import com.example.dikob.app5.di.DaggerTestComponent;
import com.example.dikob.app5.di.TestComponent;
import com.example.dikob.app5.di.modules.TestRepoModule;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.repo.UserRepo;
import com.example.dikob.app5.mvp.presenter.MainPresenter;
import com.example.dikob.app5.mvp.view.MainView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import timber.log.Timber;

public class MainPresenterUnitTest {

    private MainPresenter mainPresenter;
    private TestScheduler testScheduler;

    @Mock
    MainView mainView;

    @BeforeClass
    public static void setupClass(){
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @AfterClass
    public static void tearDownClass(){
        Timber.d("tear down class");
    }

    @Before
    public void setup(){
        Timber.d("setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        mainPresenter = Mockito.spy(new MainPresenter(testScheduler));
    }

    @After
    public void tearDown(){
        Timber.d("tear down");
    }

    @Test
    public void saveInfoSuccess(){
        Timber.d("saveInfoSuccess");

        User user = new User("googlesamples", "avatar_url", "repos_url");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule(){
                    @Override
                    public UserRepo userRepo() {
                        UserRepo userRepo = super.userRepo();
                        Mockito.when(userRepo.getUserByCashe(user.getLogin())).thenReturn(Single.just(user));
                        Mockito.when(userRepo.getUserReposByCashe(user)).thenReturn(Single.just(new ArrayList<>()));
                        return userRepo;
                    }
                }).build();

        component.inject(mainPresenter);
        mainPresenter.attachView(mainView);
        Mockito.verify(mainPresenter).saveInfo("googlesamples");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).showAvatar(user.getAvatarUrl());
        Mockito.verify(mainView).setUsername(user.getLogin());

        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).updateRepoList();
    }

    @Test
    public void saveInfoFailure(){
        Timber.d("saveInfoFailure");
        User user = new User("googlesamples", "avatar_url", "repos_url");
        Throwable error = new Throwable();
        TestComponent component = DaggerTestComponent.builder().testRepoModule(new TestRepoModule(){
            @Override
            public UserRepo userRepo() {
                UserRepo userRepo = super.userRepo();
                //////Ошибка юзера
                //                Mockito.when(userRepo.getUserByCashe(user.getLogin())).thenReturn(Single.error(error));

                //////ошибка repos
                Mockito.when(userRepo.getUserByCashe(user.getLogin())).thenReturn(Single.just(user));
                Mockito.when(userRepo.getUserReposByCashe(user)).thenReturn(Single.error(error));
                return userRepo;
            }
        }).build();
        component.inject(mainPresenter);
        mainPresenter.attachView(mainView);
        Mockito.verify(mainPresenter).saveInfo("googlesamples");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        Mockito.verify(mainView).showError(error.getMessage());
    }
    }
