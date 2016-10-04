package taginfo.renato.com.br.taginfoservice.modelo;

import java.io.Serializable;

public class TagDescoberta implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tagId;

	private TipoInformacao tipoInformacao;

	private String idInformacao;

	public TagDescoberta() {
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public TipoInformacao getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(TipoInformacao tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public String getIdInformacao() {
		return idInformacao;
	}

	public void setIdInformacao(String idInformacao) {
		this.idInformacao = idInformacao;
	}
}
