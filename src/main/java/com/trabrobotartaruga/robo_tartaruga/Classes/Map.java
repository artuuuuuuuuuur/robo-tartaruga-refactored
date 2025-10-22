package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;

public class Map {

    private final int x;
    private final int y;
    private final List<List<Position>> positions;
    private final List<Bot> bots;
    private boolean foodFound;

    public Map(int x, int y, List<Bot> bots) {
        this.x = x;
        this.y = y;
        positions = new CopyOnWriteArrayList<>();

        for (int i = 0; i < x; i++) {
            positions.add(null);
        }
        for (int i = y - 1; i >= 0; i--) {
            List<Position> row = new CopyOnWriteArrayList<>();
            for (int j = 0; j < x; j++) {
                row.add(new Position(i, j));
            }
            positions.set(i, row);
        }
        this.bots = bots;

        for (Bot bot : bots) {
            this.positions.get(3).get(0).getObjects().add(bot);
        }
        foodFound = false;
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

    public boolean isFoodFound() {
        return foodFound;
    }

    public void setFoodFound(boolean foodFound) {
        this.foodFound = foodFound;
    }

    public void updateBots() {
        bots.clear();
        for (List<Position> positionRow : positions) {
            for (Position positionCell : positionRow) {
                if (!positionCell.getObjects().isEmpty()) {
                    for (Object object : positionCell.getObjects()) {
                        if (object instanceof Bot bot) {
                            if (bot.getPosY() >= 0 && bot.getPosY() < positions.size() && bot.getPosX() >= 0 && bot.getPosX() < positions.get(bot.getPosY()).size()) {
                                positions.get(bot.getPosY()).get(bot.getPosX()).getObjects().add(bot);
                                positionCell.getObjects().remove(bot);
                                bots.add(bot);
                            }
                        }
                    }
                }
            }
        }
    }

}
