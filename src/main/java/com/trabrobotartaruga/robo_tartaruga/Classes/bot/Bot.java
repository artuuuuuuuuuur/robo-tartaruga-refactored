package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Bot {

    protected final String color;
    protected int posX;
    protected int posY;
    protected int lastMove;
    protected final int mapX;
    protected final int mapY;

    public Bot(String color, int mapX, int mapY) {
        this.color = color;
        this.posX = 0;
        this.posY = 3;
        this.mapX = mapX;
        this.mapY = mapY;
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

    public void move(String move) throws InvalidMoveException {
        switch (move.toLowerCase()) {
            case "up" -> {
                if (posY > 0) {
                    moveUp();
                } else {
                    posY = 0;
                    throw new InvalidMoveException();
                }
                break;
            }
            case "down" -> {
                if (posY < mapX - 1) {
                    moveDown();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            case "right" -> {
                if (posX < mapX - 1) {
                    moveRight();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            case "left" -> {
                if (posX > 0) {
                    moveLeft();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            default -> {
                System.out.println(move + " é um movimento inválido!");
                return;
            }
        }
        System.out.println("Robô " + color + " está agora em (" + posX + ", " + posY + ")");
    }

    public void move(int i) throws InvalidMoveException {
        switch (i) {
            case 1 -> {
                if (posY > 0) {
                    moveUp();
                } else {
                    posY = 0;
                    throw new InvalidMoveException();
                }
                break;
            }

            case 2 -> {
                if (posY < mapX - 1) {
                    moveDown();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            case 3 -> {
                if (posX > 0) {
                    moveLeft();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            case 4 -> {
                if (posX < mapX - 1) {
                    moveRight();
                } else {
                    throw new InvalidMoveException();
                }
                break;
            }
            default -> {
                System.out.println("Apenas 1, 2, 3 e 4 são permitidos!");
                return;
            }
        }
        System.out.println("Robô " + color + " está agora em (" + posX + ", " + posY + ")");
    }

    private void moveUp() {
        posY--;
        lastMove = 1;
    }

    private void moveDown() {
        posY++;
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
}
