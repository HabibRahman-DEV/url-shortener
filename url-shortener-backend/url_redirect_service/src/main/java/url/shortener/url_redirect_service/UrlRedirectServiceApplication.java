package url.shortener.url_redirect_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import url.shortener.url_redirect_service.config.RedisProperties;

@SpringBootApplication
@EnableConfigurationProperties(RedisProperties.class)
public class UrlRedirectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlRedirectServiceApplication.class, args);
		System.out.println("Hello from Redirect Service");
	}

}
