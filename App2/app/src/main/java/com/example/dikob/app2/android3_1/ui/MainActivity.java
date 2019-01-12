package com.example.dikob.app2.android3_1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.dikob.app2.android3_1.mvp.presenter.MainPresenter;
import com.example.dikob.app2.android3_1.mvp.view.MainView;

import com.example.dikob.app2.R;

public class MainActivity extends MvpAppCompatActivity implements View.OnClickListener, MainView {

    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOne = findViewById(R.id.btnCounter1);
        buttonTwo = findViewById(R.id.btnCounter2);
        buttonThree = findViewById(R.id.btnCounter3);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter(){
        return new MainPresenter();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCounter1:
                presenter.counterClick(1);
                break;
            case R.id.btnCounter2:
                presenter.counterClick(2);
                break;
            case R.id.btnCounter3:
                presenter.counterClick(3);
                break;

        }
    }

    @Override
    public void setButtonOneValue(int value) {
        buttonOne.setText(String.format(getString(R.string.countEquals), value));
    }

    @Override
    public void setButtonTwoValue(int value) {
        buttonTwo.setText(String.format(getString(R.string.countEquals), value));
    }

    @Override
    public void setButtonThreeValue(int value) {
        buttonThree.setText(String.format(getString(R.string.countEquals), value));
    }
}
