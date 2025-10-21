package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.Scanner;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class MainR1 {
    public static void main(String argrs[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite uma cor para o robô:");
        String cor = sc.nextLine();
        Bot robo = new Bot(cor);
        System.out.println("Digite as coordenadas do alimento:");
        System.out.print("X: ");
        int x = sc.nextInt();
        System.out.print("\nY: ");
        int y = sc.nextInt();
        sc.nextLine();
        
        while (robo.verificarAlimentoEncontrado(x, y) == false) {
            System.out.println("Digite o movimento: ");
            String movimento = sc.nextLine();
            try{
                robo.mover(movimento);
            }catch(InvalidMoveException e){
                e.mensagemDeErro();
            }
            
        }
        System.out.println("Alimento encontrado!");
        sc.close();
    }
}