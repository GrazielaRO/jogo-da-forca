package br.com.zup.jogodaforca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Random;
import java.util.Scanner;

public class JogoDaForca {

	public static String incluirPalavras(Scanner sc, int quantidadepalavras, String arquivoForca) throws IOException {

		final String PADRAO_PALAVRA = "[a-zA-Z ]+";
		FileWriter lista = new FileWriter(arquivoForca);
		String palavra = "";

		for (int i = 0; i < quantidadepalavras; i++) {

			do {
				System.out.printf("\nDigite a %dª palavra: ", i + 1);
				palavra = retiraAcento(sc.nextLine());
				
				//inclusão de constante para Regex
				if (!Pattern.matches(PADRAO_PALAVRA, palavra)) {
					System.out
							.println("\nVocê precisa inserir palavras apenas com letras do alfabeto. Tente novamente!");
				}

			} while (!Pattern.matches(PADRAO_PALAVRA, palavra));

			lista.write(palavra.toLowerCase());
			lista.write(String.format("\n"));
		}

		lista.close();

		clearScreen();

		return "\nPalavras incluídas com sucesso!\n";
	}

	public static String sorteioPalavra(String arquivoForca) throws IOException {

		FileReader fReader = new FileReader(arquivoForca);
		BufferedReader bReader = new BufferedReader(fReader);

		int contaLinhas = 0;
		while ((bReader.readLine()) != null) {
			contaLinhas++;
		}

		Random rand = new Random();
		int rangeDeLinhas = contaLinhas;
		int linhaEscolhida = rand.nextInt(rangeDeLinhas);

		String palavraEscolhida = Files.readAllLines(Paths.get(arquivoForca)).get(linhaEscolhida);

		bReader.close();
		fReader.close();

		return palavraEscolhida;
	}

	public static int solicitaQuantidadePalavras(Scanner sc) throws IOException {
		
		final String PADRAO_NUMEROS_INTEIROS = "^[1-9]\\d*$";
		while (true) {
			System.out.print("Peça para um amigo digitar a quantidade de palavras a serem sorteadas " + "\nno jogo: ");
			String numeroDePalavras = sc.nextLine();
			
			//inclusão de constante para Regex
			if (Pattern.matches(PADRAO_NUMEROS_INTEIROS, numeroDePalavras)) {
				return Integer.parseInt(numeroDePalavras);
			} else {
				System.out.println("\nVocê não digitou um número inteiro, tente novamente!\n");
			}
		}
	}

	public static String completaPalavra(String palavraSorteada, String letrasDigitadas) {

		String palavraSecreta = "";
		letrasDigitadas = letrasDigitadas.replaceAll(", ", "");

		for (int i = 0; i < palavraSorteada.length(); i++) {
			if (letrasDigitadas.contains(Character.toString(palavraSorteada.charAt(i)))) {
				palavraSecreta += palavraSorteada.charAt(i) + " ";
			} else {

				if (palavraSorteada.charAt(i) == ' ') {
					palavraSecreta += "  ";
				} else {
					palavraSecreta += "_ ";
				}

			}
		}

		return palavraSecreta;

	}

	public static String incluiPalavraNova(String novaPalavra, String arquivoForca) throws IOException {

		FileWriter fwriter = new FileWriter(arquivoForca, true);

		fwriter.append(retiraAcento(novaPalavra));
		fwriter.append(String.format("\n"));

		fwriter.close();

		return novaPalavra;
	}

	public static void confirmaVitoria(Scanner sc, String arquivoForca, String novaPalavra) throws IOException {
		
		final String PADRAO_PALAVRA = "[a-zA-Z ]+";
		System.out.println(
				"\nParabéns, você GANHOU! Agora você tem direito de " + "adicionar uma nova palavra na nossa lista!");

		do {
			System.out.print("\nPor favor, digite uma palavra: ");
			novaPalavra = sc.next();
			
			// inclusão de constante para Regex
			if (!Pattern.matches(PADRAO_PALAVRA, novaPalavra)) {
				System.out.println("\nVocê precisa inserir palavras apenas com letras do alfabeto. Tente novamente!");
			}
			
			// inclusão de constante para Regex
		} while (!Pattern.matches(PADRAO_PALAVRA, novaPalavra));

		incluiPalavraNova(novaPalavra, arquivoForca);
		System.out.print("\nNova palavra incluída com sucesso!\n\n");
	}

