package url.shortener.url_redirect_service.service;

import lombok.AllArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Service;
import url.shortener.url_redirect_service.entity.UrlMapping;
import url.shortener.url_redirect_service.repository.UrlMappingRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RedirectService {

    private final UrlMappingRepository urlMappingRepository;

    public String getLongUrl(String shortCode) {
        Optional<UrlMapping> urlMappingOption = urlMappingRepository.findByShortCode(shortCode);
        String longUrl = urlMappingOption.map(UrlMapping::getLongUrl).orElse(null);
        if (StringHelper.isEmpty(longUrl)) {
            throw new RuntimeException("Short Code not found: " + shortCode);
        }
        return longUrl;
    }
}
