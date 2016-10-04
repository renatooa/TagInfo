package taginfo.renato.com.br.taginfoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TagInfoAplication {

	public static void main(String[] args) {
		SpringApplication.run(TagInfoAplication.class, args);
	}
}
