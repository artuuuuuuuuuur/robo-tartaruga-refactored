package com.trabrobotartaruga.robo_tartaruga.utils;

public class Colors {

    private Colors() {
    }

    public static String toString(String hexColor) {
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
