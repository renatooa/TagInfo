package taginfo.renato.com.br.taginfoandroid.http.cliente;


public class HttpExcecao extends Exception {


	private static final long serialVersionUID = 1L;

	private int status = 0;

	public HttpExcecao(int httpResponseCode) {
		super("Problemas de Conexão " + httpResponseCode);
		status = httpResponseCode;
	}

	public int getStatus() {
		return status;
	}
}
