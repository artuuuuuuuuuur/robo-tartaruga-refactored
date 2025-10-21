package com.trabrobotartaruga.robo_tartaruga.classes;
import java.util.Scanner;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class MainR2 {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite uma cor para o robô 1:");
        String cor1 = sc.nextLine();
        Bot robo1 = new Bot(cor1);
        
        System.out.println("Digite outra cor para o robô 2:");
        String cor2 = sc.nextLine();
        Bot robo2 = new Bot(cor2);

        System.out.println("Digite as coordenadas do alimento:");
        System.out.print("X: ");
        int x = sc.nextInt();
        System.out.print("\nY: ");
        int y = sc.nextInt();
        sc.nextLine();
        
        int quant_val1 =0;
        int quant_val2 =0;
        int quant_inval1 =0;
        int quant_inval2 =0;

        do{
            try{
                System.out.println("Digite o movimento do robô 1: ");
                String movimento = sc.nextLine();
                robo1.mover(movimento);
                if(robo1.getPosicaoX() >= 0 && robo1.getPosicaoX() <= 4 && robo1.getPosicaoY() >= 0 && robo1.getPosicaoY() <= 4){
                    quant_val1++;
                }
            }catch(InvalidMoveException e1){
                e1.mensagemDeErro();
                quant_inval1++;
            }

            try{
                System.out.println("Digite o movimento do robô 2: ");
                String movimento2 = sc.nextLine();
                robo2.mover(movimento2);
                if(robo2.getPosicaoX() >= 0 && robo2.getPosicaoX() <= 4 && robo2.getPosicaoY() >= 0 && robo2.getPosicaoY() <= 4){
                    quant_val2++;
                }
            }catch(InvalidMoveException e2){
                e2.mensagemDeErro();
                quant_inval2++;
            }

        }while(robo1.verificarAlimentoEncontrado(x, y) == true || robo2.verificarAlimentoEncontrado(x, y) == true);
        
        if(robo1.verificarAlimentoEncontrado(x, y) == true){
            System.out.println("O robô "+ robo1.getCor()+" encontrou o alimento!");
            System.out.println(quant_val1+" movimentos válidos e "+ quant_inval1+" movimentos inválidos!");
        }

        if(robo2.verificarAlimentoEncontrado(x, y) == true){
            System.out.println("O robô "+ robo2.getCor()+" encontrou o alimento!");
            System.out.println(quant_val2+" movimentos válidos e "+ quant_inval2+" movimentos inválidos!");
        }

        sc.close();
    }
}