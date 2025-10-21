package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;

public class Map {
    private final int x; 
    private final int y; 
    private List<List<Position>> positions;
    private List<Bot> bots;

    public Map(int x, int y, List<Bot> bots) {
        this.x = x;
        this.y = y;
        positions = new CopyOnWriteArrayList<>();
        for (int i = y-1; i <= 0; i--) {
            List<Position> row = new CopyOnWriteArrayList<>();
            for (int j = 0; j < x; j++) {
                row.add(j, new Position(x, y));
            }
            positions.add(y, row);
        }
        this.bots = bots;
        
        for (Bot bot : bots) {
            this.positions.get(0).get(0).getObjects().add(bot);
        }
    }

    public List<List<Position>> getPositions() {
        return positions;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Bot> getBots() {
        return bots;
    }
    
    public void updateBots() {
        bots.clear();
        for (List<Position> positionRow : positions) {
            for (Position positionCell : positionRow) {
                if(!positionCell.getObjects().isEmpty()) {
                    for (Object object : positionCell.getObjects()) {
                        if(object instanceof Bot bot) {
                            if(bot.getPosX() != positionCell.getPosX() && bot.getPosY() != positionCell.getPosY()) {
                                positions.get(bot.getPosY()).get(bot.getPosX()).getObjects().add(bot);
                                positionCell.getObjects().remove(bot);
                            }
                        }
                    }
                }
            }
        }
    }
    
}
