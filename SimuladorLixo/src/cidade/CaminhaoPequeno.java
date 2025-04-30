package cidade;

public abstract class CaminhaoPequeno {
    public int capacidade;
    protected int cargaAtual;

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
}
