package cidade;

public abstract class CaminhaoPequeno {
    public int capacidade;
    protected int cargaAtual;
    protected static int proximoId = 1;
    protected String id;
    protected int tempoDeViagem;
    protected int viagensRealizadasHoje = 0;
    public int maxViagensPorDia;
    
    protected static Zona[] roteiro;
    protected Zona[] roteiroIndividual;
    protected int proximaZonaIndex = 0;
    protected Zona zonaAtual;
    protected boolean emViagemParaEstacao = false;
    
    protected int tempoParaChegar = 0;
    
    public CaminhaoPequeno() {
        this.id = String.format("%04d", proximoId++);
    }
    
    public boolean podeViajar() {
        return viagensRealizadasHoje < maxViagensPorDia;
    }
    
    public void registrarViagem() {
        viagensRealizadasHoje++;
    }
    
    public void resetarViagens() {
        viagensRealizadasHoje = 0;
    }
    
    public void iniciarDeslocamento(int tempoViagem) {
        this.tempoParaChegar = tempoViagem;
    }
    
    public boolean chegouNaZona() {
        return tempoParaChegar <= 0;
    }
    
    public void atualizarTempoDeslocamento() {
        if (tempoParaChegar > 0) {
            tempoParaChegar--;
        }
    }
    
    public String getId() {
        return id;
    }

    public boolean coletar(int quantidade) {
        if (quantidade <= 0) return false;
        
        if (cargaAtual + quantidade <= capacidade) {
            cargaAtual += quantidade;
            return true;
        }
        return false;
    }
    
    // Adicionar método para verificar se pode coletar mais
    public boolean podeColetarMais() {
        return cargaAtual < capacidade;
    }

    public boolean estaCheio() {
        return cargaAtual >= capacidade;
    }

    public int descarregar() {
        int carga = cargaAtual;
        cargaAtual = 0;
        return carga;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }
    
    public int getCapacidade() {
        return capacidade;
    }
    
    @Override
    public String toString() {
        String roteiroInfo = (roteiroIndividual != null) ? " (Roteiro Inverso)" : "";
        return "Caminhão ID " + getId() + roteiroInfo + " (" + capacidade + "kg, " + 
               viagensRealizadasHoje + "/" + maxViagensPorDia + " viagens)";
    }
    
    public static void setRoteiro(Zona[] zonas) {
    	roteiro = zonas;
    }
    
    public Zona getProximaZona() {
        // Usa roteiro individual se existir, senão usa o roteiro padrão
        Zona[] roteiroAtual = (this.roteiroIndividual != null) ? this.roteiroIndividual : CaminhaoPequeno.roteiro;
        Zona zona = roteiroAtual[proximaZonaIndex];
        proximaZonaIndex = (proximaZonaIndex + 1) % roteiroAtual.length;
        return zona;
    }
    
    
    public int getTempoParaChegar() {
        return tempoParaChegar;
    }
    
    public void setTempoDeViagem(int tempo) {
        this.tempoDeViagem = tempo;
    }

    public int getTempoDeViagem() {
        return tempoDeViagem;
    }
    
    public void iniciarViagemParaEstacao(int tempoViagem) {
        this.emViagemParaEstacao = true;
        this.tempoParaChegar = tempoViagem;
    }

    public void iniciarViagemParaZona(Zona zona, int tempoViagem) {
        this.zonaAtual = zona;
        this.emViagemParaEstacao = false;
        this.tempoParaChegar = tempoViagem;
    }

    public boolean isEmViagemParaEstacao() {
        return emViagemParaEstacao;
    }

    public Zona getZonaAtual() {
        return zonaAtual;
    }
    
    public void setRoteiroIndividual(Zona[] zonas) {
        this.roteiroIndividual = zonas;
    }
    
    public Zona[] getRoteiroIndividual() {
        return this.roteiroIndividual;
    }
    
    public static void setProximoId(int id) {
        proximoId = id;
    }
    
    public static void resetarContadorId() {
        proximoId = 1;
    }
}

