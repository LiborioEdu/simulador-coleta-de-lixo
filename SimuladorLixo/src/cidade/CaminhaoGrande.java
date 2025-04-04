package cidade;

public class CaminhaoGrande {
	private int contadorId = 1;
	private int id;
	private int capacidadeMaxGrande = 20;
	private int cargaAtualGrande;
	
	public CaminhaoGrande() {
		this.id = contadorId++;
		this.cargaAtualGrande = 0;
	}

	public int getId() {
		return id;
	}

	public int getCapacidadeMaxGrande() {
		return capacidadeMaxGrande;
	}

	public int getCargaAtual() {
		return cargaAtualGrande;
	}
	
	public void receberCarga(int carga) {
		if (carga <= getCapacidadeMaxGrande()) {
			cargaAtualGrande += carga;
		} else {
			System.out.println("Caminhão Grande está cheio, aguarde ele voltar");
			enviarParaAterro();
		}
	}
	
	public void enviarParaAterro() {
		cargaAtualGrande = 0;
	}
	

}
