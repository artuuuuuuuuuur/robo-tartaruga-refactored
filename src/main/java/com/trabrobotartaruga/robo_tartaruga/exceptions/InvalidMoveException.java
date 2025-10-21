package com.trabrobotartaruga.robo_tartaruga.exceptions;


public class InvalidMoveException extends Exception{
    @Override
    public String toString(){
        return "O movimento é inválido pois está em uma posição fora da matriz!";
    }
    
    public void mensagemDeErro(){
        System.out.println("O movimento é inválido pois está em uma posição fora da matriz!");
    }
}
