package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Bot {

    protected final String color;
    protected String type;
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
        this.type = "Robô normal";
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
            case "up" -> {
                moveUp();
                this.lastMove = 1;
            }
            case "down" -> {
                moveDown();
                this.lastMove = 2;
            }
            case "left" -> {
                moveLeft();
                this.lastMove = 3;
            }
            case "right" -> {
                moveRight();
                this.lastMove = 4;
            }
            default ->
                throw new InvalidInputException();
        }
    }

    public void move(int i) throws InvalidMoveException, InvalidInputException {
        switch (i) {
            case 1 -> {
                moveUp();
                this.lastMove = 1;
            }
            case 2 -> {
                moveDown();
                this.lastMove = 2;
            }
            case 3 -> {
                moveLeft();
                this.lastMove = 3;
            }
            case 4 -> {
                moveRight();
                this.lastMove = 4;
            }
            default ->
                throw new InvalidInputException();
        }
    }

    private void moveUp() throws InvalidMoveException {
        if (posY < mapY - 1) {
            posY++;
            lastMove = 1;
        } else {
            posY = mapY - 1;
            throw new InvalidMoveException(color, "cima");
        }
    }

    private void moveDown() throws InvalidMoveException {
        if (posY > 0) {
            posY--;
            lastMove = 2;
        } else {
            throw new InvalidMoveException(color, "baixo");
        }
    }

    private void moveLeft() throws InvalidMoveException {
        if (posX > 0) {
            posX--;
            lastMove = 3;
        } else {
            throw new InvalidMoveException(color, "esquerda");
        }
    }

    private void moveRight() throws InvalidMoveException {
        if (posX < mapX - 1) {
            posX++;
            lastMove = 4;
        } else {
            throw new InvalidMoveException(color, "direita");
        }
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

    public String getType() {
        return type;
    }
}
