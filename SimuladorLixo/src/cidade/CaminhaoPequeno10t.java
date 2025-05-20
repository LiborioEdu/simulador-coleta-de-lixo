package cidade;

public class CaminhaoPequeno10t extends CaminhaoPequeno {
    public CaminhaoPequeno10t() {
        super();
        this.capacidade = 10000;
        this.cargaAtual = 0;
        this.maxViagensPorDia = 2;
    }

    @Override
    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidade) {
            cargaAtual += quantidade;
            return true;
        }
        return false;
    }
}
