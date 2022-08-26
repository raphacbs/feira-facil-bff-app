package br.com.coelho;

import br.com.coelho.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FeiraFacilAppBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeiraFacilAppBffApplication.class, args);
	}

}
