package cidade;

public class CaminhaoPequeno {
	private int contadorId = 1;
	private int id;
	private int capacidadePequeno;
	private int cargaAtualPequeno;
	
	public CaminhaoPequeno(int capacidadePequeno) {
		this.id = contadorId++;
		this.capacidadePequeno = capacidadePequeno;
		this.cargaAtualPequeno = 0;
	}

	public int getCapacidadePequeno() {
		return capacidadePequeno;
	}

	public int getCargaAtualPequeno() {
		return cargaAtualPequeno;
	}
	
    public boolean estaCheio() {
        return cargaAtualPequeno == capacidadePequeno;
    }
	
    
    public void coletarLixo(int quantidadeDeLixo) {
    	while(cargaAtualPequeno < capacidadePequeno) {
    		cargaAtualPequeno += quantidadeDeLixo;
    	}
    	System.out.println("Caminhão pequeno " + id + " está lotado, encaminhando para a estação de transferencia");
    }
}
