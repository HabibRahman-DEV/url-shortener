package url.shortener.url_redirect_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "url-shortener.redis")
public record RedisProperties(String urlKey, Long cacheTtlHours) {
}
