package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import cidade.CaminhaoGrandePadrao;
import cidade.CaminhaoPequeno;
import cidade.CaminhaoPequeno10t;
import cidade.CaminhaoPequeno4t;
import cidade.CaminhaoPequeno8t;
import cidade.CaminhaoPequenoPadrao;
import cidade.EstacaoPadrao;
import cidade.Zona;
import estruturasDeDados.Fila;

public class Simulacao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private transient Timer timer;
    private int tempoSimulado = 0;
    private boolean pausado = false;
    
    private EstacaoPadrao estacao;
    private Fila filaCaminhoesPequenos = new Fila();
    
    private Zona zonaNorte = new Zona("Zona Norte");
    private Zona zonaSul = new Zona("Zona Sul");
    private Zona zonaLeste = new Zona("Zona Leste");
    private Zona zonaCentro = new Zona("Zona Centro");
    private Zona zonaSudeste = new Zona("Zona Sudeste");
    

    public void iniciar() {
        System.out.println("Simulação iniciada...");
        estacao = new EstacaoPadrao("Estação Central");
        
        zonaNorte.configurarGeracaoLixo(200, 500);
        zonaSul.configurarGeracaoLixo(300, 600);
        zonaLeste.configurarGeracaoLixo(150, 450);
        zonaCentro.configurarGeracaoLixo(250, 550);
        zonaSudeste.configurarGeracaoLixo(180, 400);
        
        zonaNorte.configurarTempoViagem(10, 15, 5, 8);  // Pico: 10–15 min, Fora pico: 5–8 min
        zonaSul.configurarTempoViagem(12, 18, 6, 10);
        zonaLeste.configurarTempoViagem(8, 12, 4, 6);
        zonaCentro.configurarTempoViagem(15, 20, 7, 12);
        zonaSudeste.configurarTempoViagem(10, 14, 5, 7);

        
        Zona[] roteiro = new Zona[] {
        	    zonaCentro, zonaNorte, zonaSul, zonaLeste, zonaSudeste
        	};
        	CaminhaoPequeno.setRoteiro(roteiro);
       
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!pausado) {
                    tempoSimulado++;
                    atualizarSimulacao();
                }
            }
        }, 0, 1000);
    }

    public void pausar() {
        System.out.println("Simulação pausada.");
        pausado = true;
    }

    public void continuarSimulacao() {
        System.out.println("Simulação retomada.");
        pausado = false;
    }

    public void encerrar() {
        System.out.println("Simulação encerrada.");
        if (timer != null) timer.cancel();
    }

    public void gravar(String caminho) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminho))) {
            oos.writeObject(this);
            System.out.println("Simulação salva.");
        }
    }

    public static Simulacao carregar(String caminho) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminho))) {
            Simulacao sim = (Simulacao) ois.readObject();
            sim.timer = new Timer();
            return sim;
        }
    }
    

    
    public void atualizarSimulacao() {
    	System.out.println("\n=== Tempo simulado: " + tempoSimulado + " minutos ===");
        
        Zona[] zonas = { zonaNorte, zonaSul, zonaLeste, zonaCentro, zonaSudeste };

        for (Zona zona : zonas) {
            int lixoGerado = zona.gerarLixo();
            
            if (filaCaminhoesPequenos.tamanho() == 0 || filaCaminhoesPequenos.verProximoDaFila().estaCheio()) {
                filaCaminhoesPequenos.enqueue(new CaminhaoPequenoPadrao());
            }

            CaminhaoPequeno caminhaoAtual = filaCaminhoesPequenos.verProximoDaFila();
            boolean conseguiuColetar = caminhaoAtual.coletar(lixoGerado);

            if (!conseguiuColetar) {
            	
                int capacidadeRestante = caminhaoAtual.capacidade - caminhaoAtual.getCargaAtual();
                caminhaoAtual.coletar(capacidadeRestante);

                if (caminhaoAtual.estaCheio()) {
                    Zona zonaAtual = zona; // zona onde o caminhão estava coletando
                    int tempoViagem = zonaAtual.calcularTempoViagem(tempoSimulado);
                    caminhaoAtual.setTempoDeViagem(tempoViagem);

                    estacao.receberCaminhaoPequeno(caminhaoAtual);
                    filaCaminhoesPequenos.dequeue();
                }

                int restante = lixoGerado - capacidadeRestante;

                CaminhaoPequeno[] novosCaminhoes = {
                    new CaminhaoPequeno4t(),
                    new CaminhaoPequeno8t(),
                    new CaminhaoPequeno10t()
                };

                for (CaminhaoPequeno novo : novosCaminhoes) {
                	novo.coletar(restante);
                    filaCaminhoesPequenos.enqueue(novo);
                }
            }
        }
		
     
        
        
        if (tempoSimulado % 10 == 0) { // a cada 10 min a estação carrega o lixo para o caminhão grande
            estacao.descarregarParaCaminhaoGrande(new CaminhaoGrandePadrao());
        }
        
        if (tempoSimulado % 30 == 0) { // 30 minutos o tempo de tolerancia de espera do caminhao grande para ir para o aterro
            estacao.enviarCaminhoesGrandesParaAterro();
        }
        
        estacao.processarFila();
    }
   
}
