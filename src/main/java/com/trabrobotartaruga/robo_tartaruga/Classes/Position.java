package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Position {

    protected int posX;
    protected int posy;
    protected List<Object> objects = new CopyOnWriteArrayList<>();

    public Position(int posX, int posy) {
        this.posX = posX;
        this.posy = posy;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
