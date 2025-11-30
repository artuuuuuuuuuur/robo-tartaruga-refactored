package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;
import java.util.Random;

public class SmartBot extends Bot {

    private boolean lastGoodMove;

    public SmartBot(String color, int mapX, int mapY) {
        super(color, mapX, mapY);
        type = "Robô inteligente";
        lastGoodMove = true;
    }

    public boolean isLastGoodMove() {
        return lastGoodMove;
    }

    public void setLastGoodMove(boolean lastGoodMove) {
        this.lastGoodMove = lastGoodMove;
    }

    private int newMove() {
        int newMove;

        do {
            newMove = new Random().nextInt(4) + 1;
        } while (newMove == lastMove);

        return newMove;
    }

    @Override
    public void move(int motion) throws InvalidMoveException, InvalidInputException {
        boolean moved = false;
        if (motion == 0) {
            motion = newMove();
        }

        if (lastGoodMove) {
            try {
                super.move(motion);
            } catch (InvalidMoveException e) {
                lastGoodMove = false;
                throw e;
            }
        } else {
            while (!moved) {
                try {
                    super.move(motion);
                    moved = true;
                    lastGoodMove = true;
                } catch (InvalidMoveException e) {
                    motion = newMove();
                }
            }
        }

    }
}
