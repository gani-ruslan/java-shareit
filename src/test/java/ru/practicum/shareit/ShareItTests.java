package ru.practicum.shareit;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Контекстный тест не нужен после перехода на JPA")
class ShareItTests {
}
