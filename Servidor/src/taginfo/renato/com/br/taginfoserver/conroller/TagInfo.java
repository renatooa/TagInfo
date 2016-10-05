package taginfo.renato.com.br.taginfoserver.conroller;

import java.io.Serializable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import taginfo.renato.com.br.taginfoservice.modelo.InformacaoProduto;

@Controller
@RequestMapping("/tag")
public class TagInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@CrossOrigin(origins = "*")
	@ResponseBody
	@RequestMapping(path = "/oi", method = RequestMethod.GET)
	public String texto() {
		return "teste on ok";
	}

	@CrossOrigin(origins = "*")
	@ResponseBody
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = "application/json")
	public InformacaoProduto info() {

		InformacaoProduto info = new InformacaoProduto();
		
		return info;
	}
}
