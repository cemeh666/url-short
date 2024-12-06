package url.shortner.url_shortner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import url.shortner.url_shortner.builder.UrlBuilder;
import url.shortner.url_shortner.cache.HashCache;
import url.shortner.url_shortner.entity.Hash;
import url.shortner.url_shortner.entity.Url;
import url.shortner.url_shortner.exception.ResourceNotFoundException;
import url.shortner.url_shortner.repository.HashRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrlService {
    private final HashCache hashCache;
    private final HashRepository hashRepository;
    private final UrlBuilder urlBuilder;
    private final UrlCacheRepository urlCacheRepository;
    private final UrlRepository urlRepository;

    @Value("${scheduler.lifetime_days}")
    private int lifetimeDays;

    @Transactional
    public String createHashUrl(String uri) {
        String hash = hashCache.getHash();
        Url url = Url.builder()
                .hash(hash)
                .url(uri)
                .createdAt(LocalDateTime.now())
                .build();

        urlRepository.save(url);
        urlCacheRepository.save(url);

        return urlBuilder.makeUrl(url.getHash());
    }

    @Transactional
    public void removeExpiredUrls() {
        LocalDate expireDate = LocalDate.now().minusDays(lifetimeDays);
        List<String> hashes = urlRepository.getAndDeleteUrlsByDate(expireDate);
        List<Hash> hashesEntity = hashes.stream()
                .map(Hash::new)
                .toList();
        hashRepository.saveAll(hashesEntity);
        urlCacheRepository.deleteHashes(hashes);
    }

    @Transactional(readOnly = true)
    public String getOriginalUrl(String hash) {
        String originalUrl = urlCacheRepository.findByHash(hash);

        if (originalUrl != null) {
            return originalUrl;
        }

        Url url = urlRepository.findById(hash).orElseThrow(() -> new ResourceNotFoundException(hash));

        return url.getUrl();
    }
}
