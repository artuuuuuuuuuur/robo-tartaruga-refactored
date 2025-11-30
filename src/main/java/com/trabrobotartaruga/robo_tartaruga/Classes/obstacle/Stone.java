package com.trabrobotartaruga.robo_tartaruga.classes.obstacle;

import com.trabrobotartaruga.robo_tartaruga.TabletopController;
import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.SmartBot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Stone extends Obstacle {

    public Stone(int id, int posX, int posY) {
        super(id, posX, posY);
    }

    @Override
    public void hit(Map map, TabletopController tabletopController) throws InvalidMoveException, InvalidInputException {
        for (Object object : map.getPositions().get(posY).get(posX).getObjects()) {
            if (object instanceof Bot bot) {
                switch (bot.getLastMove()) {
                    case 1 ->
                        bot.move(2);
                    case 2 ->
                        bot.move(1);
                    case 3 ->
                        bot.move(4);
                    case 4 ->
                        bot.move(3);
                }
                if (bot instanceof SmartBot smartBot) {
                    smartBot.setLastGoodMove(false);
                }
                tabletopController.createLogLabel(bot.getType() + " bateu na pedra.");
            }
        }
    }

}
