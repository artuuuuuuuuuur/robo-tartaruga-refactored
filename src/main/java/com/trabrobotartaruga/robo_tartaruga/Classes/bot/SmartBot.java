package com.trabrobotartaruga.robo_tartaruga.classes.bot;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;
import java.util.Random;

public class SmartBot extends Bot{
    private final Random random;

    public SmartBot(String color, Map map){
        super(color , map);
        this.random = new Random();
    }

    private String gerarNovoMovimento(String ultimo){
        String[] moves = {"up", "down", "left", "right"};
        String novo;

        do{
            novo = moves[random.nextInt(moves.length)];
        } while (novo.equals(ultimo));

        return novo;
    }

    @Override
    public void move(String motion) throws InvalidMoveException{
        boolean sucesso = false;

        while(!sucesso){
            try{
                super.move(motion);
                sucesso = true;
            } catch(InvalidMoveException e){
                motion = gerarNovoMovimento(motion);
            }
        }
    }
}
