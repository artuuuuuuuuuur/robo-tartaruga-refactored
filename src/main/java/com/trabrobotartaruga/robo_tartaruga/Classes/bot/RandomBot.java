package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;
import java.util.Random;

public class RandomBot extends Bot{
    private final Random random;

    public RandomBot(String color, int mapX, int mapY){
        super(color, mapX, mapY);
        this.random = new Random();
    }

    public void moveRandom() throws InvalidMoveException{
        int direction = random.nextInt(4) + 1;
        move(direction);
    }

    @Override
    public void move(String ignored) throws InvalidMoveException{
        moveRandom();
    }
}
