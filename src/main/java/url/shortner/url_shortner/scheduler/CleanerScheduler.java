package url.shortner.url_shortner.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import url.shortner.url_shortner.service.UrlService;

@RequiredArgsConstructor
@Component
public class CleanerScheduler {
    private final UrlService urlService;

    @Scheduled(cron = "${scheduler.cron}")
    public void execute() {
        urlService.removeExpiredUrls();
    }
}
