package url.shortener.url_redirect_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import url.shortener.url_redirect_service.service.RedirectService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
@AllArgsConstructor
@Slf4j
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/redirect")
    public ResponseEntity<String> redirect(@RequestParam String shortCode) {
        try {
            String longUrl = redirectService.getLongUrl(shortCode);

            // return 302 redirect
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(longUrl))
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
