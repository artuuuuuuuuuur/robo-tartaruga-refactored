package com.trabrobotartaruga.robo_tartaruga.exceptions;

public class InvalidMoveException extends Exception {

    private final String botColor, direction;

    public InvalidMoveException(String botColor, String direction) {
        this.botColor = botColor;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "O bot " + colorDecoder(botColor) + " não pode ir para " + direction;
    }

    private String colorDecoder(String hexColor) {
        switch (hexColor) {
            case "0xffffffff" -> {
                return "branco";
            }
            case "0x000000ff" -> {
                return "preto";
            }
            case "0x00ff00ff" -> {
                return "verde";
            }
            case "0xff0000ff" -> {
                return "vermelho";
            }
            case "0xffff00ff" -> {
                return "amarelo";
            }
            case "0x0000ffff" -> {
                return "azul";
            }
            default -> {
                return hexColor;
            }
        }
    }
}
