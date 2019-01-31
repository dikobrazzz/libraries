package com.example.dikob.lesson2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.et);
        final TextView textView = findViewById(R.id.tw);

        Subscription editTextSub = RxTextView.textChanges(editText).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                textView.setText(charSequence);
            }
        });

        Bus bus = new Bus();
        bus.getEvents().subscribe(new Action1() {
            @Override
            public void call(Object o) {
                // Do smth
            }
        });
    }
}
