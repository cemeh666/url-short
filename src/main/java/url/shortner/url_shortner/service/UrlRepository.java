package url.shortner.url_shortner.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import url.shortner.url_shortner.entity.Url;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {
    @Modifying
    @Query(nativeQuery = true, value = """
            DELETE FROM url WHERE url.hash IN (
                SELECT url.hash FROM url WHERE url.created_at < :date FOR UPDATE SKIP LOCKED
            )
            RETURNING url.hash
            """)
    List<String> getAndDeleteUrlsByDate(@Param("date") LocalDate date);
}
