package org.geekhub.doctorsregistry;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
public class DoctorsRegistryTest extends AbstractTestNGSpringContextTests {

    @Test
    public void context_loads() {
        Assertions.assertDoesNotThrow(() -> {});
    }

}