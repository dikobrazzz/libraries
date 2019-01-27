package com.example.dikob.app4.mvp.presenter;

import com.example.dikob.app4.mvp.view.item.UserItemView;
import com.example.dikob.app4.ui.adapter.UsersAdapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public interface IUsersPresenter {

//    Observer<? super UsersAdapter.ViewHolder> getClickSubject();
    void bindView (UserItemView view);
    int getReposCount();
}