	private static final String CABECALHO_DO_JOGO = ("-------------------------------------------------------------------------------\n")
			+ ("                                 JOGO DA FORCA                                 \n")
			+ ("-------------------------------------------------------------------------------");

	private static final String REGRAS_DO_JOGO = ("\nVamos às REGRAS:\n\n"
			+ "--> Tente adivinhar a palavra dizendo as letras que podem existir dentro dela.\n"
			+ "--> Cada letra acertada será escrita na palavra, errando, você perde 1 chance " + "\nem um total de 6.\n"
			+ "--> O jogo termina quando você ganha completando a palavra ou quando você perde"
			+ "\nutilizando todas as vidas.\n");

	public static String retiraAcento(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static void clearScreen() {

		for (int i = 0; i < 35; i++) {
			System.out.println("");
		}

	}

	public static void main(String[] args) throws IOException {

		final String PADRAO_PALAVRA = "[a-zA-Z]";
		final String PADRAO_LETRA = "[a-zA-Z]+";
		final int LIMITE_DE_ERROS = 6;
		
		Scanner sc = new Scanner(System.in);

		String continuarJogando = "S";
		String letra = "";
		String arquivoForca = "palavras.txt";
		boolean acertou;
		
		// inclusão de constante
		System.out.println(CABECALHO_DO_JOGO);
		
		// inclusão de constante
		System.out.println(REGRAS_DO_JOGO);

		int qtdPalavras = solicitaQuantidadePalavras(sc);

		incluirPalavras(sc, qtdPalavras, arquivoForca);

		while (continuarJogando.toUpperCase().equals("S")) {

			String palavraSorteada = null;
			palavraSorteada = sorteioPalavra(arquivoForca);
			System.out.println("\n\nA palavra escolhida é: " + palavraSorteada.replaceAll(PADRAO_PALAVRA, "_ ").trim());

			int qtdErros = 0;
			String letrasDigitadas = "";
			String novaPalavra = "";
			
			// substituição do limite de erros por uma constante
			while (qtdErros < LIMITE_DE_ERROS) {
				// condição alterada para boolean
				boolean padraoLetraIncorreto = !Pattern.matches(PADRAO_LETRA, letra) || (letra.length() > 1);
				do {
					System.out.print("\nDigite uma letra: ");
					letra = retiraAcento(sc.next().toLowerCase());

					if (padraoLetraIncorreto) {
						System.out.println(
								"\nVocê não pode inserir mais que uma letra e essa letra precisa ser pertencente "
										+ "\nao alfabeto. Tente novamente!");
					}

				} while (padraoLetraIncorreto);
				
				// inclusão de variável boolean para condição
				boolean letraRepetida = letrasDigitadas.contains(letra);
				if (letraRepetida) {
					System.out.println("\nA letra " + letra + " já foi utilizada, tente uma letra diferente!");
					System.out.println("Letras já digitadas: " + letrasDigitadas);
					continue;
				}

				letrasDigitadas += letra + ", ";
				System.out.println("Letras já digitadas: " + letrasDigitadas);

				acertou = palavraSorteada.contains(letra);

				String palavraCompleta = completaPalavra(palavraSorteada, letrasDigitadas);
				System.out.println(palavraCompleta);

				if (acertou) {
					//inclusão de boolean em variável para a condição
					boolean palavraFinalizada = !palavraCompleta.contains("_");
					if (palavraFinalizada) {
						
						//função de confirmar vitória incluída
						confirmaVitoria(sc, arquivoForca, novaPalavra);
						break;
					}
					
				} else {
					
					qtdErros++;
					System.out.println("\nLetra inexitente na palavra!");
					System.out.println("Você errou pela " + qtdErros + "ª vez!");
				}
				
				// inclusão de constante de limite de erros
				if (qtdErros == LIMITE_DE_ERROS) {
					System.out.println("\nQue pena, você PERDEU! =(");
				}

			}
			
			// inclusão de variável boolean para o laço
			boolean opcaoInvalidaParaContinuarJogo = !continuarJogando.toUpperCase().equals("S") && !continuarJogando.toUpperCase().equals("N");
			do {
				System.out.println("\n\nDeseja continuar jogando? (S/N): ");
				continuarJogando = sc.next();

				if (opcaoInvalidaParaContinuarJogo) {
					System.out.println("Opção inválida! Voce precisa escolher S para sim ou N para não.");
				}
			} while (opcaoInvalidaParaContinuarJogo);

		}

		System.out.println("\n\nJogo encerrado! Até a próxima! =)");

		sc.close();
	}

}