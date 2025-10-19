package com.trabrobotartaruga.robo_tartaruga.Classes;

public class Robo {
    private String cor;
    private int posicaoX;
    private int posicaoY;

    public Robo(String cor) {
        this.cor = cor;
        this.posicaoX = 0;
        this.posicaoY = 0;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    public void mover(String movimento) throws MovimentoInvalidoException {

        switch (movimento.toLowerCase()) {
            case "up":
                posicaoY++;
                break;
            case "down":
                posicaoY--;
                break;
            case "right":
               posicaoX++;
                break;
            case "left":
               posicaoX--;
                break;
            default:
                System.out.println(movimento+" é um movimento inválido!");
                return;
        }

        if(posicaoX<0){
            throw new MovimentoInvalidoException(posicaoX);
        }
        else if(posicaoY<0){
             throw new MovimentoInvalidoException(posicaoY);
        }

        System.out.println("Robô " + cor + " está agora em (" + posicaoX + ", " + posicaoY + ")");
    }

    public void mover(int movimentoI) throws MovimentoInvalidoException{
        switch(movimentoI){
            case 1:
                posicaoY++;
                break;
            case 2:
                posicaoY--;
                break;
            case 3:
                posicaoX++;
                break;
            case 4:
                posicaoX--;
                break;
            default:
                System.out.println("Apenas 1, 2, 3 e 4 são permitidos!");
                 return;
        }

        if(posicaoX<0){
            throw new MovimentoInvalidoException(posicaoX);
        }
        else if(posicaoY<0){
             throw new MovimentoInvalidoException(posicaoY);
        }

        System.out.println("Robô " + cor + " está agora em (" + posicaoX + ", " + posicaoY + ")");

    }

    public boolean verificarAlimentoEncontrado(int x, int y){
        if(posicaoX == x && posicaoY == y){
            return true;
        }
        else{
            return false;
        }
    }
}
