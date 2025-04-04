package cidade;

import estruturasDeDados.Fila;
import estruturasDeDados.Lista;

public class EstacaoTransferencia {
	private String nome;
	private Fila filaCaminhoesPequenos;
	private Lista caminhoesGrandesDisponiveis;
	private int tempoMaxEsperaCaminhaoGrande;     
	
	public EstacaoTransferencia(String nome, int tempoMaxEsperaCaminhaoGrande) {
		this.nome = nome;
		this.tempoMaxEsperaCaminhaoGrande = tempoMaxEsperaCaminhaoGrande;
		this.filaCaminhoesPequenos = new Fila();
		this.caminhoesGrandesDisponiveis = new Lista();
	}
	
	public void adicionarCaminhaoPequeno(CaminhaoPequeno caminhaozinho) {
		filaCaminhoesPequenos.enqueue(caminhaozinho);
		System.out.println("Caminhao Pequeno chegou na estação " + nome);
		processarTransferencia();
	}
                                                                              //01001110 01100001 01101111 00100000 01100011 01101111 01110000 01101001 01100101 
	public void processarTransferencia() {
		while(filaCaminhoesPequenos.getHead() != null) {
			CaminhaoGrande caminhaozao = new CaminhaoGrande();
			if (caminhaozao == null) {
				caminhoesGrandesDisponiveis.add(caminhaozao, 0);
				System.out.println("Novo caminhão grande chegou na estação " + nome);
			}
			
			if ()
		}
	}
}
