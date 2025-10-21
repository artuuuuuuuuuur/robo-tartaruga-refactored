package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Position {

    protected int posX;
    protected int posY;
    protected List<Object> objects;

    public Position(int posX, int posY,  List<Object> objects) {
        this.posX = posX;
        this.posY = posY;
        this.objects = objects;
    }
    
    public Position(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.objects = new CopyOnWriteArrayList<>();
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
}
