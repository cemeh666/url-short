package url.shortner.url_shortner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import url.shortner.url_shortner.entity.Url;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UrlCacheRepository {
    private final RedisTemplate<String, String> urlRedisTemplate;

    public void save(Url url) {
        urlRedisTemplate.opsForValue().set(url.getHash(), url.getUrl());
    }

    public String findByHash(String hash) {
        return urlRedisTemplate.opsForValue().get(hash);
    }

    public void deleteHashes(List<String> hashes) {
        urlRedisTemplate.delete(hashes);
    }
}
