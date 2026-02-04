package url.shortener.url_write_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import url.shortener.url_write_service.dto.CreateUrlRequest;
import url.shortener.url_write_service.dto.CreateUrlResponse;
import url.shortener.url_write_service.entity.UrlMapping;
import url.shortener.url_write_service.repository.UrlMappingRepository;
import url.shortener.url_write_service.utils.Base62Encoder;

import java.time.LocalDateTime;

@Service
public class UrlService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${url-shortener.redis.counter-key}")
    private String redisCounterKey;

    @Value("${url-shortener.base-url}")
    private String baseUrl;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    public CreateUrlResponse createShortUrl(CreateUrlRequest request) {
        Long id = getNextIdFromRedisCounter();
        String shortCode = Base62Encoder.encode(id);

        UrlMapping urlMapping = UrlMapping.builder()
                .longUrl(request.getLongUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusHours(request.getKeepLiveInHour() != null ? request.getKeepLiveInHour() : 24))
                .build();
        urlMappingRepository.save(urlMapping);

        String shortUrl = buildShortUrl(shortCode);

        return CreateUrlResponse.builder()
                .longUrl(request.getLongUrl())
                .shortCode(shortCode)
                .shortUrl(shortUrl)
                .createdAt(urlMapping.getCreatedAt())
                .expireAt(urlMapping.getExpireAt())
                .build();
    }

    private Long getNextIdFromRedisCounter() {
        return redisTemplate.opsForValue().increment(redisCounterKey);
    }

    public String buildShortUrl(String shortCode) {
        return baseUrl + '/' + shortCode;
    }
}
