package com.example.soundclounddemo.model;

import java.util.List;

public class Container<T> {
    List<T> list ;

    public Container(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
