package com.example.dikob.app4.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void setImageUrl(String url);
    void setUsersText(String text);

    void showRepos(List<String> repos);

    void updateUsersList();
    void init();
}
