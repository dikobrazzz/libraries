package com.example.dikob.app3.android3_1.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.dikob.app3.android3_1.mvp.model.MyConverter;
import com.example.dikob.app3.android3_1.mvp.presenter.MainPresenter;
import com.example.dikob.app3.android3_1.mvp.view.MainView;

import com.example.dikob.app3.R;
import com.example.dikob.app3.android3_1.ui.converter.Converterr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends MvpAppCompatActivity implements View.OnClickListener, MainView {

    public static final int PICK_IMAGE = 1;
    ImageView imageView;
    Dialog convert;

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.btnCounter1)
    Button buttonOne;

    @BindView(R.id.btnCounter2)
    Button buttonTwo;

    @BindView(R.id.btnCounter3)
    Button buttonThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.picture);
        ButterKnife.bind(this);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter(){
        return new MainPresenter(this, AndroidSchedulers.mainThread(), new Converterr(this) {
        });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCounter1:
                presenter.counterClick(1);
                presenter.pickImage(this);
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
    public void pickImage() {

    }

    @Override
    public void showConvertProgressDialog() {
        if (convert == null){
            convert = new AlertDialog.Builder(this)
                    .setNegativeButton("Отмена", (dialog, which) -> presenter.cancelConvert())
                    .setMessage("Конвертация")
                    .create();
        }
        convert.show();
    }

    @Override
    public void dismissConvertProgressDialog() {
        if (convert != null && convert.isShowing()) {
            convert.dismiss();
        }

    }

    @Override
    public void showConvertationSuccessMessage() {
        Toast.makeText(this, R.string.convertation_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConvertationCanceledMessage() {
        Toast.makeText(this, R.string.convertation_canceled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConvertationFailedMessage() {
        Toast.makeText(this, R.string.convertation_failed, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//            presenter.takeImage(this, data);
            presenter.setPath(data);
        }
    }


}
