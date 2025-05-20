package estruturasDeDados;

import cidade.CaminhaoPequeno;

public class Fila {
	private No head;
	private No tail;
	private int tamanho;
	                                                                        
	public class No {
		private CaminhaoPequeno caminhao;
		private No prox;
		
		No(CaminhaoPequeno caminhao){
			this.setCaminhao(caminhao);
			this.setProx(null);
		}

		public CaminhaoPequeno getCaminhao() {
			return caminhao;
		}

		public void setCaminhao(CaminhaoPequeno caminhao) {
			this.caminhao = caminhao;
		}

		public No getProx() {
			return prox;
		}

		public void setProx(No prox) {
			this.prox = prox;
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
			tail.setProx(novoNo);
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
	    head = head.getProx();
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
			System.out.print(atual.getCaminhao() + " ");
			atual = atual.getProx();
		}
	}
	
	public CaminhaoPequeno verProximoDaFila() {
		if (head == null) {
			return null;
		}
		return head.getCaminhao();
	}
	
}







