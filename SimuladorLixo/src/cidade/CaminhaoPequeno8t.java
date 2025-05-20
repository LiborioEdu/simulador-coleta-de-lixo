package cidade;

public class CaminhaoPequeno8t extends CaminhaoPequeno {
    public CaminhaoPequeno8t() {
        super();
        this.capacidade = 8000;
        this.cargaAtual = 0;
        this.maxViagensPorDia = 4;
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
