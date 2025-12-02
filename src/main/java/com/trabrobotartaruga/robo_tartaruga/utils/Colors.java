package com.trabrobotartaruga.robo_tartaruga.utils;

import java.util.Map;

public class Colors {

    private static final Map<String, String> COLOR_MAP = Map.of(
            "0xffffffff", "branco",
            "0x000000ff", "preto",
            "0x00ff00ff", "verde",
            "0xff0000ff", "vermelho",
            "0xffff00ff", "amarelo",
            "0x0000ffff", "azul"
    );

    private Colors() {
    }

    public static String toString(String hexColor) {
        return COLOR_MAP.getOrDefault(hexColor, hexColor);
    }
}
