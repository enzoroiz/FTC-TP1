import java.io.BufferedWriter;
import java.io.FileWriter;

public class EscritorArquivo {
	private String saida;

	/**
	 * Construtor informando o arquivo de saida onde serao escritos os
	 * resultados
	 * 
	 * @param saida
	 */
	public EscritorArquivo(String saida) {
		this.saida = saida;
	}

	/**
	 * Escreve no arquivo de saída a partir de uma string
	 * 
	 * @param mensagem
	 */
	public void escreveArquivo(String mensagem) {
		try {
			// Cria arquivo
			FileWriter escritorArquivo = new FileWriter(saida);
			BufferedWriter arquivoSaida = new BufferedWriter(escritorArquivo);

			// Escreve mensagem no arquivo
			arquivoSaida.write(mensagem);

			// Fecha arquivo
			arquivoSaida.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Escreve informações do AFN no arquivo de saída sob o padrão especificado
	 * 
	 * @param afn
	 */
	public void escreveArquivo(AFN afn) {
		try {
			// Cria arquivo
			FileWriter escritorArquivo = new FileWriter(saida);
			BufferedWriter arquivoSaida = new BufferedWriter(escritorArquivo);

			// Escreve informações no arquivo
			arquivoSaida.write(AFN.arrayListStringParaString(afn.estadosAFNE));
			arquivoSaida.write("\n");
			arquivoSaida.write(AFN.arrayListStringParaString(afn.alfabeto));
			arquivoSaida.write("\n");
			arquivoSaida.write("\n");
			arquivoSaida.write(afn.transicoesString());
			arquivoSaida.write("\n");
			arquivoSaida.write(AFN
					.arrayListStringParaString(afn.estadosIniciais));
			arquivoSaida.write("\n");
			arquivoSaida
					.write(AFN.arrayListStringParaString(afn.estadosFinais));
			arquivoSaida.write("\n");
			arquivoSaida.write("\n");
			arquivoSaida.write(afn.verificaPalavras());

			// Fecha arquivo
			arquivoSaida.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}