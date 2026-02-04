package url.shortener.url_write_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateUrlRequest {
    private String longUrl;
    private Long keepLiveInHour;
}
