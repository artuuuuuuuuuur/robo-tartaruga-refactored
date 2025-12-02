package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trabrobotartaruga.robo_tartaruga.TabletopController;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Bomb;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Obstacle;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Map {

    private final int x;
    private final int y;
    private final List<List<Position>> positions;
    private final List<Bot> bots;
    private final List<Bot> winnerBots;
    private final Food food;
    private boolean foodFound;
    private final boolean oneWinner;
    private final List<Obstacle> obstacles;
    private boolean allBotsDisabled;

    public Map(int x, int y, List<Bot> bots, Food food, List<Obstacle> obstacles, boolean oneWinner) {
        this.x = x;
        this.y = y;
        this.food = food;
        this.obstacles = obstacles;
        this.oneWinner = oneWinner;
        positions = new CopyOnWriteArrayList<>();
        winnerBots = new CopyOnWriteArrayList<>();
        this.allBotsDisabled = false;

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
            this.positions.get(0).get(0).getObjects().add(bot);
        }

        positions.get(food.getPosX()).get(food.getPosY()).getObjects().add(food);

        for (Obstacle obstacle : obstacles) {
            positions.get(obstacle.getPosX()).get(obstacle.getPosY()).getObjects().add(obstacle);
        }

        foodFound = false;
    }

    public List<List<Position>> getPositions() {
        return positions;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Food getFood() {
        return food;
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

    public boolean checkFoodFound() {
        winnerBots.clear();
        for (Bot bot : bots) {
            if (bot.getPosX() == food.getPosX() && bot.getPosY() == food.getPosY()) {
                if (!winnerBots.contains(bot)) {
                    winnerBots.add(bot);
                    disableBot(bot);
                }
                foodFound = true;
            }
        }
        return foodFound;
    }

    private void disableBot(Bot bot) {
        bot.setActive(false);
    }

    private void checkAllBotsDisabled() {
        allBotsDisabled = true;
        for (Bot bot : bots) {
            if (bot.isActive()) {
                allBotsDisabled = false;
            }
        }
    }

    public void updateBots() {
        bots.clear();
        winnerBots.clear();
        for (List<Position> positionRow : positions) {
            for (Position positionCell : positionRow) {
                if (!positionCell.getObjects().isEmpty()) {
                    for (Object object : positionCell.getObjects()) {
                        if (object instanceof Bot bot && (bot.getPosY() >= 0 && bot.getPosY() < positions.size() && bot.getPosX() >= 0 && bot.getPosX() < positions.get(bot.getPosY()).size())) {
                            positions.get(bot.getPosY()).get(bot.getPosX()).getObjects().add(bot);
                            positionCell.getObjects().remove(bot);
                            bots.add(bot);
                        }
                    }
                }
            }
        }
        checkFoodFound();
        checkAllBotsDisabled();
    }

    public void obstacleAction(TabletopController tabletopController) throws InvalidMoveException, InvalidInputException {
        for (Obstacle obstacle : obstacles) {
            obstacle.hit(this, tabletopController);
            if (obstacle instanceof Bomb bomb && bomb.isExploded()) {
                obstacles.remove(bomb);
            }
        }
    }

    public boolean hasAnotherActiveBot(Bot current) {
        for (Bot b : bots) {
            if (!b.equals(current) && b.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        if (oneWinner && foodFound) {
            return true;
        }

        if (!oneWinner && winnerBots.size() == 2) {
            return true;
        }

        return allBotsDisabled;
    }

    public List<Bot> getWinnerBots() {
        return winnerBots;
    }

    public boolean isOneWinner() {
        return oneWinner;
    }

    public boolean isAllBotsDisabled() {
        return allBotsDisabled;
    }

    public void setAllBotsDisabled(boolean allBotsDisabled) {
        this.allBotsDisabled = allBotsDisabled;
    }

}
