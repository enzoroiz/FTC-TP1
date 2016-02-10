import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LeitorArquivo {
	/**
	 * Constantes para facilitar legibilidade do código
	 */
	private final static int ESTADOS_AFNE = 0;
	private final static int ALFABETO = 1;
	private final static int TRANSICOES = 2;
	private final static int ESTADOS_INICIAIS = 3;
	private final static int ESTADOS_FINAIS = 4;
	private final static int PALAVRAS = 5;
	
	private String entrada;
	
	/**
	 * Construtor informando o arquivo de entrada de onde serao lidas as informações do AFN
	 * @param saida
	 */
	public LeitorArquivo(String entrada) {
		this.entrada = entrada;
	}

	/**
	 * Lê as informações de um AFNE a partir de um arquivo de entrada
	 * @return AFNE
	 */
	public AFN lerArquivo() {
		//Variáveis necessárias
		ArrayList<String> estadosAFNE = new ArrayList<>();
		ArrayList<String> alfabeto = new ArrayList<>();
		ArrayList<String> estadosIniciais = new ArrayList<>();
		ArrayList<String> estadosFinais = new ArrayList<>();
		ArrayList<String> palavras = new ArrayList<>();
		ArrayList<ArrayList<String>> transicoes = new ArrayList<>();

		int tipoLeitura;
		String token, estadoInicial, estadoFinal, palavra;

		// Arquivo de entrada
		File file = new File(entrada);

		try {

			Scanner scannerFile = new Scanner(file);
			Scanner scannerLine = null;

			tipoLeitura = ESTADOS_AFNE;

			//Enquanto houver linhas
			while (scannerFile.hasNextLine()) {
				scannerLine = new Scanner(scannerFile.nextLine());

				// Para cada tipo de informação lida as armazena nas variáveis dedicadas
				// Muda o tipo de leitura incrementando a variável que verifica o tipo de leitura corrente
				// Apenas para as transicoes não muda o tipo de leitura ao terminar a linha
				switch (tipoLeitura) {
				case ESTADOS_AFNE:
					while (scannerLine.hasNext()) { 
						token = scannerLine.next();
						if (fimDaLinha(token)) {
							tipoLeitura++;
							break;
						}
						estadosAFNE.add(token);
					}

					break;

				case ALFABETO:
					while (scannerLine.hasNext()) {
						token = scannerLine.next();
						if (fimDaLinha(token)) {
							tipoLeitura++;
							break;
						}
						alfabeto.add(token);
					}

					break;

				case TRANSICOES:
					if (scannerLine.hasNext()) {
						estadoInicial = scannerLine.next();
						if (estadoInicial.equals(";")) {
							tipoLeitura++;
							break;
						}
						estadoFinal = scannerLine.next();

						palavra = scannerLine.next();

						ArrayList<String> transicao = new ArrayList<String>();
						transicao.add(estadoInicial);
						transicao.add(estadoFinal);
						transicao.add(palavra);

						transicoes.add(transicao);
					}
					break;

				case ESTADOS_INICIAIS:
					while (scannerLine.hasNext()) {
						token = scannerLine.next();
						if (fimDaLinha(token)) {
							tipoLeitura++;
							break;
						}
						estadosIniciais.add(token);
					}

					break;

				case ESTADOS_FINAIS:
					while (scannerLine.hasNext()) {
						token = scannerLine.next();
						if (fimDaLinha(token)) {
							tipoLeitura++;
							break;
						}
						estadosFinais.add(token);
					}

					break;

				case PALAVRAS:
					while (scannerLine.hasNext()) {
						token = scannerLine.next();
						if (fimDaLinha(token)) {
							break;
						}
						palavras.add(token);
					}

					break;

				default:
					break;
				}
			}

			// Fecha "scanners" usados para ler o arquivo
			scannerFile.close();
			scannerLine.close();

			// Cria um novo AFN e o retorna
			AFN afne = new AFN(estadosAFNE, alfabeto, estadosIniciais,
					estadosFinais, palavras, transicoes);

			return afne;
		} catch (Exception e) {
			System.out.println("Arquivo de entrada não encontrado ou arquivo de entrada fora do padrão especificado");
		}

		//Caso dê algum erro
		return null;
	}

	/**
	 * Recebe uma string e verifica se é " ; " => final da linha.
	 * @param token
	 * @return true se for o fim da linha, falso, caso contrário
	 */
	private static Boolean fimDaLinha(String token) {
		if (token.equals(";")) {
			return true;
		}
		return false;
	}
}
