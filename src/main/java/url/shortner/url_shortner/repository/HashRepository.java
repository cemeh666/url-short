package url.shortner.url_shortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import url.shortner.url_shortner.entity.Hash;

import java.util.List;

@Repository
public interface HashRepository extends JpaRepository<Hash, String> {
    @Query(nativeQuery = true, value = """
            SELECT nextval('unique_hash_number_seq') FROM generate_series(1, :maxRange)
            """)
    List<Long> getUniqueNumbers(@Param("maxRange") int maxRange);

    @Query(nativeQuery = true, value = """
            DELETE FROM hash WHERE hash IN (
                SELECT hash.hash FROM hash LIMIT :batchSize FOR UPDATE SKIP LOCKED
            ) RETURNING *
            """)
    List<String> getHashBatch(@Param("batchSize") int batchSize);

    @Query(nativeQuery = true, value = "SELECT COUNT(h.hash) FROM hash h")
    Long getHashesSize();
}
