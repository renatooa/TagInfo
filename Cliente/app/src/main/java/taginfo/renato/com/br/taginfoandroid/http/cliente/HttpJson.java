package taginfo.renato.com.br.taginfoandroid.http.cliente;

import java.io.Serializable;
import java.net.URLConnection;

import com.google.gson.Gson;

public class HttpJson extends HttpCliente {

	public HttpJson() {
		setReadTimeOut(60000);
	}

	@Override
	protected void configureConnection(URLConnection connection) {
		super.configureConnection(connection);

		connection.addRequestProperty("content-type", "application/json");
		connection.addRequestProperty("keep-alive", "true");
	}

	public <E extends Serializable> E enviarGet(Class<E> retorno, String urlBaseSpec, String... urlParams)
			throws Exception {

		String resposta = enviarRequisicao(urlBaseSpec, urlParams, RequestMethod.GET, null);

		Gson gson = new Gson();

		return gson.fromJson(resposta, retorno);
	}

	public <E extends Serializable> E enviarPost(Class<E> retorno, String urlBaseSpec, Serializable parametro)
			throws Exception {

		Gson gson = new Gson();

		String content = getContent(parametro, gson);

		String resposta = enviarRequisicao(urlBaseSpec, null, RequestMethod.POST, content);

		return gson.fromJson(resposta, retorno);
	}

	private String getContent(Serializable parametro, Gson gson) {

		if (parametro instanceof String) {
			return parametro.toString();
		} else {
			return gson.toJson(parametro);
		}
	}
}
