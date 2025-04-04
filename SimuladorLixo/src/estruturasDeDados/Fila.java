package estruturasDeDados;

public class Fila {
	private No head;
	private No tail;
	private int tamanho;
	
	private class No {
		int valor;
		No prox;
		
		No(int valor){
			this.valor = valor;
			this.prox = null;
		}
	}
	
	public Fila() {
		head = null;
		tail = null;
		tamanho = 0;
	}
	
	public int tamanho() {
		return tamanho;
	}
	
	public No enqueue(int item) {
		No novoNo = new No (item);
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
		if(tail == null) {
			throw new RuntimeException("A Fila está vazia");
		} else if(head.prox == null) {
				head.prox = null;
				tail.prox = null;
		} else {
			head = head.prox;
		}
		return head;
	}
	
	public void imprimir() {
		No atual = head;
		System.out.print("Fila: ");
		while (atual != null) {
			System.out.print(atual.valor + " ");
			atual = atual.prox;
		}
	}
	
}







