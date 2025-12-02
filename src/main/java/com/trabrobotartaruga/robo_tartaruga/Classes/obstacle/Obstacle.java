package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.TabletopController;
import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public abstract class Obstacle {
    protected final int id;
    protected final int posX;
    protected final int posY;

    protected Obstacle(int id, int posX, int posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
    }
    
    public abstract void hit(Map map, TabletopController tabletopController) throws InvalidMoveException, InvalidInputException; 

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
