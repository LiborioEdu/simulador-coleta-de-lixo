package application;

import estruturasDeDados.Fila;

public class Main {

	public static void main(String[] args) {
		Fila fila = new Fila();
		
		fila.enqueue(10);
		fila.enqueue(20);
		fila.enqueue(30);
		fila.imprimir();
		
		fila.dequeue();
		fila.dequeue();
		fila.imprimir();
	}

}
