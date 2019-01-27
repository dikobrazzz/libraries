package com.example.dikob.app3.android3_1.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    void pickImage();

    void showConvertProgressDialog();

    void dismissConvertProgressDialog();

    void showConvertationSuccessMessage();

    void showConvertationCanceledMessage();

    void showConvertationFailedMessage();

    void setButtonOneValue(int value);
    void setButtonTwoValue(int value);
    void setButtonThreeValue(int value);
}
