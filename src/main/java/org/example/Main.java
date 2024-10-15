package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    /*
     * Variáveis globais usadas no jogo.
     * random: Usada para gerar números aleatórios.
     * sc: Usada para capturar a entrada do usuário pelo terminal.
     * nivelInicial e nivelFinal: Definem o intervalo de números para o jogo.
     */
    public static Random random = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static int nivelInicial = -1;
    public static int nivelFinal = -1;

    /*
    Lista para armazenar o histórico das partidas
     */
    private static List<Partida> historicoPartidas = new ArrayList<>();


    /*
     * Ponto de entrada do programa, que inicia o jogo chamando o método inicioJogo().
     */
    public static void main(String[] args) {
        inicioJogo();
    }

    /*
     * Exibe o menu inicial do jogo e aguarda a escolha do jogador.
     */
    public static void inicioJogo() {
        int op = 0;
        try {
            limpaTela(); //faz a limpeza da tela do terminal caso tiver algo antes
            digitarTexto(textos.apresentacao, 0); // insere o texto e a velocidade que é exebida
            digitarTexto(textos.apontador, 200);// insere o texto e a velocidade que é exebida
            op = sc.nextInt(); // Captura a opção escolhida pelo usuário.
        } catch (InputMismatchException e) {

            // Tratamento para entradas inválidas.
            System.out.println(textos.ANSI_RED + "Erro: Entrada inválida. Por favor, insira um número." + textos.ANSI_RESET);
            sc.nextLine();  // Limpa o buffer para evitar loops infinitos
            inicioJogo();  // Reinicia o menu
            return;
        }

        // Executa a ação baseada na escolha do usuário.
        switch (op) {
            case 1:
                novoJogo();
                break;
            case 2:
                historico();
                break;
            case 69:
                gameSecreto();
                break;
            case 0:
                despedida();
                break;
            default:
                System.out.println(textos.ANSI_RED + "Opção inválida." + textos.ANSI_RESET);
                inicioJogo();
        }
    }

    /*
     * Inicia um novo jogo, configurando os limites e chamando a lógica principal do jogo.
     */
    private static void novoJogo() {
        limpaTela();
        digitarTexto(textos.definirLimites, 80);
        configura();
        game(nivelInicial, nivelFinal); // Inicia o jogo com os limites configurados


    }

    /*
     * Inicia o jogo secreto, que neste caso é o Jogo da Forca.
     */
    private static void gameSecreto() {
        limpaTela();
        digitarTexto(textos.gameSecreto, 100);
        jogoDaForca();
    }

    /*
     * Configura os limites do jogo principal, definindo o valor inicial e final.
     */
    private static void configura() {
        try {
            digitarTexto(textos.configValoresInicial, 80); //Solicita o valor minimo do intervalo
            nivelInicial = sc.nextInt();
            digitarTexto(textos.configValoresFinal, 80); //Solicita o valor maximo do intervalo
            nivelFinal = sc.nextInt();

            // Verifica se o valor inicial é maior ou igual ao valor final.

            if (nivelInicial >= nivelFinal) {
                throw new IllegalArgumentException(textos.ANSI_RED + "O valor inicial deve ser menor que o valor final." + textos.ANSI_RESET);
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

    /*
     * Logica do jogo principal
     * */

    private static void game(int nivelInicial, int nivelFinal) {
        // Sorteia um número dentro dos limites definidos.
        limpaTela();
        int numeroSorteado = random.nextInt(nivelFinal - nivelInicial) + nivelInicial;
        int tentativas = 0;
        String opString = "";

        try {
            // Loop do jogo, continua até o jogador acertar ou digitar "sair".
            sc.nextLine(); // limpar a memoria
            while (true) {
                pausa();
                 limpaTela();

                System.out.println("Digite seu palpite ou digite '" + textos.ANSI_RED + "sair" + textos.ANSI_RESET + "' para encerrar:");
                opString = sc.nextLine().toLowerCase();

                // Verifica se o jogador deseja sair.
                if (opString.equals("sair")) {
                    System.out.println(textos.ANSI_YELLOW + "Voltando...." + textos.ANSI_RESET);
                    despedida();  // Metodo para encerrar o jogo ou retornar ao menu.
                    inicioJogo();  // Encerra o metodo e retorna ao ponto de chamada.
                }

                // Converte a entrada do jogador para númer.
                int op;
                try {
                    op = Integer.parseInt(opString);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: Entrada inválida. Insira um número válido ou " + textos.ANSI_RED + "sair" + textos.ANSI_RESET + ".");
                    continue;  // Reinicia o loop, pedindo uma nova entrada.
                }

                tentativas++;

                // Verifica se o jogador acertou o número sorteado.
                if (op == numeroSorteado) {
                    int x = random.nextInt(textos.parabens.length);
                    String texto = String.format(textos.parabens[x], tentativas);
                    System.out.println(texto);

                    System.out.println("Deseja salvar? \n1 - " + textos.ANSI_GREEN + "Sim" + textos.ANSI_RESET + "\n 2 - " + textos.ANSI_RED + "Não" + textos.ANSI_RESET);
                    int i = sc.nextInt();

                    if (i == 1) {
                        // Se o jogador escolhe salvar, captura o nome e salva o resultado.
                        System.out.println("Qual o seu nome?");
                        sc.nextLine();  // Limpa o buffer
                        String nome = sc.nextLine();
                        salvarHistorico(nome, tentativas);  // Salva o histórico da partida.
                    } else {
                        int indice = random.nextInt(textos.naoSalvar.length);
                        digitarTexto(textos.naoSalvar[indice], indice);
                    }

                    novoJogo();  // Pergunta se o jogador quer iniciar um novo jogo.
                    return;
                } else {
                    int x = random.nextInt(textos.analisando.length);
                    System.out.println(textos.analisando[x]);

                    if (op < numeroSorteado) {
                        x = random.nextInt(textos.chuteMenor.length);
                        System.out.printf(textos.ANSI_RED + textos.chuteMenor[x] + textos.ANSI_RESET + "\n", op);
                    } else {
                        x = random.nextInt(textos.chuteMaior.length);
                        System.out.printf(textos.ANSI_RED + textos.chuteMaior[x] + textos.ANSI_RESET + "\n", op);
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println(textos.ANSI_RED + "Erro: Entrada inválida. Insira um número." + textos.ANSI_RESET);
            sc.nextLine();  // Limpa o buffer
            game(nivelInicial, nivelFinal);  // Reinicia o jogo.
        }
    }


    /*
     * Exibe uma mensagem de despedida e encerra o jogo.
     */
    private static void despedida() {
        int indiceDespedida = random.nextInt(textos.despedida.length); // Variável renomeada
        digitarTexto(textos.despedida[indiceDespedida], 80);
    }

    /*
     * Salva o resultado da partida no histórico.
     */
    private static void salvarHistorico(String nome, int tentativas) {
        try {
            String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            historicoPartidas.add(new Partida(nome, tentativas, dataAtual));
            inicioJogo();
        } catch (Exception e) {
            System.out.println(textos.ANSI_RED + "Erro ao salvar o histórico: " + textos.ANSI_RESET + e.getMessage());
        }
    }


    /*
     * Lógica do jogo da forca.
     */
    private static void jogoDaForca() {
        int indicePalavraSecreta = random.nextInt(textos.palavraSecreta.length);
        String palavraSorteada = textos.palavraSecreta[indicePalavraSecreta];
        Set<Character> letrasTentadas = new HashSet<>(); // Armazena as letras já tentadas
        int tentativasRestantes = 6;
        char[] palavraOculta = new char[palavraSorteada.length()]; // Exibe a palavra com letras ocultas.
        Arrays.fill(palavraOculta, '_'); // Preenche a palavra oculta com '_'.

        // Loop do jogo da forca, continua até acabar as tentativas ou acertar a palavra.
        while (tentativasRestantes > 0 && new String(palavraOculta).contains("_")) {


            limpaTela();
            System.out.println(textos.ANSI_GREEN + "Dica: " + textos.ANSI_YELLOW + "Termos relacionado a programação" + textos.ANSI_RESET);
            System.out.println("  +---+");
            System.out.println("  |   |");
            System.out.println("  |   " + (tentativasRestantes < 6 ? 'O' : ' '));
            System.out.println("  |  " + (tentativasRestantes < 4 ? '\\' : ' ') + (tentativasRestantes < 5 ? '|' : ' ') + (tentativasRestantes < 3 ? '/' : ' '));
            System.out.println("  |  " + (tentativasRestantes < 2 ? '|' : ' '));
            System.out.println("  |  " + (tentativasRestantes < 1 ? "/ \\" : ' '));
            System.out.println(" -+");

            System.out.println("Palavra: " + new String(palavraOculta));
            System.out.println("Tentativas restantes: " + textos.ANSI_RED + tentativasRestantes + textos.ANSI_RESET);
            System.out.print("Digite uma letra: ");
            System.out.println("\n");
            char tentativa = sc.next().toLowerCase().charAt(0);

            // Verifica se a letra já foi tentada.
            if (!Character.isLetter(tentativa)) {
                System.out.println(textos.ANSI_RED + "Por favor, digite uma letra válida." + textos.ANSI_RESET);
                continue;
            }

            if (letrasTentadas.contains(tentativa)) {
                System.out.println(textos.ANSI_RED + "Você já tentou essa letra antes." + textos.ANSI_RESET);
                continue;
            }

            letrasTentadas.add(tentativa);// Adiciona a letra tentada ao conjunto.

            // Verifica se a letra está presente na palavra sorteada.
            if (palavraSorteada.contains(String.valueOf(tentativa))) {
                for (int i = 0; i < palavraSorteada.length(); i++) {
                    if (palavraSorteada.charAt(i) == tentativa) {
                        palavraOculta[i] = tentativa;
                    }
                }
                System.out.println("Bom trabalho! A letra '" + textos.ANSI_BLUE + tentativa + textos.ANSI_RESET + "' está na palavra.");
            } else {
                tentativasRestantes--;
                System.out.println("Que pena! A letra '" + textos.ANSI_RED + tentativa + textos.ANSI_RESET + "' não está na palavra.");
            }
        }

        if (tentativasRestantes > 0) {
            // Se o jogador acertou a palavra completa.
            System.out.println("Parabéns! Você acertou a palavra: " + textos.ANSI_BLUE + palavraSorteada + textos.ANSI_RESET);
        } else {
            // Se o jogador esgotar as tentativas.
            limpaTela();
            // Se o jogador esgotar as tentativas.
            System.out.println("  +---+");
            System.out.println("  |   |");
            System.out.println("  |   O");
            System.out.println("  |  \\|/");
            System.out.println("  |   |");
            System.out.println("  |  / \\");
            System.out.println(" -+"); // <-- Movida para dentro do else
            System.out.println("Fim de jogo! A palavra era: " + textos.ANSI_YELLOW + palavraSorteada + textos.ANSI_RESET);

        }

        System.out.println("Deseja jogar novamente? (1 - " + textos.ANSI_BLUE + "Sim" + textos.ANSI_RESET + " / 2 - " + textos.ANSI_RED + "Não" + textos.ANSI_RESET + ")");
        int escolha = sc.nextInt();
        if (escolha == 1) {
            jogoDaForca();
        } else {
            inicioJogo();
        }
    }


    /*
     * Salva o resultado da partida do jogo da adivinhação
     * */
    private static void historico() {

        if (!historicoPartidas.isEmpty()) {
            System.out.println(textos.ANSI_YELLOW + "Histórico de jogos:" + textos.ANSI_RESET);
            for (Partida partida : historicoPartidas) {
                System.out.printf("Nome: %s | Tentativas: %d | Data: %s\n",
                        partida.getNome(),
                        partida.getTentativas(),
                        partida.getData());
            }
            inicioJogo();
        }
        System.out.println(textos.ANSI_RED + "Nenhum jogo jogado até o momento." + textos.ANSI_RESET);
        pausa();
        inicioJogo();

    }

    /*
     * UTILITARIOS
     * */

    public static void limpaTela() {
        try {
            // Verifica o sistema operacional
            String sistema = System.getProperty("os.name");

            if (sistema.contains("Windows")) {
                // Executa o comando 'cls' no Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Executa o comando 'clear' no Linux e MacOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Em caso de erro, exibe a mensagem
            System.out.println("Erro ao limpar o terminal: " + e.getMessage());
        }
    }


    /*
     * Função para dar efeito de escrita
     * */
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


    /*
     * Biblioteca de textos, aparti daqui posso adicinar mais coisas sem nescessidade de alterar o codigo
     * */
    public static class textos {

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
                |  ? - Game Secreto                           |
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
        public static final String apontador = """
                >>>>>\s
                """;
        public static final String definirLimites = """
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
        public static final String gameSecreto = """
                Meus parabens voce achou um game secreto, o jogo a seguir sera o jogo da forca
                """;
        public static final String[] palavraSecreta = {"programacao", "desenvolvedor", "algoritmo", "compilador", "sintaxe",
                "objeto", "classe", "heranca", "encapsulamento", "abstracao",
                "polimorfismo", "interface", "metodo", "atributo", "instancia",
                "variavel", "constante", "biblioteca", "framework", "estrutura"};
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";

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