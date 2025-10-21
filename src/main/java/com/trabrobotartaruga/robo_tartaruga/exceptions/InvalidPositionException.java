package com.trabrobotartaruga.robo_tartaruga.exceptions;

public class InvalidPositionException extends Exception {
    
    @Override
    public String toString() {
        return "Já existe algo nessa posição";
    }
}
