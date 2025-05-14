package cidade;

public abstract class CaminhaoPequeno {
    public int capacidade;
    protected int cargaAtual;
    protected static int proximoId = 1;
    protected String id;
    
    public CaminhaoPequeno() {
        this.id = String.format("%04d", proximoId++);
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
}
