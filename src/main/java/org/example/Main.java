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
            digitarTexto(textos.apresentacao, 0);
            digitarTexto(textos.apontador, 200);
            op = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida. Por favor, insira um número.");
            sc.nextLine();  // Limpa o buffer para evitar loops infinitos
            inicioJogo();  // Reinicia o menu
            return;
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
                System.out.println("Opção inválida.");
                inicioJogo();
        }
    }

    private static void historico() {
        if (historicoPartidas.isEmpty()) {
            System.out.println("Nenhum jogo jogado até o momento.");
            pausa();
            inicioJogo();
        } else {
            System.out.println("Histórico de jogos:");
            for (Partida partida : historicoPartidas) {
                System.out.printf("Nome: %s | Tentativas: %d | Data: %s\n",
                        partida.getNome(),
                        partida.getTentativas(),
                        partida.getData());
            }
            System.out.println("Preciona enter para continuar....");
            sc.nextLine();
            inicioJogo();
        }


    }

    private static void novoJogo() {
        int op = sc.nextInt();

        if (nivelInicial == -1) {
            int novoJogo = random.nextInt(textos.novoJogo.length);
            digitarTexto(textos.novoJogo[novoJogo], novoJogo);

            digitarTexto(textos.nivelInvalido, 100);
            configura();

        }
        game(nivelInicial, nivelFinal);


    }

    private static void configura() {
        try {
            digitarTexto(textos.configValoresInicial, 100);
            nivelInicial = sc.nextInt();
            digitarTexto(textos.configValoresFinal, 100);
            nivelFinal = sc.nextInt();

            if (nivelInicial >= nivelFinal) {
                throw new IllegalArgumentException("O valor inicial deve ser menor que o valor final.");
            }

            digitarTexto(textos.configurado, 100);
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida. Insira um número válido.");
            sc.nextLine();  // Limpa o buffer
            configura();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            configura();
        }
    }

    private static void game(int nivelInicial, int nivelFinal) {
        int numeroSorteado = random.nextInt(nivelFinal - nivelInicial) + nivelInicial;
        int tentativas = 0;

        try {
            digitarTexto(textos.digiteNumero, 100);
            int op = sc.nextInt();

            while (true) {
                tentativas++;
                if (op == numeroSorteado) {
                    int x = random.nextInt(textos.parabens.length);
                    String texto = String.format(textos.parabens[x], tentativas);
                    System.out.println(texto);

                    System.out.println("Deseja salvar? \n1 - Sim \n 2 - Não");
                    int i = sc.nextInt();

                    if (i == 1) {
                        System.out.println("Qual o seu nome?");
                        sc.nextLine();  // Limpa o buffer
                        String nome = sc.nextLine();
                        salvarHistorico(nome, tentativas);
                    } else {
                        int indice = random.nextInt(textos.naoSalvar.length);
                        digitarTexto(textos.naoSalvar[indice], indice);
                    }

                    novoJogo();
                } else {

                    int x = random.nextInt(textos.analisando.length);
                    System.out.print(textos.analisando[x]);

                    if (op < numeroSorteado) {
                        x = random.nextInt(textos.chuteMenor.length);
                        System.out.printf(textos.chuteMenor[x] + "\n", op);
                    } else {
                        x = random.nextInt(textos.chuteMaior.length);
                        System.out.printf(textos.chuteMaior[x] + "\n", op);
                    }
                }

                digitarTexto(textos.apontador, 100);
                op = sc.nextInt();
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida. Insira um número.");
            sc.nextLine();  // Limpa o buffer
            game(nivelInicial, nivelFinal);  // Reinicia o jogo
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
        try {
            String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            historicoPartidas.add(new Partida(nome, tentativas, dataAtual));
            inicioJogo();
        } catch (Exception e) {
            System.out.println("Erro ao salvar o histórico: " + e.getMessage());
        }
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
        public static final String[] parabens = {
                "Parabéns! Você acertou em %d tentativas!",
                "Mandou bem! Acertou o número em %d tentativas!",
                "Isso aí! Você é um mestre adivinhador! Só precisou de %d tentativas.",
                "Sensacional! Você tem um sexto sentido para números! Acertou em %d tentativas!",
                "Uau! Que incrível! Você acertou em apenas %d tentativas!",
                "Você é demais! Acertou em cheio em %d tentativas!",
                "Que sorte a sua! Brincadeira, você é bom mesmo! Acertou em %d tentativas!",
                "Palmas para você! Acertou o número em %d tentativas!",
                "Você me impressionou! Parabéns por acertar em %d tentativas!",
                "Incrível! Você é um gênio dos números! Descobriu em %d tentativas!"};
        public static final String[] analisando = {
                "Analisando... Errou, tente novamente.",
                "Verificando... Não foi dessa vez, tente de novo.",
                "Analisando... Opa, número incorreto! Tente outra vez.",
                "Checando... Erro! Tente novamente.",
                "Processando... Não foi o número certo. Tente de novo.",
                "Analisando... Errou, continue tentando!",
                "Verificando... O número está errado. Tente novamente.",
                "Checando... Número incorreto, tente mais uma vez.",
                "Processando... Que pena, você errou. Tente outra vez.",
                "Analisando... Infelizmente, errou. Tente novamente."};
        public static final String configValoresInicial = "Digite o numero minimo que deseja tentar";
        public static final String configValoresFinal = "Digite o numero maximo que deseja tentar";
        public static final String configurado = "O sistema foi configurado, pode iniciar o seu jogo ou sair";
        public static final String[] naoSalvar = {
                "Tudo bem",
                "Tudo bem, você não é digno de estar em meu sistema",
                "Você que sabe"};
        public static final String[] chuteMenor = {
                "O valor %s é menor que o número secreto.",
                "O número %s é baixo demais, tente um maior.",
                "Você chutou %s, e está abaixo do número correto.",
                "O valor %s não é suficiente, tente um número maior.",
                "O número %s está menor que o número secreto.",
                "A escolha %s é pequena, tente algo maior.",
                "O chute %s não alcança o número secreto.",
                "O valor %s é inferior ao número correto.",
                "Você chutou %s, e o número é menor do que o necessário.",
                "O número %s está muito abaixo do número secreto."};

        public static final String[] chuteMaior = {
                "O valor %s é maior que o número secreto.",
                "O número %s é muito alto, tente algo menor.",
                "Você chutou %s, e está acima do número correto.",
                "O valor %s é maior do que o necessário, tente um número menor.",
                "O número %s está acima do número secreto.",
                "A escolha %s é grande demais, tente algo menor.",
                "O chute %s ultrapassa o número secreto.",
                "O valor %s é superior ao número correto.",
                "Você chutou %s, e o número é maior do que o esperado.",
                "O número %s está muito acima do número secreto."};
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

    public static void pausa() {
        System.out.println("Pressione Enter para continuar...");
        sc.nextLine();
        sc.nextLine();
    }

}