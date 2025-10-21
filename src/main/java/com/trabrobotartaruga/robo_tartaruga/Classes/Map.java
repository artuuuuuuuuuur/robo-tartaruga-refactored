package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {

    List<List<Position>> positions = new CopyOnWriteArrayList<>();

    public Map(int x, int y) {
        for (int i = 1; i <= y; i++) {
            List<Position> linha = new CopyOnWriteArrayList<>();
            for (int j = 1; j <= x; j++) {
                linha.add(new Position(x, y));
            }
            positions.add(linha);
        }
    }

    public List<List<Position>> getPositions() {
        return positions;
    }
    
}
