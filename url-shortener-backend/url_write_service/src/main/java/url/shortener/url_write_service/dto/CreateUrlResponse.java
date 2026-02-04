package url.shortener.url_write_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlResponse {
    private String longUrl;
    private String shortCode;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;
}
