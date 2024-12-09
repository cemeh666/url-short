package url.shortner.url_shortner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import url.shortner.url_shortner.generator.Base62Encoder;
import url.shortner.url_shortner.generator.HashGenerator;
import url.shortner.url_shortner.repository.HashRepository;
import url.shortner.url_shortner.service.HashJdbcRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static url.shortner.url_shortner.utils.TestData.generateHashes;

@ExtendWith(MockitoExtension.class)
public class HashGeneratorTest {
    @Mock
    private HashRepository hashRepository;
    @Mock
    private HashJdbcRepository hashJdbcRepository;
    @Spy
    private Base62Encoder encoder;
    @InjectMocks
    private HashGenerator hashGenerator;

    private int batchSize = 100;
    private double minPercentHashes = 20.0;

    @BeforeEach
    void setUp() throws Exception {
        Field batchSize = HashGenerator.class.getDeclaredField("batchSize");
        Field minPercentHashes = HashGenerator.class.getDeclaredField("minPercentHashes");
        batchSize.setAccessible(true);
        minPercentHashes.setAccessible(true);
        batchSize.set(hashGenerator, this.batchSize);
        minPercentHashes.set(hashGenerator, this.minPercentHashes);
    }

    @Test
    public void testGetHashes() {
        List<String> hashes = generateHashes(100, 6);

        when(hashRepository.getHashBatch(hashes.size())).thenReturn(hashes);
        when(hashRepository.getHashesSize()).thenReturn((long) hashes.size());
        List<String> result = hashGenerator.getHashes(hashes.size());

        assertEquals(result, hashes);
    }

    @Test
    public void testGetHashesLowHashes() {
        int amount = 20;
        List<String> hashes = generateHashes(10, 6);

        List<Long> range = LongStream.range(0, batchSize).boxed().toList();
        when(hashRepository.getHashesSize()).thenReturn((long) hashes.size());
        when(hashRepository.getUniqueNumbers(batchSize)).thenReturn(range);

        List<String> result = hashGenerator.getHashes(amount);

        assertEquals(result.size(), 20);
    }
}
