package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static Random random = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static int nivelInicial = -1;
    public static int nivelFinal = -1;

    // Lista para armazenar o histórico das partidas
    private static List<Partida> historicoPartidas = new ArrayList<>();

    public static void main(String[] args) {
        inicioJogo();

    }


    public static void inicioJogo() {

        int op = 0;
        try {
        limpaTela();
        digitarTexto(textos.apresentacao, 5);

        digitarTexto(textos.apontador, 200);

            op = sc.nextInt();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        switch (op) {
            case 1:
                novoJogo();
                break;
            case 2:
                historico();
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

    private static void historico() {
        if (historicoPartidas.isEmpty()) {
            System.out.println("Nenhum jogo jogado até o momento.");
        } else {
            System.out.println("Histórico de jogos:");
            for (Partida partida : historicoPartidas) {
                System.out.printf("Nome: %s | Tentativas: %d | Data: %s\n",
                        partida.getNome(),
                        partida.getTentativas(),
                        partida.getData());
            }
        }


    }

    private static void novoJogo() {
        int op = sc.nextInt();
        switch (op) {
            case 1:
                if(nivelInicial == -1){
                    int novoJogo = random.nextInt(textos.novoJogo.length);
                    digitarTexto(textos.novoJogo[novoJogo], novoJogo);

                    digitarTexto(textos.nivelInvalido, 100);
                    configura();


                }
                game(nivelInicial, nivelFinal);
                break;
            case 2:
                break;
            case 0:
                break;

            default:
                break;

        }

    }

    private static void configura() {
        digitarTexto(textos.configValoresInicial, 100);
        nivelInicial = sc.nextInt();
        digitarTexto(textos.configValoresFinal, 100);
        nivelFinal = sc.nextInt();
        digitarTexto(textos.configurado, 100);

    }

    private static void game(int nivelInicial, int nivelFinal) {
        // Gera um número aleatório entre (inclusive) e(exclusive)
        int numeroSorteado = random.nextInt(nivelFinal - nivelInicial) + nivelInicial;
        int tentativas = 0;

        digitarTexto(textos.digiteNumero, 100);
        int op = sc.nextInt();

        while(true){
            tentativas++;
            if(op == numeroSorteado){
                int x = random.nextInt(textos.parabens.length);
                String texto = textos.parabens[x];
                System.out.printf(texto, tentativas);

                System.out.println("Deseja salvar? \n1 - Sim \n 2 - Não");
                int i = sc.nextInt();
                if(i == 1){
                    System.out.println("Qual o seu nome?");
                    String nome = sc.nextLine();
                    sc.nextLine();
                    salvarHistorico(nome, tentativas);
                }else{
                    int indece = random.nextInt(textos.naoSalvar.length);
                    digitarTexto(textos.naoSalvar[indece], indece);
                }

                novoJogo();
            }else{
                digitarTexto(textos.analisando, 150);
            }
            digitarTexto(textos.apontador, 100);
            op = sc.nextInt();
        }
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

    private static void salvarHistorico(String nome, int tentativas) {
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        historicoPartidas.add(new Partida(nome, tentativas, dataAtual));
        inicioJogo();
    }
    public class textos {

        public static final String apresentacao = """
                
                                                ==============================================
                                                |                                             |
                                                |        Bem-vindo ao Jogo da Adivinhação!    |
                                                |      Será que você vai conseguir ganhar?    |
                                                |                                             |
                                                ==============================================
                                               \s
                                                                MENU:
                                                ----------------------------------------------
                                                |  1 - Novo Jogo                              |
                                                |  2 - Histórico                              |
                                                |  3 - HARD                                   |
                                                |                                             |
                                                |  0 - Sair                                   |
                                                ----------------------------------------------
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
        public static final String novaPartida = """
                ======================================
                |                                    |
                |           MENU PRINCIPAL           |
                |                                    |
                |  1 - Iniciar                       |
                |  2 - Configurar a dificuldade      |
                |  0 - Voltar                        |
                |                                    |
                ======================================
                """;
        public static final String apontador = """
                     >>>>> 
                
                """;
        public static final String nivelInvalido = """
                Foi detectado um novo jogo, temos que configurar os limites!
                """;
        public static final String digiteNumero = """
                Digite o seu numero
                """;
        public static final String[] parabens ={
                "Parabéns! Você acertou em %d tentativas!",
                "Mandou bem! Acertou o número em %d tentativas!",
                "Isso aí! Você é um mestre adivinhador! Só precisou de %d tentativas.",
                "Sensacional! Você tem um sexto sentido para números! Acertou em %d tentativas!",
                "Uau! Que incrível! Você acertou em apenas %d tentativas!",
                "Você é demais! Acertou em cheio em %d tentativas!",
                "Que sorte a sua! Brincadeira, você é bom mesmo! Acertou em %d tentativas!",
                "Palmas para você! Acertou o número em %d tentativas!",
                "Você me impressionou! Parabéns por acertar em %d tentativas!",
                "Incrível! Você é um gênio dos números! Descobriu em %d tentativas!"}
                ;
        public static final String analisando = "Analisando.... Errou, tenta Novamente";
        public static final String configValoresInicial = "Digite o numero minimo que deseja tentar";
        public static final String configValoresFinal = "Digite o numero maximo que deseja tentar";
        public static final String configurado = "O sistema foi configurado, pode iniciar o seu jogo ou sair";
        public static final String[] naoSalvar = {"Tudo bem",
                "Tudo bem, você não é digno de estar em meu sistema",
                "Você que sabe"};
    }
    public static class Partida {
        private String nome;
        private int tentativas;
        private String data;

        public Partida(String nome, int tentativas, String data) {
            this.nome = nome;
            this.tentativas = tentativas;
            this.data = data;
        }

        public String getNome() {
            return nome;
        }

        public int getTentativas() {
            return tentativas;
        }

        public String getData() {
            return data;
        }
    }


}