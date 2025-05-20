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
        
        // Configuração das zonas (mantida igual)
        zonaNorte.configurarGeracaoLixo(200, 500);
        zonaSul.configurarGeracaoLixo(300, 600);
        zonaLeste.configurarGeracaoLixo(150, 450);
        zonaCentro.configurarGeracaoLixo(250, 550);
        zonaSudeste.configurarGeracaoLixo(180, 400);
        
        // Definir roteiros
        Zona[] roteiroPadrao = {zonaCentro, zonaNorte, zonaSul, zonaLeste, zonaSudeste};
        Zona[] roteiroInverso = {zonaSudeste, zonaLeste, zonaSul, zonaNorte, zonaCentro};
        
        // Resetar ID para começar em 0001
        CaminhaoPequeno.resetarContadorId();
        
        // ===== CAMINHÕES COM ROTEIRO PADRÃO (IDs 0001-0004) =====
        CaminhaoPequeno.setRoteiro(roteiroPadrao);
        
        // Criar caminhões padrão
        filaCaminhoesPequenos.enqueue(new CaminhaoPequenoPadrao());   // ID 0001 (2t)
        filaCaminhoesPequenos.enqueue(new CaminhaoPequeno4t());      // ID 0002 (4t)
        filaCaminhoesPequenos.enqueue(new CaminhaoPequeno8t());      // ID 0003 (8t)
        filaCaminhoesPequenos.enqueue(new CaminhaoPequeno10t());     // ID 0004 (10t)
        
        // ===== CAMINHÕES COM ROTEIRO INVERSO (IDs 0006-0009) =====
        // Forçar IDs específicos
        CaminhaoPequeno.setProximoId(6); // Próximo ID será 0006
        
        // Criar caminhões com roteiro inverso
        CaminhaoPequeno cam6 = new CaminhaoPequenoPadrao();  // ID 0006 (2t)
        cam6.setRoteiroIndividual(roteiroInverso);
        filaCaminhoesPequenos.enqueue(cam6);
        
        CaminhaoPequeno cam7 = new CaminhaoPequeno4t();      // ID 0007 (4t)
        cam7.setRoteiroIndividual(roteiroInverso);
        filaCaminhoesPequenos.enqueue(cam7);
        
        CaminhaoPequeno cam8 = new CaminhaoPequeno8t();      // ID 0008 (8t)
        cam8.setRoteiroIndividual(roteiroInverso);
        filaCaminhoesPequenos.enqueue(cam8);
        
        CaminhaoPequeno cam9 = new CaminhaoPequeno10t();     // ID 0009 (10t)
        cam9.setRoteiroIndividual(roteiroInverso);
        filaCaminhoesPequenos.enqueue(cam9);
        
        // Iniciar todos os caminhões na primeira zona de seus roteiros
        Fila.No atual = filaCaminhoesPequenos.getHead();
        while (atual != null) {
            CaminhaoPequeno caminhao = atual.getCaminhao();
            Zona primeiraZona = caminhao.getProximaZona(); // Pega a primeira zona do roteiro apropriado
            int tempoViagem = primeiraZona.calcularTempoViagem(tempoSimulado);
            caminhao.iniciarViagemParaZona(primeiraZona, tempoViagem);
            
            System.out.println(caminhao + " iniciando na " + primeiraZona.getNome() + 
                             " (" + (caminhao.getRoteiroIndividual() != null ? "Inverso" : "Padrão") + ")");
            
            atual = atual.getProx();
        }
        
        // Iniciar timer (mantido igual)
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
        
        // 1. Gerar lixo em todas as zonas
        Zona[] zonas = {zonaNorte, zonaSul, zonaLeste, zonaCentro, zonaSudeste};
        for (Zona zona : zonas) {
            zona.gerarLixo();
        }
        
        // 2. Processar cada caminhão na fila
        Fila.No noAtual = filaCaminhoesPequenos.getHead();
        while (noAtual != null) {
            CaminhaoPequeno caminhao = noAtual.getCaminhao();
            
            // Verificar limite de viagens
            if (!caminhao.podeViajar()) {
                System.out.println("Caminhão " + caminhao.getId() + " atingiu o limite de viagens.");
                noAtual = noAtual.getProx();
                continue;
            }
            
            // Atualizar tempo de viagem
            caminhao.atualizarTempoDeslocamento();
            
            if (caminhao.chegouNaZona()) {
                if (caminhao.isEmViagemParaEstacao()) {
                    // Chegou na estação - descarregar
                    System.out.println("Caminhão " + caminhao.getId() + " chegou na estação central");
                    estacao.receberCaminhaoPequeno(caminhao);
                    
                    // Programar próxima viagem para a próxima zona do roteiro
                    Zona proximaZona = caminhao.getProximaZona();
                    int tempoViagem = proximaZona.calcularTempoViagem(tempoSimulado);
                    caminhao.iniciarViagemParaZona(proximaZona, tempoViagem);
                    System.out.println("Caminhão " + caminhao.getId() + " programado para " + 
                                      proximaZona.getNome() + " (tempo: " + tempoViagem + " min)");
                } else {
                    // Chegou na zona - coletar lixo
                    Zona zonaAtual = caminhao.getZonaAtual();
                    int lixoDisponivel = zonaAtual.getLixoAcumulado();
                    
                    if (lixoDisponivel > 0) {
                        int capacidadeDisponivel = caminhao.getCapacidade() - caminhao.getCargaAtual();
                        int lixoColetado = Math.min(lixoDisponivel, capacidadeDisponivel);
                        
                        if (lixoColetado > 0) {
                            boolean coletou = caminhao.coletar(lixoColetado);
                            if (coletou) {
                                zonaAtual.coletarLixo(lixoColetado);
                                System.out.println("Caminhão " + caminhao.getId() + " coletou " + lixoColetado + 
                                                 "kg na " + zonaAtual.getNome() + ". Carga: " + 
                                                 caminhao.getCargaAtual() + "/" + caminhao.getCapacidade());
                            }
                        }
                        
                        // Verificar se está cheio para voltar à estação
                        if (caminhao.estaCheio()) {
                            int tempoViagem = zonaAtual.calcularTempoViagem(tempoSimulado);
                            caminhao.iniciarViagemParaEstacao(tempoViagem);
                            caminhao.registrarViagem();
                            System.out.println("Caminhão " + caminhao.getId() + " CHEIO. Voltando para estação. " +
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
    	

    }
   
}
