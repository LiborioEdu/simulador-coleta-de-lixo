package application;

import estruturasDeDados.Fila;
import estruturasDeDados.Lista;

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
		System.out.println();
		
		Lista lista = new Lista();
		
		lista.add(10, 0);
		lista.add(20, 1);
		lista.add(30, 2);
		lista.add(40, 3);
		lista.imprimir();
		
		lista.remove(2);
		lista.remove(1);
		lista.imprimir();
		
		lista.remove(7);
	}

}
