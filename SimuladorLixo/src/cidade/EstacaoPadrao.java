package cidade;

import estruturasDeDados.Fila;
import estruturasDeDados.Lista;

public class EstacaoPadrao extends EstacaoTransferencia{

    private int lixoArmazenado;
    private Fila filaCaminhoesPequenos;
    private Lista listaCaminhoesGrandes;

    public EstacaoPadrao(String nome) {
        super(nome);
        this.lixoArmazenado = 0;
        this.filaCaminhoesPequenos = new Fila(); 
        this.listaCaminhoesGrandes = new Lista();
    }

    @Override
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao) {
    	filaCaminhoesPequenos.enqueue(caminhao);
        System.out.println("Caminhão pequeno chegou à estação " + nome);
    }
    
    public void processarFila() {
        while (filaCaminhoesPequenos.tamanho() > 0) {
            CaminhaoPequeno pequeno = filaCaminhoesPequenos.verProximoDaFila();
            if (pequeno != null) {
                int descarregado = pequeno.descarregar();
                lixoArmazenado += descarregado;
                filaCaminhoesPequenos.dequeue();
                System.out.println("Caminhão pequeno descarregou " + descarregado + "kg na estação " + nome);
            }
        }
    }

    @Override
    public void descarregarParaCaminhaoGrande(CaminhaoGrande caminhao) {
        caminhao.carregar(lixoArmazenado);
        lixoArmazenado = 0;
        listaCaminhoesGrandes.add(caminhao, 0);
        System.out.println("Estação " + nome + " carregou caminhão grande com " + lixoArmazenado + "kg.");
    }
    
    public void enviarCaminhoesGrandesParaAterro() {
        while (listaCaminhoesGrandes.verProximoDaLista() != null) {
            CaminhaoGrande caminhao = listaCaminhoesGrandes.verProximoDaLista();
            caminhao.descarregar();
            listaCaminhoesGrandes.remove(0);
        }
    }
}
