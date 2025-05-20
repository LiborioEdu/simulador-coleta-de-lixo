package cidade;

public class CaminhaoPequenoPadrao extends CaminhaoPequeno{
    public CaminhaoPequenoPadrao() {
    	super();
        this.capacidade = 2000;
        this.cargaAtual = 0;
        this.maxViagensPorDia = 10;
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
