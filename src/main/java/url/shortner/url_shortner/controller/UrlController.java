package url.shortner.url_shortner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import url.shortner.url_shortner.dto.CreateUrlDto;
import url.shortner.url_shortner.dto.UrlResponseDto;
import url.shortner.url_shortner.service.UrlService;

@RequiredArgsConstructor
@RestController
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/url")
    public UrlResponseDto createHashUrl(@RequestBody @Valid CreateUrlDto dto) {
        String result = urlService.createHashUrl(dto.getUrl());

        return new UrlResponseDto(result);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirect(@PathVariable String hash) {
        String originalUrl = urlService.getOriginalUrl(hash);

        return ResponseEntity
                .status(302)
                .header("Location", originalUrl)
                .build();
    }
}
