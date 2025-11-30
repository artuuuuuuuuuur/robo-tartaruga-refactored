package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;
import java.util.Random;

public class RandomBot extends Bot {

    public RandomBot(String color, int mapX, int mapY) {
        super(color, mapX, mapY);
        type = "Robô aleatório";
    }

    @Override
    public void move(String ignored) throws InvalidMoveException, InvalidInputException {
        int direction = new Random().nextInt(4) + 1;
        super.move(direction);
    }
}
