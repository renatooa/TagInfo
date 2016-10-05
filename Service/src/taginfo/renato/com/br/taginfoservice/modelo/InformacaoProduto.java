package taginfo.renato.com.br.taginfoservice.modelo;

public class InformacaoProduto {
	
	private int codigo = 10;
	
	private String nome = "Batata Lisa";
	
	private double vendas = 1099.00;
	
	private double quantidadeAReceber = 10;
	
	private double estoque = 50;
	
	public InformacaoProduto() {
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getVendas() {
		return vendas;
	}

	public void setVendas(double vendas) {
		this.vendas = vendas;
	}

	public double getEstoque() {
		return estoque;
	}

	public void setEstoque(double estoque) {
		this.estoque = estoque;
	}
}
