package estruturasDeDados;

import cidade.CaminhaoPequeno;

public class Fila {
	private No head;
	private No tail;
	private int tamanho;
	                                                                        
	private class No {
		CaminhaoPequeno caminhao;
		No prox;
		
		No(CaminhaoPequeno caminhao){
			this.caminhao = caminhao;
			this.prox = null;
		}
	}
	
	public No getHead() {
		return head;
	}

	public No getTail() {
		return tail;
	}


	public Fila() {
		head = null;
		tail = null;
		tamanho = 0;
	}
	
	public int tamanho() {
		return tamanho;
	}
	
	public No enqueue(CaminhaoPequeno caminhao) {
		No novoNo = new No (caminhao);
		if (head == null) {
			head = novoNo;
		} else {
			tail.prox = novoNo;
		}
		tail = novoNo;
		tamanho++;
		return head;
	}
	
	public No dequeue() {
	    if (head == null) {
	        throw new RuntimeException("A fila está vazia");
	    }

	    No removido = head;
	    head = head.prox;
	    if (head == null) {
	        tail = null;
	    }
	    tamanho--;
	    return removido;
	}
	
	public void imprimir() {
		No atual = head;
		System.out.print("Fila: ");
		while (atual != null) {
			System.out.print(atual.caminhao + " ");
			atual = atual.prox;
		}
	}
	
	public CaminhaoPequeno verProximoDaFila() {
		if (head == null) {
			return null;
		}
		return head.caminhao;
	}
	
}







