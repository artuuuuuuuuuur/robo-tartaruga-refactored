package com.trabrobotartaruga.robo_tartaruga.Classes;

public class MovimentoInvalidoException extends Exception{
    public String toString(){
        return "Movimento inválido";
    }
    public MovimentoInvalidoException(int a){
        super("O movimento é inválido pois o número "+ a +" é negativo!");
    }
}
