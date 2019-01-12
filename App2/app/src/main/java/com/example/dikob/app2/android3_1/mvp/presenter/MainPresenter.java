package com.example.dikob.app2.android3_1.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dikob.app2.android3_1.mvp.view.MainView;

import com.example.dikob.app2.R;
import com.example.dikob.app2.android3_1.mvp.model.Model;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    Model model;

    public MainPresenter() {
        this.model = new Model();
    }

    public int calculateButtonValue(int index){
        model.setAt(index, model.getAt(index) + 1);
        return model.getAt(index);
    }

    public void counterClick(int id){
        switch (id){
            case 1:
                getViewState().setButtonOneValue(calculateButtonValue(0));
                break;
            case 2:
                getViewState().setButtonTwoValue(calculateButtonValue(1));
                break;
            case 3:
                getViewState().setButtonThreeValue(calculateButtonValue(2));
                break;
        }
    }

}
