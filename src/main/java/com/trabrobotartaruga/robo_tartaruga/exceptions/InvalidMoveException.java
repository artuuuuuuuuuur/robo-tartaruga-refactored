package com.trabrobotartaruga.robo_tartaruga.exceptions;

import com.trabrobotartaruga.robo_tartaruga.utils.Colors;

public class InvalidMoveException extends Exception {

    private final String botColor, direction;

    public InvalidMoveException(String botColor, String direction) {
        this.botColor = botColor;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "O bot " + Colors.toString(botColor) + " não pode ir para " + direction;
    }
}
