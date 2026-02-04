package url.shortener.url_write_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import url.shortener.url_write_service.dto.CreateUrlRequest;
import url.shortener.url_write_service.dto.CreateUrlResponse;
import url.shortener.url_write_service.service.UrlService;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<CreateUrlResponse> createShortUrl(@RequestBody CreateUrlRequest request) {
        CreateUrlResponse response = urlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
