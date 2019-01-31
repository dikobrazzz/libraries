package com.example.dikob.app3.android3_1.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dikob.app3.android3_1.mvp.model.MyConverter;
import com.example.dikob.app3.android3_1.mvp.view.MainView;

import com.example.dikob.app3.android3_1.mvp.model.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final int MY_PERM_REQ = 1;
    Model model;
    public static final int PICK_IMAGE = 1;
    MainView view;
    private MyConverter converter;
    Scheduler scheduler;
    Disposable subscription;
    private final static String TAG = "DST";

    public MainPresenter(MainView view, Scheduler scheduler, MyConverter converter) {
        this.model = new Model();
        this.view = view;
        this.scheduler = scheduler;
        this.converter = converter;
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

    public void pickImage(Activity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void takeImage(Activity activity, Intent data) {
        Uri imageUri = data.getData();
        if (ContextCompat.checkSelfPermission (activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERM_REQ);
        }

        final String docId = DocumentsContract.getDocumentId(imageUri);
        final String[] split = docId.split(":");

        String picturePath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + split[1] + ".png";
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            FileOutputStream fos = new FileOutputStream(new File(picturePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelConvert() {
        if (subscription != null && !subscription.isDisposed()){
            subscription.dispose();
            view.dismissConvertProgressDialog();
            view.showConvertationCanceledMessage();
        }
    }

    public void setPath(Intent data) {
        Uri uri = data.getData();
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        String out = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + split[1] + ".png";
        String src = uri.toString();
        String dst = Uri.fromFile(new File(out)).toString();
        Log.d(TAG, dst);
        view.showConvertProgressDialog();
        converter.convertJpgToPng(src, dst)
                .subscribeOn(Schedulers.computation())
                .observeOn(scheduler)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscription = d;
                    }

                    @Override
                    public void onComplete() {
                        view.showConvertationSuccessMessage();
                        view.dismissConvertProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showConvertationFailedMessage();
                        view.dismissConvertProgressDialog();
                    }
                });
    }
}
