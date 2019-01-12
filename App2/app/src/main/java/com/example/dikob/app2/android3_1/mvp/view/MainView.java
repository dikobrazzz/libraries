package com.example.dikob.app2.android3_1.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void setButtonOneValue(int value);
    void setButtonTwoValue(int value);
    void setButtonThreeValue(int value);

}
