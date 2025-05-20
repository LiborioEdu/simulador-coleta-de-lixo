package cidade;

import java.util.Random;

public class Zona {
    private String nome;
    private int lixoAcumulado;
    
    private int minGeracaoLixo = 100;
    private int maxGeracaoLixo = 600;
    
    private int minPico;
    private int maxPico;
    private int minForaPico;
    private int maxForaPico;
    
    private Random random = new Random();
    
    public Zona(String nome) {
        this.nome = nome;
        this.lixoAcumulado = 0;
    }
    
    public void configurarGeracaoLixo(int minimo, int maximo) {
    	this.minGeracaoLixo = minimo;
    	this.maxGeracaoLixo = maximo;
    }
    
    public void configurarTempoViagem(int minPico, int maxPico, int minForaPico, int maxForaPico) {
    	this.minPico = minPico;
    	this.maxPico = maxPico;
    	this.minForaPico = minForaPico;
    	this.maxForaPico = maxForaPico;
    }
    
    public int calcularTempoViagem(int tempoAtual) {
    	boolean pico = (tempoAtual >= 420 && tempoAtual <= 540) || (tempoAtual >= 1020 && tempoAtual <= 1140); //Picos - 7h as 9h ou 17h as 19h
    	if (pico) {
    		return random.nextInt(maxPico - minPico + 1) + minPico;
    	} else {
    		return random.nextInt(maxForaPico - minForaPico + 1) + minForaPico;
    	}
    }

    public int gerarLixo() {
        int quantidade = random.nextInt(maxGeracaoLixo - minGeracaoLixo + 1) + minGeracaoLixo;
        lixoAcumulado += quantidade;
        System.out.println(nome + ": Gerou " + quantidade + "kg de lixo. Total: " + lixoAcumulado + "kg.");
		return quantidade;
    }

    public int coletarLixo(int quantidade) {
        int coletado = Math.min(quantidade, lixoAcumulado);
        lixoAcumulado -= coletado;
        return coletado;
    }

    public int getLixoAcumulado() {
        return lixoAcumulado;
    }

    public String getNome() {
        return nome;
    }
}
