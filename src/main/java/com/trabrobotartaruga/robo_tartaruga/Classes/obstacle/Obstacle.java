package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;

public abstract class Obstacle {
    protected final int id;
    protected final int posX;
    protected final int posY;

    public Obstacle(int id, int posX, int posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
    }
    
    public abstract void hit(Map map); 

    public int getId() {
        return id;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
