package com.example.dikob.app4.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dikob.app4.mvp.model.entity.User;
import com.example.dikob.app4.mvp.model.repo.UsersRepo;
import com.example.dikob.app4.mvp.view.MainView;
import com.example.dikob.app4.mvp.view.item.UserItemView;
import com.example.dikob.app4.ui.adapter.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    public class UsersListPresenter implements IUsersPresenter {
        PublishSubject<UserItemView> subject = PublishSubject.create();
        List<String> repos = new ArrayList<>();

//        @Override
//        public Observer<? super UsersAdapter.ViewHolder> getClickSubject() {
//            return subject;
//        }

        @Override
        public void bindView(UserItemView view) {
            view.setRepo(repos.get(view.getPos()));
        }

        @Override
        public int getReposCount() {
            return repos.size();
        }

    }
    private Scheduler scheduler;
    UsersRepo usersRepo;
    private UsersListPresenter listpresenter;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        this.usersRepo = new UsersRepo();
        listpresenter = new UsersListPresenter();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadUser();

        listpresenter.subject.subscribe(userItemView -> {
            listpresenter.repos.get(userItemView.getPos());
            getViewState().showRepos(listpresenter.repos);
        });
    }

    public UsersListPresenter getListPresenter(){
        return listpresenter;
    }

    private void loadUser() {
        usersRepo.getUser("googlesamples")
                .observeOn(scheduler)
                .subscribe(user -> {
                    getViewState().setUsersText(user.getLogin());
                    getViewState().setImageUrl(user.getAvatarUrl());
                },
                        throwable -> {

                });
        usersRepo.getUsersRepos("googlesamples")
                .observeOn(scheduler)
                .subscribe(repoRequests -> {
                    for (int i = 0; i < repoRequests.size() - 1; i++) {
                        listpresenter.repos.add(repoRequests.get(i).getName());
                    }
                });
}

    private void loadDataOkHttp(){
        Single<String> single = Single.fromCallable(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/users/googlesamples") // users/{user}/repos
                    .build();
            return client.newCall(request).execute().body().string();
        });

        single.subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    Timber.d(s);
                });
    }
}
