package com.trabrobotartaruga.robo_tartaruga.Classes;

public class Robo {
    String cor;
    private int posicaoX;
    private int posicaoY;

    public Robo(String cor){
        this.cor=cor;
        posicaoX=0;
        posicaoY=0;
    }

    public int getPosicaoX(){
        return posicaoX;
    }
    public void setPosicaoX(int posicaoX){
        this.posicaoX=posicaoX;
    }

    public int getPosicaoY(){
        return posicaoY;
    }
    public void setPosicaoY(int posicaoY){
        this.posicaoY=posicaoY;
    }

    public void mover(String movimento) throws MovimentoInvalidoException{
        if(movimento.equalsIgnoreCase("up")){
            posicaoY+= 1;
        }

        else if(movimento.equalsIgnoreCase("")){

        }

        else if(movimento.equalsIgnoreCase("")){

        }

        else if(movimento.equalsIgnoreCase("")){

        }
    }
}
