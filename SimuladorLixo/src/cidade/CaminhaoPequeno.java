package cidade;

public abstract class CaminhaoPequeno {
    public int capacidade;
    protected int cargaAtual;
    protected static int proximoId = 1;
    protected String id;
    protected int tempoDeViagem;
    
    protected static Zona[] roteiro;
    protected int proximaZonaIndex = 0;
    
    protected int tempoParaChegar = 0;
    
    public CaminhaoPequeno() {
        this.id = String.format("%04d", proximoId++);
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

    public abstract boolean coletar(int quantidade);

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
        return "Caminhão ID " + id + " (" + capacidade + "kg)";
    }
    
    public static void setRoteiro(Zona[] zonas) {
        roteiro = zonas;
    }
    
    public Zona getProximaZona() {
        Zona zona = roteiro[proximaZonaIndex];
        proximaZonaIndex = (proximaZonaIndex + 1) % roteiro.length;
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
}
