package cidade;

public class CaminhaoPequeno4t extends CaminhaoPequeno{
    public CaminhaoPequeno4t() {
        super();
        this.capacidade = 4000;
        this.cargaAtual = 0;
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
