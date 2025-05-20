package estruturasDeDados;

import cidade.CaminhaoGrande;
import cidade.CaminhaoPequeno;

public class Lista {
	private No inicio;
	
	private class No {
		CaminhaoGrande caminhaozao;
		No prox;
	
		No(CaminhaoGrande caminhaozao) {
			this.caminhaozao = caminhaozao;
			this.prox = null;
		}
	}
	
	public No add(CaminhaoGrande caminhaozao, int posicao) {
		No novoNo = new No(caminhaozao);
		
		if(posicao < 0) {
			throw new RuntimeException("Posição invárida");
		}
		
		if (posicao == 0) {
			novoNo.prox = inicio;
			inicio = novoNo;
			return inicio;
		}
		
		No atual = inicio;
		int i = 0;
		while(atual != null && i < (posicao-1)) {
			atual = atual.prox;
			i++;
		}
		
		if (atual == null) {
			throw new RuntimeException("Posição inválida");
		}
		
		novoNo.prox = atual.prox;
		atual.prox = novoNo;
		return inicio;
	}
	
	public No remove(int posicao) {
		if (inicio == null || posicao < 0) {
			throw new RuntimeException("Posição inválida");
		}
		
		if (posicao == 0) {
			inicio = inicio.prox;
			return inicio;
		}
		
		No atual = inicio;
		int i = 0;
		while (atual.prox != null && i < posicao - 1) {
			atual = atual.prox;
			i++;
		}
		
		if (atual.prox == null) {
			throw new RuntimeException("Posição não encontrada");
		}
		
		atual.prox = atual.prox.prox;
		return inicio;
	}
	
	public void imprimir() {
		No atual = inicio;
		System.out.print("Lista: ");
		while (atual != null) {
			System.out.print(atual.caminhaozao + " ");
			atual = atual.prox;
		}
	}
	
	public CaminhaoGrande verProximoDaLista() {
		if (inicio == null) {
			return null;
		}
		return inicio.caminhaozao;
	}


	
}
