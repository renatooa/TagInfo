package taginfo.renato.com.br.taginfoservice.mensagem;

import java.io.Serializable;

import taginfo.renato.com.br.taginfoservice.modelo.InformacaoProduto;

public class MensagemInformacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private InformacaoProduto informacaoProduto;

	public MensagemInformacao() {

	}

	public MensagemInformacao(InformacaoProduto informacaoProduto) {
		super();
		this.informacaoProduto = informacaoProduto;
	}

	public InformacaoProduto getInformacaoProduto() {
		return informacaoProduto;
	}

	public void setInformacaoProduto(InformacaoProduto informacaoProduto) {
		this.informacaoProduto = informacaoProduto;
	}
}
