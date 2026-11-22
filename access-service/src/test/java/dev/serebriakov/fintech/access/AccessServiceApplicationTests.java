package dev.serebriakov.fintech.access;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AccessServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}