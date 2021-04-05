package org.geekhub.doctorsregistry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DoctorsRegistryTest {

    @Test
    void context_loads() {
        Assertions.assertDoesNotThrow(() -> {});
    }

}