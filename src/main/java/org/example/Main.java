package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Random random = new Random();

    public static void main(String[] args) {
        inicioJogo();

    }


    public static void inicioJogo() {
        int op = 0;
        limpaTela();
        digitarTexto(textos.apresentacao, 10);


        Scanner sc = new Scanner(System.in);

        try {
            op = sc.nextInt();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        switch (op) {
            case 1:
                novoJogo();
                break;
            case 2:
                //  historico();
                break;
            case 3:
                //hard();
                break;
            case 0:
                despedida();
                break;
            default:
                break;
        }
    }

    private static void novoJogo() {
        int novoJogo = random.nextInt(textos.novoJogo.length);
        digitarTexto(textos.novoJogo[novoJogo], 80);


    }

    private static void despedida() {
        int indiceDespedida = random.nextInt(textos.despedida.length); // Variável renomeada
        digitarTexto(textos.despedida[indiceDespedida], 80);
    }

    public static void limpaTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void digitarTexto(String texto, int intervalo) {
        for (char caractere : texto.toCharArray()) {
            System.out.print(caractere);  // Imprime cada caractere sem pular linha
            try {
                Thread.sleep(intervalo);  // Pausa o programa por 'intervalo' milissegundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restaura o estado interrompido da thread
            }
        }
        System.out.println();  // Quebra a linha no final do texto
    }

    public class textos {

        public static final String apresentacao = """
                Bem vindo ao jogo da adivinhação.
                Sera que você vai conseguir ganhar?
                
                ------------------------------------
                MENU:
                1 - Novo Jogo
                2 - Historico
                3 - HARD
                
                0- Sair
                
                """;
        public static final String[] despedida = {
                "Foi um jogo legal!",
                "Até a próxima!",
                "Foi até legal jogar com você",
                "Nos vemos na próxima partida!",
                "Obrigado por jogar!",
                "Te vejo em outra oportunidade!",
                "Espero que tenha se divertido!",
                "Boa sorte nos próximos jogos!",
                "Continue praticando!",
                "Parabéns pela partida!"
        };
        public static final String[] novoJogo = {
                "Você me desafia?",
                "Hahaha ta bom, vamos lá!",
                "Acho que te conheço...",
                "Prepare-se para perder!",
                "Que os jogos comecem!",
                "Estou pronto para te vencer!",
                "Será que você consegue me superar?",
                "Vamos ver quem é o mestre da adivinhação!",
                "Traga seu melhor jogo!",
                "Espero que esteja preparado para uma batalha épica!"
        };
    }
}