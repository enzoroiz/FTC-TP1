import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class AFN {
	/**
	 * Constantes para facilitar legibilidade do código
	 */
	private final int ESTADO_INICIAL = 0;
	private final int ESTADO_FINAL = 1;
	private final int SIMBOLO = 2;
	private final int INDICE_INVALIDO = -1;
	private final String PALAVRA_VAZIA = "v";

	/**
	 * Atributos de um AFN Obs.: As palavras são salvas como atributo de um AFN
	 */
	public ArrayList<String> estadosAFNE;
	public ArrayList<String> alfabeto;
	public ArrayList<String> estadosIniciais;
	public ArrayList<String> estadosFinais;
	public ArrayList<String> palavras;
	public ArrayList<ArrayList<String>> transicoes;

	/**
	 * Construtor da classe AFN
	 * 
	 * @param estadosAFNE
	 * @param alfabeto
	 * @param estadosIniciais
	 * @param estadosFinais
	 * @param palavras
	 * @param transicoes
	 */
	public AFN(ArrayList<String> estadosAFNE, ArrayList<String> alfabeto,
			ArrayList<String> estadosIniciais, ArrayList<String> estadosFinais,
			ArrayList<String> palavras, ArrayList<ArrayList<String>> transicoes) {
		this.estadosAFNE = estadosAFNE;
		this.alfabeto = alfabeto;
		this.estadosIniciais = estadosIniciais;
		this.estadosFinais = estadosFinais;
		this.palavras = palavras;
		this.transicoes = transicoes;
	}

	/**
	 * Remove transições sobre palavras, criando novos estados e realizando
	 * transições sobre símbolos
	 */
	public void deAFNEParaAFN() {
		ArrayList<String> transicaoAtual = new ArrayList<String>();
		ArrayList<String> estadosNovos = new ArrayList<String>();

		// Para cada transição
		for (int i = 0; i < transicoes.size(); i++) {
			transicaoAtual = transicoes.get(i);

			// Caso a transição seja sobre palavra
			// a remove e
			if (transicaoAtual.get(SIMBOLO).length() > 1) {
				transicoes.remove(i);

				// Faz uma copia da transacao sob palavra para passar como
				// parametro
				ArrayList<String> transicaoAux = new ArrayList<String>();
				transicaoAux = (ArrayList<String>) transicaoAtual.clone();

				deUmaParaNTransicoes(0, i, transicaoAtual, transicaoAux,
						estadosNovos);
			}

		}

	}

	/**
	 * Cria novos estados para fazer a transição sob símbolos e não palavras.
	 * Seta contador para cada novo estado criado.
	 * 
	 * @param indicePalavra
	 *            qual a posição da palavra que está sendo lida
	 * @param indiceTransicao
	 *            qual o índice da transição no ArrayList de transicoes
	 * @param transicaoAtual
	 *            qual a transição que está sendo transformada
	 * @param transicaoNova
	 *            transição após transformação
	 * @param estadosNovos
	 *            estados criados ao transformar a transição
	 */
	private void deUmaParaNTransicoes(int indicePalavra, int indiceTransicao,
			ArrayList<String> transicaoAtual, ArrayList<String> transicaoNova,
			ArrayList<String> estadosNovos) {
		String palavra = transicaoAtual.get(SIMBOLO);

		// Retorna caso tenha lido toda palavfa
		if (indicePalavra == palavra.length()) {
			return;
		}

		String simbolo = String.valueOf(palavra.charAt(indicePalavra));

		// Caso não seja o último índice
		if (indicePalavra < palavra.length() - 1) {
			int i = 1;
			// Cria novo estado
			String novoEstado = transicaoAtual.get(ESTADO_INICIAL) + "."
					+ String.valueOf(indicePalavra + i);

			// Verifica se estado a ser criado já existe
			// Só sai do while quando encontrar um estado que ainda não foi
			// criado
			while (estadosNovos.indexOf(novoEstado) != INDICE_INVALIDO) {
				// Seta contador e cria estado para o novo contador - verifica
				// no while se estado novo já existe
				i++;
				novoEstado = transicaoAtual.get(ESTADO_INICIAL) + "."
						+ String.valueOf(indicePalavra + i);
			}

			// Adiciona aos estados do AFN
			estadosNovos.add(novoEstado);
			estadosAFNE.add(novoEstado);

			// Transição nova
			transicaoNova.set(ESTADO_FINAL, novoEstado);
			transicaoNova.set(SIMBOLO, simbolo);

			// Adiciona a lista de transicoes
			transicoes.add(indiceTransicao, transicaoNova);

			// Cria transição auxiliar (Tem que fazer "new" Java passa por
			// referência e não por parâmetro)
			ArrayList<String> transicaoAux = new ArrayList<String>();

			// Troca o estado inicial de transicao Aux para novo estado do AFNE
			// para
			// passar ao proximo passo
			transicaoAux.add(ESTADO_INICIAL, novoEstado);
			transicaoAux.add(ESTADO_FINAL, novoEstado);
			transicaoAux.add(SIMBOLO, simbolo);

			// Chamada recursiva aumentando o índice da palavra (próximo
			// símbolo)
			// e o índice de transição
			deUmaParaNTransicoes(indicePalavra + 1, indiceTransicao + 1,
					transicaoAtual, transicaoAux, estadosNovos);
		} else {
			// Caso seja o último índice
			// Cria transição auxiliar (Tem que fazer "new" Java passa por
			// referência e não por parâmetro)
			ArrayList<String> transicaoAux = new ArrayList<String>();

			// Troca o estado inicial de transicao Aux para novo estado para
			// passar ao proximo passo
			transicaoAux.add(ESTADO_INICIAL, transicaoNova.get(ESTADO_INICIAL));
			transicaoAux.add(ESTADO_FINAL, transicaoAtual.get(ESTADO_FINAL));
			transicaoAux.add(SIMBOLO, simbolo);

			// Adiciona a lista de transicoes
			transicoes.add(indiceTransicao, transicaoAux);

			// Chamada recursive -> No próximo passo palavra.length =
			// indicePalavra -> Retorna
			deUmaParaNTransicoes(indicePalavra + 1, indiceTransicao + 1,
					transicaoAtual, transicaoAux, estadosNovos);
		}
	}

	/**
	 * Remove transições lambda do AFN Remoção é feita em passos Há casos que
	 * não remove todas as transições de uma vez
	 */
	public void removeLambda() {
		ArrayList<ArrayList<String>> transicoes = new ArrayList<>();

		// Verifica se existem transições lambda a serem removidas
		Boolean removerTransicoesLambda = true;

		// Se ainda existirem transicoes lambda
		while (removerTransicoesLambda) {
			// Faz uma copia do array de transições para passar como parametro
			// para a função
			transicoes = (ArrayList<ArrayList<String>>) this.transicoes.clone();

			// Chama função para remover lambda
			removerTransicoesLambda = removeLambda(transicoes);
		}
	}

	/**
	 * Remove transições lambda e adiciona novas transições para funcionamento
	 * correto do autômato
	 * 
	 * @param transicoes
	 * @return true se ainda houver transições lambda a serem removidas false
	 *         caso contrário
	 */
	private Boolean removeLambda(ArrayList<ArrayList<String>> transicoes) {
		ArrayList<ArrayList<String>> transicoesDoEstado = new ArrayList<>();
		ArrayList<ArrayList<String>> transicoesParaEstado = new ArrayList<>();
		Boolean fechoLambdaIncompleto;
		Boolean existemTransicoesLambda = false;

		// Para cada transição
		for (int i = 0; i < transicoes.size(); i++) {
			// Caso encontre uma transição lambda
			if ((transicoes.get(i).get(SIMBOLO).equals(PALAVRA_VAZIA))) {
				// Remove a transição
				this.transicoes.remove(this.transicoes.indexOf(transicoes
						.get(i)));

				// Caso a transição venha de um estado inicial
				// Faz o estado final da transição ser um estado inicial do
				// autômato
				if (estadosIniciais.indexOf(transicoes.get(i).get(
						ESTADO_INICIAL)) != INDICE_INVALIDO) {
					estadosIniciais.add(transicoes.get(i).get(ESTADO_FINAL));
				}

				// Caso o estado final da transição não seja um estado inicial
				// do autômato
				// Pega todas as transições que saem estado final da transição
				// Caso contrário pega um Array vazio
				if (estadosIniciais
						.indexOf(transicoes.get(i).get(ESTADO_FINAL)) == INDICE_INVALIDO) {
					transicoesDoEstado = transicoesDoEstado(transicoes.get(i)
							.get(ESTADO_FINAL));
				} else {
					transicoesDoEstado = new ArrayList<>();
				}

				// Pega as transições que chegam no estado inicial da transição
				transicoesParaEstado = transicoesParaEstado(transicoes.get(i)
						.get(ESTADO_INICIAL));

				// Verifica se ainda existem transições lambda no autômato
				// Chama a função fechoLambda para fazer as ligações entre
				// estados apos
				// a remoção da transição lambda
				fechoLambdaIncompleto = fechoLambda(transicoes.get(i),
						transicoesDoEstado, transicoesParaEstado);
				if (fechoLambdaIncompleto) {
					existemTransicoesLambda = true;
				}
			}
		}

		return existemTransicoesLambda;
	}

	/**
	 * 
	 * Faz a ligação entre os estados que se consegue alcançar por lambda com os
	 * estados que se alcança consumindo um símbolo Faz a ligação entre os
	 * estados que consomem um símbolo e chegam a outros estados por lambda
	 * 
	 * @param transicaoAtual
	 *            transição feita sob lambda
	 * @param transicoesDoEstado
	 *            transições que saem do estado final da transição feita sob
	 *            lambda
	 * @param transicoesParaEstado
	 *            transições que chegam ao estado inicial da transição feita sob
	 *            lambda
	 * @return true se ainda ouver transições lambda a serem removidas false
	 *         caso contrário
	 */
	private Boolean fechoLambda(ArrayList<String> transicaoAtual,
			ArrayList<ArrayList<String>> transicoesDoEstado,
			ArrayList<ArrayList<String>> transicoesParaEstado) {

		// Variáveis para verificar
		// se existe uma transição que chega à transição lambda sob lambda
		// se existe uma transição que sai da transição lambda sob lambda
		// (2 lambdas em sequência)
		Boolean existemTransicoesLambdaDoEstado = false;
		Boolean existemTransicoesLambdaParaEstado = false;

		// Para cada transição do estado final da transição feita sob lambda
		// Liga o estado inicial da transição feita sob lambda a cada estado
		// alcançável por lambda + símbolo
		for (int j = 0; j < transicoesDoEstado.size(); j++) {
			// Cria nova transição
			ArrayList<String> transicaoNova = new ArrayList<String>();
			transicaoNova.add(ESTADO_INICIAL,
					transicaoAtual.get(ESTADO_INICIAL));
			transicaoNova.add(ESTADO_FINAL,
					transicoesDoEstado.get(j).get(ESTADO_FINAL));
			transicaoNova.add(SIMBOLO, transicoesDoEstado.get(j).get(SIMBOLO));

			// Adiciona transição nova à lista de transições caso ela não exista
			if (transicoes.indexOf(transicaoNova) == INDICE_INVALIDO) {
				transicoes.add(transicaoNova);
			}

			// Se existir uma transicao que parte deste estado por lambda
			if (transicoesDoEstado.get(j).get(SIMBOLO).equals(PALAVRA_VAZIA)) {
				// Informa que ainda há transições lambda a serem corrigidas
				existemTransicoesLambdaDoEstado = true;
			}

		}

		// Para cada transição para o estado inicial da transição feita sob
		// lambda
		// Liga cada estado que chega ao estado inicial da transição sob lambda
		// a cada estado alcançável por lambda
		// símbolo + lambda
		for (int j = 0; j < transicoesParaEstado.size(); j++) {
			// Cria nova transição
			ArrayList<String> transicaoNova = new ArrayList<String>();
			transicaoNova.add(ESTADO_INICIAL,
					transicoesParaEstado.get(j).get(ESTADO_INICIAL));
			transicaoNova.add(ESTADO_FINAL, transicaoAtual.get(ESTADO_FINAL));
			transicaoNova
					.add(SIMBOLO, transicoesParaEstado.get(j).get(SIMBOLO));

			// Adiciona transição nova à lista de transições caso ela não exista
			if (transicoes.indexOf(transicaoNova) == INDICE_INVALIDO) {
				transicoes.add(transicaoNova);
			}

			// Se existir uma transicao que chega a este estado por lambda
			if (transicoesParaEstado.get(j).get(SIMBOLO).equals(PALAVRA_VAZIA)) {
				// Informa que ainda há transições lambda a serem corrigidas
				existemTransicoesLambdaParaEstado = true;
			}

		}

		return (existemTransicoesLambdaDoEstado || existemTransicoesLambdaParaEstado);
	}

	/**
	 * @param estado
	 * @return Uma lista de transições que o dado estado alcança
	 */
	private ArrayList<ArrayList<String>> transicoesDoEstado(String estado) {
		ArrayList<ArrayList<String>> transicoesDoEstado = new ArrayList<>();

		// Para todas as transições
		for (int j = 0; j < transicoes.size(); j++) {
			// Caso a transição tenha o estado inicial "estado"
			if (transicoes.get(j).get(ESTADO_INICIAL).equals(estado)) {
				transicoesDoEstado.add(transicoes.get(j));
			}
		}

		return transicoesDoEstado;
	}

	/**
	 * @param estado
	 * @return Uma lista de transições que chegam ao dado estado
	 */
	private ArrayList<ArrayList<String>> transicoesParaEstado(String estado) {
		ArrayList<ArrayList<String>> transicoesParaEstado = new ArrayList<>();

		// Para todas as transições
		for (int j = 0; j < transicoes.size(); j++) {
			// Caso a transição tenha o estado inicial "estado"
			if (transicoes.get(j).get(ESTADO_FINAL).equals(estado)) {
				transicoesParaEstado.add(transicoes.get(j));
			}
		}

		return transicoesParaEstado;
	}

	/**
	 * 
	 * @param estado
	 * @param simbolo
	 * @return Uma lista de transições que o dado estado alcança sob determinado
	 *         símbolo
	 */
	private ArrayList<String> transicoesDoEstado(String estado, String simbolo) {
		ArrayList<String> transicoesDoEstado = new ArrayList<String>();

		// Para cada transição
		for (int j = 0; j < transicoes.size(); j++) {
			// Caso a transição tenha o estado inicial "estado" e símbolo
			// "símbolo"
			if ((transicoes.get(j).get(ESTADO_INICIAL).equals(estado))
					&& (transicoes.get(j).get(SIMBOLO).equals(simbolo))) {
				transicoesDoEstado.add(transicoes.get(j).get(ESTADO_FINAL));
			}
		}

		return transicoesDoEstado;
	}

	/**
	 * Ordena os estados do AFN e os estados iniciais do AFN
	 */
	public void ordenaEstadosAFN() {
		// Ordena estados do AFNE
		Collections.sort(estadosAFNE);
		Collections.sort(estadosIniciais);
	}

	/**
	 * Ordena as transições do AFN baseadas em seu estado inicial
	 */
	public void ordenaTransicoes() {
		Collections.sort(transicoes, new comparadorTransicoes());
	}

	/**
	 * @return string contendo a todas as palavras lidas e se elas são aceitas
	 *         ou não pelo AFN
	 */
	public String verificaPalavras() {
		String resultado = "";
		// Para cada palavra
		for (int i = 0; i < palavras.size(); i++) {
			// Verifica se ela é aceita e coloca Sim ou Não ao fim.
			// Quebra linha e concatena com a string obtida anteriormente
			if (aceita(palavras.get(i))) {
				resultado = resultado + palavras.get(i) + " Sim ;\n";
			} else {
				resultado = resultado + palavras.get(i) + " Nao ;\n";
			}
		}

		return resultado;
	}

	/**
	 * @param palavra
	 * @return true se a palavra alcança um estado final do AFN, false caso
	 *         contrário
	 */
	private Boolean aceita(String palavra) {
		// Array para armazenar todos os estados que a palavra alcança
		ArrayList<String> estadosAlcancados = new ArrayList<String>();

		estadosAlcancados(palavra, estadosIniciais, estadosAlcancados, 0);

		// Para cada estado alcançado pela palavra
		// Verifica se estado alcançado é estado final do AFN
		for (int i = 0; i < estadosAlcancados.size(); i++) {
			if (estadosFinais.indexOf(estadosAlcancados.get(i)) != INDICE_INVALIDO) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Verifica os estados alcançados por uma palavra saindo de um conjunto de
	 * estados de partida
	 * 
	 * @param palavra
	 *            palavra a ser verificada
	 * @param estadosDePartida
	 *            conjunto de estados iniciais para verificar a palavra
	 * @param estadosAlcancados
	 *            estados que a palavra alcança após ser completamente consumida
	 * @param indiceSimbolo
	 *            indice do simbolo que vai ser consumido dentro da palavra
	 */
	private void estadosAlcancados(String palavra,
			ArrayList<String> estadosDePartida,
			ArrayList<String> estadosAlcancados, int indiceSimbolo) {

		// Caso tenha lido toda a palavra adiciona os estados alcançados ao
		// ArrayList de estados alcançados e retorna
		if (indiceSimbolo == palavra.length()) {
			estadosAlcancados.addAll(estadosDePartida);
			return;
		}

		// Array para armazenar a quais estados se chega através de um
		// determinado símbolo
		ArrayList<String> proximosEstados = new ArrayList<String>();

		// Pega o símbolo (caractere) da palavra sob o índice dado
		String simbolo;
		simbolo = String.valueOf(palavra.charAt(indiceSimbolo));

		// Pra cada estado de partida
		for (int i = 0; i < estadosDePartida.size(); i++) {
			// Pega os estados alcançados sob o símbolo que está sendo consumido
			proximosEstados = transicoesDoEstado(estadosDePartida.get(i),
					simbolo);

			// Chamada recursiva, passando como os estados de partida os estados
			// alcançados
			// pelo símbolo consumido e incrementando índice de 1 para ir ao
			// próximo símbolo
			estadosAlcancados(palavra, proximosEstados, estadosAlcancados,
					indiceSimbolo + 1);
		}
	}

	/**
	 * @param arrayList
	 * @return String contendo as informações do ArrayList dado sob o padrão de
	 *         saída especificado
	 */
	public static String arrayListStringParaString(ArrayList<String> arrayList) {
		String resultado = "";

		// Para cada elemento do ArrayList
		for (int i = 0; i < arrayList.size(); i++) {
			// Concatena
			resultado = resultado + arrayList.get(i) + " ";
		}

		return resultado + ";";
	}

	/**
	 * @return String contendo todas as transições do AFN sob o padrão de saída
	 *         especificado
	 */
	public String transicoesString() {
		String transicoesString = "";

		for (int i = 0; i < transicoes.size(); i++) {
			transicoesString = transicoesString
					+ arrayListStringParaString(transicoes.get(i)) + "\n";
		}

		return transicoesString;
	}

	/**
	 * Função auxiliar para verificar todas as informações do AFN no console
	 */
	public String toString() {
		return ("Alfabeto: " + this.alfabeto + "\n" + "Estados AFNE: "
				+ this.estadosAFNE + "\n" + "Estados Iniciais: "
				+ this.estadosIniciais + "\n" + "Estados Finais: "
				+ this.estadosFinais + "\n" + "Transicoes: " + this.transicoes + "\n");
	}

	/**
	 * Classe que implementa Comparator Utilizada como parâmetro para ordenar as
	 * transições
	 */
	class comparadorTransicoes implements Comparator<ArrayList<String>> {
		/**
		 * Compara duas transições (baseado no Unicode) observando o seu estado
		 * inicial. Se as transições forem igual seguem na ordem de entrada dos
		 * dados
		 */
		@Override
		public int compare(ArrayList<String> transicao1,
				ArrayList<String> transicao2) {

			String estadoInicial1 = transicao1.get(ESTADO_INICIAL);
			String estadoInicial2 = transicao2.get(ESTADO_INICIAL);

			return estadoInicial1.compareTo(estadoInicial2);
		}
	}

}
