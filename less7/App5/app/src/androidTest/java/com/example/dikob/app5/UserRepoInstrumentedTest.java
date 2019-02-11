package com.example.dikob.app5;

import com.example.dikob.app5.di.DaggerTestInstrumentalComponent;
import com.example.dikob.app5.di.TestInstrumentalComponent;
import com.example.dikob.app5.di.modules.ApiModule;
import com.example.dikob.app5.di.modules.CasheModule;
import com.example.dikob.app5.mvp.model.api.ApiService;
import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.model.repo.UserRepo;
import com.example.dikob.app5.mvp.presenter.MainPresenter;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;

public class UserRepoInstrumentedTest {

    @Inject
    UserRepo userRepo;

    private static MockWebServer mockWebServer;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        Timber.d("tear down class");
        mockWebServer.shutdown();
    }

    @Before
    public void setup(){
        Timber.d("setup");
        TestInstrumentalComponent component = DaggerTestInstrumentalComponent.builder()
                .apiModule(new ApiModule(){
                    @Override
                    public String baseUrl() {
                        return mockWebServer.url("/").toString();
                    }
                })
//                .casheModule(new CasheModule(){
//                    @Override
//                    public ICashe realmCashe() {
//                        return
//                    }
//                })
        .build();
        component.inject(this);
    }

    @After
    public void tearDown(){
        Timber.d("tear down");
    }

    @Test
    public void getUser(){
        mockWebServer.enqueue(createUserResponse("someuser", "someava", "somerepo"));
        TestObserver<User> observer = new TestObserver<>();
        userRepo.getUser("someuser").subscribe(observer);
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(), "someuser");
        assertEquals(observer.values().get(0).getAvatarUrl(), "someava");
        assertEquals(observer.values().get(0).getReposUrl(), "somerepo");
    }

    @Test
    public void getUserRepo(){
        User user = new User("someuser", "someava", "somerepo");
        List<Repository> repositoryList = new ArrayList<>();
        repositoryList.add(new Repository("1", "repo"));
        user.setRepos(repositoryList);
        mockWebServer.enqueue(createUserRepoResponse("1", "repo"));
        TestObserver<List<Repository>> observer = new TestObserver<>();
        userRepo.getUserRepos(user).subscribe(observer);
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).get(0).getId(), "1");
        assertEquals(observer.values().get(0).get(0).getName(), "repo");

    }

    private MockResponse createUserRepoResponse(String id, String name) {
            String body = "[{\"id\":\"" + id + "\", \"name\":\"" + name + "\"}]";
            return new MockResponse().setBody(body);
    }

    private MockResponse createUserResponse(String login, String avatarUrl, String reposUrl){
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\", \"repos_url\":\"" + reposUrl + "\"}";
        return new MockResponse().setBody(body);
    }
}
