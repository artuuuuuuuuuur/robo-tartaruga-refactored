package com.trabrobotartaruga.robo_tartaruga.classes;

import java.util.Scanner;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

public class Main1R {
    public static void main(String argrs[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite uma cor para o robô:");
        String cor = sc.nextLine();
        Bot robo = new Bot(cor);

        System.out.println("Digite as coordenadas do alimento:");
        System.out.println("X (De 0 a 3): ");
        int x = sc.nextInt();

        if(x > 3 || x<0){
            while (x > 3 || x < 0) {
                System.out.println("Digite o X novamente: ");
                x = sc.nextInt();
            }
        }

        System.out.println("Y (De 0 a 3): ");
        int y = sc.nextInt();

        if(y > 3 || y<0){
            while (y > 3 || y < 0) {
                System.out.println("Digite o Y novamente: ");
                y = sc.nextInt();
            }
        }

        sc.nextLine();
        
        while (robo.verificarAlimentoEncontrado(x, y) == false) {
            System.out.println("Digite o movimento: ");
            String movimento = sc.nextLine();
            try{
                robo.move(movimento);
            }catch(InvalidMoveException e){
                e.error();
            }
            
        }
        System.out.println("Alimento encontrado!");
        sc.close();
    }
}