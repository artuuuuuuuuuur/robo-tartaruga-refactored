package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Stone extends Obstacle {

    public Stone(int id, int posX, int posY) {
        super(id, posX, posY);
    }

    @Override
    public void hit(Map map) {
        for (Object object : map.getPositions().get(posY).get(posX).getObjects()) {
            if (object instanceof Bot bot) {
                switch (bot.getLastMove()) {
                    case 1 -> {
                        try {
                            bot.mover(2);
                        } catch (InvalidMoveException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    case 2 -> {
                        try {
                            bot.mover(1);
                        } catch (InvalidMoveException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    case 3 -> {
                        try {
                            bot.mover(4);
                        } catch (InvalidMoveException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    case 4 -> {
                        try {
                            bot.mover(3);
                        } catch (InvalidMoveException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                }
            }
        }
    }
}
