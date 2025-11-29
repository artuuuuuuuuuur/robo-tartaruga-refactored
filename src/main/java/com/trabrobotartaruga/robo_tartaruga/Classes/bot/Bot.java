package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Bot {

    protected final String color;
    protected int posX;
    protected int posY;
    protected int lastMove;
    protected int validMoves;
    protected int invalidMoves;
    protected int rounds;
    protected final int mapX;
    protected final int mapY;
    protected boolean active;

    public Bot(String color, int mapX, int mapY) {
        this.color = color;
        this.posX = 0;
        this.posY = 0;
        this.mapX = mapX;
        this.mapY = mapY;
        this.validMoves = 0;
        this.invalidMoves = 0;
        this.rounds = 0;
        this.active = true;
    }

    public String getColor() {
        return color;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void move(String move) throws InvalidMoveException, InvalidInputException {
        switch (move.toLowerCase()) {
            case "up" ->
                this.lastMove = 1;
            case "down" ->
                this.lastMove = 2;
            case "left" ->
                this.lastMove = 3;
            case "right" ->
                this.lastMove = 4;
            default ->
                throw new InvalidInputException();
        }

        switch (move.toLowerCase()) {
            case "up" -> {
                if (posY < mapY - 1) {
                    moveUp();
                } else {
                    posY = mapY - 1;
                    throw new InvalidMoveException(color, "cima");
                }
            }
            case "down" -> {
                if (posY > 0) {
                    moveDown();
                } else {
                    throw new InvalidMoveException(color, "baixo");
                }
            }
            case "right" -> {
                if (posX < mapX - 1) {
                    moveRight();
                } else {
                    throw new InvalidMoveException(color, "direita");
                }
            }
            case "left" -> {
                if (posX > 0) {
                    moveLeft();
                } else {
                    throw new InvalidMoveException(color, "esquerda");
                }
            }
            default -> {
                throw new InvalidInputException();
            }
        }
    }

    public void move(int i) throws InvalidMoveException, InvalidInputException {
        this.lastMove = i;

        switch (i) {
            case 1 -> {
                if (posY < mapY - 1) {
                    moveUp();
                } else {
                    posY = mapY - 1;
                    throw new InvalidMoveException(color, "cima");
                }
                
            }

            case 2 -> {
                if (posY > 0) {
                    moveDown();
                } else {
                    throw new InvalidMoveException(color, "baixo");
                }
                
            }
            case 3 -> {
                if (posX > 0) {
                    moveLeft();
                } else {
                    throw new InvalidMoveException(color, "esquerda");
                }
                
            }
            case 4 -> {
                if (posX < mapX - 1) {
                    moveRight();
                } else {
                    throw new InvalidMoveException(color, "direita");
                }
                
            }
            default -> {
                throw new InvalidInputException();
            }
        }
    }

    private void moveUp() {
        posY++;
        lastMove = 1;
    }

    private void moveDown() {
        posY--;
        lastMove = 2;
    }

    private void moveLeft() {
        posX--;
        lastMove = 3;
    }

    private void moveRight() {
        posX++;
        lastMove = 4;
    }

    public int getLastMove() {
        return lastMove;
    }

    public int getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(int validMoves) {
        this.validMoves = validMoves;
    }

    public int getInvalidMoves() {
        return invalidMoves;
    }

    public void setInvalidMoves(int invalidMoves) {
        this.invalidMoves = invalidMoves;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
