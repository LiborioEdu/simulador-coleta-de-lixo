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
                System.out.println("Caminhão pequeno ID " + pequeno.getId() + " com capacidade " + pequeno.capacidade + "kg descarregou " + descarregado + "kg na estação " + nome);
            }
        }
    }

    @Override
    public void descarregarParaCaminhaoGrande(CaminhaoGrande novoCaminhao) {
        CaminhaoGrande caminhaoAtual = listaCaminhoesGrandes.verProximoDaLista();

        if (caminhaoAtual == null) {
            caminhaoAtual = novoCaminhao;
            listaCaminhoesGrandes.add(caminhaoAtual, 0);
        }

        caminhaoAtual.carregar(lixoArmazenado);
        System.out.println("Estação " + nome + " carregou caminhão grande com " + lixoArmazenado + "kg.");
        lixoArmazenado = 0;

        if (caminhaoAtual.prontoParaPartir()) {
        	System.out.println("Caminhão grande está cheio e será enviado ao aterro com " + caminhaoAtual.getCargaAtual() + "kg.");
            caminhaoAtual.descarregar();
            listaCaminhoesGrandes.remove(0);
        }
    }
    
    public void enviarCaminhoesGrandesParaAterro() {
        CaminhaoGrande caminhao = listaCaminhoesGrandes.verProximoDaLista();

        if (caminhao != null && caminhao.getCargaAtual() > 0) {
            System.out.println("Caminhão grande enviado para o aterro (tolerância atingida) com " + caminhao.getCargaAtual() + "kg.");
            caminhao.descarregar();
            listaCaminhoesGrandes.remove(0);
        }
    }
}
