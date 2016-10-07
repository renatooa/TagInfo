package taginfo.renato.com.br.taginfoandroid.http.cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;


/**
 * @author Desenvolvimento
 * 
 */
public class HttpCliente {

	private int bufferSize = 8192;
	private int timeOut = 5000; // ms
	private int readTimeOut = timeOut; // ms

	private String responseMessage = "";
	private int responseCode = 0;

	public HttpCliente() {
	}

	/**
	 * 
	 * @param urlBaseSpec
	 * @param urlParams
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String enviarRequisicao(String urlBaseSpec, String[] urlParams, RequestMethod requestMethod, String content)
			throws Exception {

		HttpURLConnection con = null;
		try {

			String response = null;

			con = criarUrlConnection(urlBaseSpec, urlParams, requestMethod);

			configureConnection(con);

			if (content != null) {
				addContent(content, con);
			}

			this.responseCode = con.getResponseCode();
			this.responseMessage = con.getResponseMessage();

			if (this.responseCode == HttpURLConnection.HTTP_OK) {
				response = capturarResponse(con);
			}

			return response;
		} catch (Exception ex) {
			/*
			 * Testa se conseguiu recuperar o responseCode um. Quando n�o
			 * consegue conectar no servidor a exce��o est� vindo nulo.
			 */
			if (this.responseCode == 0)
				this.responseCode = HttpURLConnection.HTTP_UNAVAILABLE;

			throw ex;
		} finally {

			if (con != null) {
				con.disconnect();
			}

			if (this.responseCode != 200) {
				throw new HttpExcecao(this.responseCode);
			}
		}
	}

	/**
	 * 
	 * @param connection
	 */
	protected void configureConnection(URLConnection connection) {

		this.responseCode = 0;
		this.responseMessage = "";

		connection.setConnectTimeout(timeOut);
		connection.setReadTimeout(readTimeOut);
	}

	protected void addContent(String content, HttpURLConnection con) throws IOException {

		DataOutputStream dos = new DataOutputStream(con.getOutputStream());

		byte[] contentByte = new String(content.getBytes(), Charset.forName(getEncoding())).getBytes();

		try {
			dos.write(contentByte);
		} finally {
			dos.close();
		}
	}

	protected HttpURLConnection criarUrlConnection(String urlBaseSpec, String[] urlParams, RequestMethod requestMethod)
			throws MalformedURLException, IOException, ProtocolException {

		String params = this.mountUrlParams(urlParams);
		String urlSpec = urlBaseSpec + params;

		URL url = new URL(urlSpec);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setDoInput(true); // Allow Inputs
		con.setDoOutput(RequestMethod.POST.equals(requestMethod)); // Allow
																	// Outputs
		con.setUseCaches(false); // Don't use a Cached Copy

		String requestMethodStr = requestMethod.getMethodHTTP();
		con.setRequestMethod(requestMethodStr);

		return con;
	}

	protected String capturarResponse(HttpURLConnection con) throws IOException {

		Scanner scanner = null;

		try {

			InputStream inputStream = getInputStreanResponse(con);

			scanner = new Scanner(inputStream);

			StringBuffer buffer = new StringBuffer();

			while (scanner.hasNext()) {
				buffer.append(scanner.nextLine());
			}

			return buffer.toString();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	protected InputStream getInputStreanResponse(HttpURLConnection con) throws IOException {

		String encoding = con.getHeaderField("Content-Encoding");

		if ("gzip".equalsIgnoreCase(encoding)) {
			return new GZIPInputStream(con.getInputStream());
		} else {
			return con.getInputStream();
		}
	}

	/**
	 * Monta uma lista de parametros de acordo com os padr�es da URL.
	 * 
	 * @param urlParams
	 *            Array de parametros
	 * @return
	 */
	protected String mountUrlParams(String[] urlParams) {
		String params = "";
		try {
			for (String param : urlParams) {
				int pos = param.indexOf("=");
				String p2 = param.substring(0, pos + 1) + URLEncoder.encode(param.substring(pos + 1), getEncoding());

				params += (params.length() > 0 ? "&" : "") + p2;
			}
			params = (params.length() > 0 ? "?" : "") + params;
		} catch (Exception ex) {

		}
		return params;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public String getEncoding() {
		return "ISO-8859-1";
	}

	public enum RequestMethod {
		GET, POST;

		public String getMethodHTTP() {
			return name();
		}
	}

	public static String getUrlInformacao(String tagId){
		return "http://192.168.0.12:8080/info/"+tagId;
	}
}
