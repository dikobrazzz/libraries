package com.example.dikob.lesson2;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Bus {
    private static Bus instance;
    PublishSubject<String> subject = PublishSubject.create();

    public static Bus instanceOf(){
        if (instance == null){
            instance = new Bus();
        }
        return instance;
    }

    public void setString(String string){
        subject.onNext(string);
    }

    public Observable getEvents(){
        return subject;
    }

}
