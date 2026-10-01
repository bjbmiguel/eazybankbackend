package bigu.eazybankbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*
Opcional, visto que todas as entidade e repositórios estão sob a mesma folder 'bigu.eazybankbackend'
@EntityScan("bigu.eazybankbackend.model")
@EnableJpaRepositories("bigu.eazybankbackend.repository") // diz ao spring onde encontra os repositórios para adicionar ao contexto do JPA/Hibernete

 */
public class EazybankbackendApplication  {

	public static void main(String[] args) {
		SpringApplication.run(EazybankbackendApplication.class, args);
	}


}
