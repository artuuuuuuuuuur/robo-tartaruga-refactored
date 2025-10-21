package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Bot {

    protected final String cor;
    protected int posX;
    protected int posY;
    protected int lastMove;

    public Bot(String cor, Map map) {
        this.cor = cor;
        this.posX = 0;
        this.posY = 0;
    }

    public String getCor() {
        return cor;
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

    public void setPosicaoY(int posY) {
        this.posY = posY;
    }

    public void mover(String movimento) throws InvalidMoveException {

        switch (movimento.toLowerCase()) {
            case "up" -> {
                posY++;
                lastMove = 1;
            }
            case "down" -> {
                posY--;
                lastMove = 2;
            }
            case "right" -> {
                posX++;
                lastMove = 3;
            }
            case "left" -> {
                posX--;
                lastMove = 4;
            }
            default -> {
                System.out.println(movimento + " é um movimento inválido!");
                return;
            }
        }

        if (posX < 0 || posX > 4) {
            throw new InvalidMoveException();
        } else if (posY < 0 || posY > 4) {
            throw new InvalidMoveException();
        }

        System.out.println("Robô " + cor + " está agora em (" + posX + ", " + posY + ")");
    }

    public void mover(int movimentoI) throws InvalidMoveException {
        switch (movimentoI) {
            case 1 ->
                posY++;
            case 2 ->
                posY--;
            case 3 ->
                posX++;
            case 4 ->
                posX--;
            default -> {
                System.out.println("Apenas 1, 2, 3 e 4 são permitidos!");
                return;
            }
        }

        if (posX < 0 || posX > 4) {
            throw new InvalidMoveException();
        } else if (posY < 0 || posY > 4) {
            throw new InvalidMoveException();
        }

        lastMove = movimentoI;
        System.out.println("Robô " + cor + " está agora em (" + posX + ", " + posY + ")");
    }

    public boolean verificarAlimentoEncontrado(int x, int y) {
        return posX == x && posY == y;
    }

    public int getLastMove() {
        return lastMove;
    }
}
