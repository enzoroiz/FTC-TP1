public class Main {

	public static void main(String[] args) {
		EscritorArquivo escritorArquivo;
		String entrada;
		String saida;
		
		//Recebe parâmetros pela linha de comando
		try {
			entrada = args[0];
			saida = args[1];
			escritorArquivo = new EscritorArquivo(saida);
		} catch (Exception e) {
			System.out.println("Erro na passagem de parâmetros!");
			System.exit(0);
			return;
		}
	    
		LeitorArquivo leitorArquivo = new LeitorArquivo(entrada);
		AFN afn = leitorArquivo.lerArquivo();
		
		// Caso haja algum erro na leitura afn será null 
		if (afn == null) {
			System.exit(0);
			return;
		}
		
		//Remove transições lambda do AFN
		afn.removeLambda();
		
		//Ordena as transicoes
		afn.ordenaTransicoes();
		
		//Muda de transicoes sobre palavras, para simbolos
		afn.deAFNEParaAFN();
		
		//Ordena os estados (com os novos estados criados) e os estados iniciais
		afn.ordenaEstadosAFN();
		
		//Escreve informações do AFN no arquivo sob o padrão especificado
		escritorArquivo.escreveArquivo(afn);
	}

}