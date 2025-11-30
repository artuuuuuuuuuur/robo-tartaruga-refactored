package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.TabletopController;
import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Bomb extends Obstacle {

    private boolean exploded;

    public Bomb(int id, int posX, int posY) {
        super(id, posX, posY);
        this.exploded = false;
    }

    @Override
    public void hit(Map map, TabletopController tabletopController) throws InvalidMoveException, InvalidInputException {
        if (!exploded) {
            for (Object object : map.getPositions().get(posY).get(posX).getObjects()) {
                if (object instanceof Bot bot) {
                    bot.setActive(false);
                    exploded = true;
                    tabletopController.createLogLabel(bot.getType() + " explodiu.");
                }
            }
        }
    }

    public boolean isExploded() {
        return exploded;
    }
}
