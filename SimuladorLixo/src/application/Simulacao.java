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
import estruturasDeDados.Lista;

public class Simulacao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private transient Timer timer;
    private int tempoSimulado = 0;
    private boolean pausado = false;
    
    private EstacaoPadrao estacao;
    private Fila filaCaminhoesPequenos = new Fila();
    private Lista filaCaminhoesDescansando = new Lista();
    
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

        
        Zona[] roteiro = new Zona[] {zonaCentro, zonaNorte, zonaSul, zonaLeste, zonaSudeste};
        CaminhaoPequeno.setRoteiro(roteiro);
       
        for (CaminhaoPequeno cam : new CaminhaoPequeno[]{
                new CaminhaoPequenoPadrao(),
                new CaminhaoPequeno4t(),
                new CaminhaoPequeno8t(),
                new CaminhaoPequeno10t()
        }) {
            Zona primeiraZona = roteiro[0];
            int tempo = primeiraZona.calcularTempoViagem(tempoSimulado);
            cam.iniciarViagemParaZona(primeiraZona, tempo);
            filaCaminhoesPequenos.enqueue(cam);
        }
        
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
    
    private CaminhaoPequeno escolherCaminhao(int lixoRestante) {
        if (lixoRestante >= 10000) return new CaminhaoPequeno10t();
        if (lixoRestante >= 8000) return new CaminhaoPequeno8t();
        if (lixoRestante >= 4000) return new CaminhaoPequeno4t();
        return new CaminhaoPequenoPadrao();
    }
    

    
    public void atualizarSimulacao() {
        System.out.println("\n=== Tempo simulado: " + tempoSimulado + " minutos ===");
        
        // 1. Gerar lixo em todas as zonas
        Zona[] zonas = {zonaNorte, zonaSul, zonaLeste, zonaCentro, zonaSudeste};
        for (Zona zona : zonas) {
            zona.gerarLixo(); // Gerar lixo em todas as zonas
        }
        
        // 2. Processar cada caminhão na fila
        Fila.No noAtual = filaCaminhoesPequenos.getHead();
        while (noAtual != null) {
            CaminhaoPequeno caminhao = noAtual.getCaminhao();
            
            // Atualizar tempo de viagem
            caminhao.atualizarTempoDeslocamento();
            
            if (caminhao.chegouNaZona()) {
                if (caminhao.isEmViagemParaEstacao()) {
                    // Chegou na estação - descarregar
                    System.out.println("Caminhão " + caminhao.getId() + " chegou na estação");
                    estacao.receberCaminhaoPequeno(caminhao);
                    
                    // Programar próxima viagem para a próxima zona do roteiro
                    Zona proximaZona = caminhao.getProximaZona();
                    int tempoViagem = proximaZona.calcularTempoViagem(tempoSimulado);
                    caminhao.iniciarViagemParaZona(proximaZona, tempoViagem);
                    System.out.println("Caminhão " + caminhao.getId() + " programado para ir para " + 
                                      proximaZona.getNome() + " (tempo: " + tempoViagem + " min)");
                } else {
                    // Chegou na zona - coletar lixo
                    Zona zonaAtual = caminhao.getZonaAtual();
                    int lixoDisponivel = zonaAtual.getLixoAcumulado();
                    
                    if (lixoDisponivel > 0) {
                        int lixoColetado = Math.min(lixoDisponivel, caminhao.getCapacidade() - caminhao.getCargaAtual());
                        boolean coletou = caminhao.coletar(lixoColetado);
                        
                        if (coletou) {
                            zonaAtual.coletarLixo(lixoColetado);
                            System.out.println("Caminhão " + caminhao.getId() + " coletou " + lixoColetado + 
                                             "kg na " + zonaAtual.getNome() + ". Carga atual: " + 
                                             caminhao.getCargaAtual() + "/" + caminhao.getCapacidade());
                        }
                        
                        // Verificar se deve voltar para a estação
                        if (caminhao.estaCheio() || lixoDisponivel - lixoColetado <= 0) {
                            int tempoViagem = zonaAtual.calcularTempoViagem(tempoSimulado);
                            caminhao.iniciarViagemParaEstacao(tempoViagem);
                            caminhao.registrarViagem();
                            System.out.println("Caminhão " + caminhao.getId() + " voltando para estação. " +
                                             "Tempo de viagem: " + tempoViagem + " min");
                        }
                    } else {
                        // Se não tem lixo, vai para próxima zona
                        Zona proximaZona = caminhao.getProximaZona();
                        int tempoViagem = proximaZona.calcularTempoViagem(tempoSimulado);
                        caminhao.iniciarViagemParaZona(proximaZona, tempoViagem);
                        System.out.println("Caminhão " + caminhao.getId() + " indo para " + 
                                         proximaZona.getNome() + " (sem lixo na zona atual)");
                    }
                }
            } else {
                // Ainda em trânsito
                System.out.println("Caminhão " + caminhao.getId() + " em trânsito para " + 
                                 (caminhao.isEmViagemParaEstacao() ? "Estação Central" : caminhao.getZonaAtual().getNome()) + 
                                 ". Tempo restante: " + caminhao.getTempoParaChegar() + " min");
            }
            
            noAtual = noAtual.getProx();
        }
        
        // Processar estação e outras lógicas
        estacao.processarFila();
        
        if (tempoSimulado % 10 == 0) {
            estacao.descarregarParaCaminhaoGrande(new CaminhaoGrandePadrao());
        }
        
        if (tempoSimulado % 30 == 0) {
            estacao.enviarCaminhoesGrandesParaAterro();
        }
        
        if (tempoSimulado % 1440 == 0) {
            System.out.println("=== NOVO DIA == Resetando viagens dos caminhões...");
            Fila.No atual = filaCaminhoesPequenos.getHead();
            while (atual != null) {
                atual.getCaminhao().resetarViagens();
                atual = atual.getProx();
            }
        }
    	
    	/*
    	System.out.println("\n=== Tempo simulado: " + tempoSimulado + " minutos ===");
        
        Zona[] zonas = { zonaNorte, zonaSul, zonaLeste, zonaCentro, zonaSudeste };

        for (Zona zona : zonas) {
            int lixoGerado = zona.gerarLixo();
            
            
            
        CaminhaoPequeno caminhaoAtual = filaCaminhoesPequenos.verProximoDaFila();

         // Se não houver caminhão, ou ele está cheio ou atingiu o limite de viagens
         if (caminhaoAtual == null || caminhaoAtual.estaCheio() || !caminhaoAtual.podeViajar()) {
             CaminhaoPequeno novo = escolherCaminhao(lixoGerado);
             filaCaminhoesPequenos.enqueue(novo);
             caminhaoAtual = novo;
         }

      
            boolean conseguiuColetar = caminhaoAtual.coletar(lixoGerado);

            if (!conseguiuColetar) {
            	
                int capacidadeRestante = caminhaoAtual.capacidade - caminhaoAtual.getCargaAtual();
                caminhaoAtual.coletar(capacidadeRestante);

                if (caminhaoAtual.estaCheio()) {
                	caminhaoAtual.registrarViagem();
                    Zona zonaAtual = zona; // zona onde o caminhão estava coletando
                    int tempoViagem = zonaAtual.calcularTempoViagem(tempoSimulado);
                    caminhaoAtual.setTempoDeViagem(tempoViagem);

                    estacao.receberCaminhaoPequeno(caminhaoAtual);
                    filaCaminhoesPequenos.dequeue();
                }

                int restante = lixoGerado - capacidadeRestante;

            }
            
        }
        
        if (tempoSimulado % 1440 == 0) {
            System.out.println("Resetando viagens dos caminhões...");
            // Resetar todos os caminhões ativos na fila
            Fila.No atual = filaCaminhoesPequenos.getHead();
            while (atual != null) {
                atual.getCaminhao().resetarViagens();
                atual = atual.getProx();
            }
        }
		
     
        
        
        if (tempoSimulado % 10 == 0) { // a cada 10 min a estação carrega o lixo para o caminhão grande
            estacao.descarregarParaCaminhaoGrande(new CaminhaoGrandePadrao());
        }
        
        if (tempoSimulado % 30 == 0) { // 30 minutos o tempo de tolerancia de espera do caminhao grande para ir para o aterro
            estacao.enviarCaminhoesGrandesParaAterro();
        }
        
        estacao.processarFila();
        */
    }
   
}
