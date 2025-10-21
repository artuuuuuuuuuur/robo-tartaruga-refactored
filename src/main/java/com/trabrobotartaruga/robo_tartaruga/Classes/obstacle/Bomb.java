package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;

public class Bomb extends Obstacle {

    public Bomb(int id, int posX, int posY) {
        super(id, posX, posY);
    }
    
    @Override
    public void hit(Map map) {
        for (Object object : map.getPositions().get(posY).get(posX).getObjects()) {
            if (object instanceof Bot bot) {
            }
        }
    }
}
