package url.shortener.url_redirect_service.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import url.shortener.url_redirect_service.config.RedisProperties;
import url.shortener.url_redirect_service.entity.UrlMapping;
import url.shortener.url_redirect_service.repository.UrlMappingRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final RedisTemplate<String, String> redisTemplate;

    private final UrlMappingRepository urlMappingRepository;

    private final RedisProperties redisProperties;


    public String getLongUrl(String shortCode) {
        // check in redis
        String cachedUrl = redisTemplate.opsForValue().get(redisProperties.urlKey() + shortCode);
        if (!StringHelper.isEmpty(cachedUrl)) {
            return cachedUrl;
        }
        // cache miss, fetch from db
        Optional<UrlMapping> urlMappingOption = urlMappingRepository.findByShortCode(shortCode);
        String longUrl = urlMappingOption.map(UrlMapping::getLongUrl).orElse(null);
        if (StringHelper.isEmpty(longUrl)) {
            throw new RuntimeException("Short Code not found: " + shortCode);
        }
        // set in redis
        redisTemplate.opsForValue().set(redisProperties.urlKey() + shortCode, longUrl, redisProperties.cacheTtlHours(), TimeUnit.HOURS);
        return longUrl;
    }
}
