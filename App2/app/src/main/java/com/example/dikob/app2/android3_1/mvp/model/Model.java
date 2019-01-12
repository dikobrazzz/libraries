package com.example.dikob.app2.android3_1.mvp.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    List<Integer> counters;

    public Model(){
        counters = new ArrayList<>();
        counters.add(0);
        counters.add(0);
        counters.add(0);
    }

    public int getAt(int pos){
        return counters.get(pos);
    }

    public void setAt(int pos, int value){
        counters.set(pos, value);
    }
}
