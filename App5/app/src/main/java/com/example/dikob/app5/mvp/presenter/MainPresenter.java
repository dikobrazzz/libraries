package com.example.dikob.app5.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;

import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.cashe.PaperCashe;
import com.example.dikob.app5.mvp.model.cashe.RealmCashe;
import com.example.dikob.app5.mvp.model.cashe.RoomCashe;
//import com.example.dikob.app5.mvp.model.repo.RealmUserRepo;
import com.example.dikob.app5.mvp.model.repo.UserRepo;
import com.example.dikob.app5.mvp.model.entity.Repository;
import com.example.dikob.app5.mvp.model.entity.User;
import com.example.dikob.app5.mvp.presenter.list.IRepoListPresenter;
import com.example.dikob.app5.mvp.view.MainView;
import com.example.dikob.app5.mvp.view.item.RepoItemView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public class RepoListPresenter implements IRepoListPresenter {
        PublishSubject<RepoItemView> clickSubject = PublishSubject.create();

        @Override
        public PublishSubject<RepoItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(RepoItemView view) {
            Repository repository = user.getRepos().get(view.getPos());
            view.setTitle(repository.getName());
        }

        @Override
        public int getRepoCount() {
            return user == null || user.getRepos() == null ? 0 : user.getRepos().size();
        }
    }

    public RepoListPresenter repoListPresenter = new RepoListPresenter();
    private Scheduler mainThreadScheduler;
    private UserRepo userRepo;

    private User user;
    private ICashe roomCashe;
    private ICashe paperCashe;
    private ICashe realmCashe;

    public MainPresenter(Scheduler mainThreadScheduler) {
        this.mainThreadScheduler = mainThreadScheduler;
        roomCashe = new RoomCashe();
        paperCashe = new PaperCashe();
        realmCashe = new RealmCashe();
        userRepo = new UserRepo();
}

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        saveInfo(realmCashe);
//        loadInfo();
    }

    @SuppressLint("CheckResult")
    public void saveInfo(ICashe cashe){
        userRepo.getUserByCashe("googlesamples", cashe)
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserReposByCashe(user, cashe)
                            .observeOn(mainThreadScheduler)
                            .subscribe(repositories -> {
                                this.user.setRepos(repositories);
                                getViewState().hideLoading();
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                            });
                }, throwable -> {
                            Timber.e(throwable, "Failed to get user");
                            getViewState().showError(throwable.getMessage());
                        });

    }

    @SuppressLint("CheckResult")
    public void loadInfo() {
        getViewState().showLoading();
        userRepo.getUser("googlesamples")
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .observeOn(mainThreadScheduler)
                            .subscribe(repositories -> {
                                this.user.setRepos(repositories);
                                getViewState().hideLoading();
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                            });


                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                    getViewState().showError(throwable.getMessage());
                });
    }

}
