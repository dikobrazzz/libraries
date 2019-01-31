package com.example.dikob.app5.mvp.presenter.list;

import io.reactivex.subjects.PublishSubject;
import com.example.dikob.app5.mvp.view.item.RepoItemView;

public interface IRepoListPresenter
{
    PublishSubject<RepoItemView> getClickSubject();
    void bindView(RepoItemView rowView);
    int getRepoCount();
}
