package url.shortener.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import url.shortener.auth_service.config.JwtSecretProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtSecretProperties.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
		System.out.println("Hello from Auth Service");
	}
}
