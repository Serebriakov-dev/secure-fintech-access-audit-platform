package dev.serebriakov.fintech.audit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
class AuditServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}