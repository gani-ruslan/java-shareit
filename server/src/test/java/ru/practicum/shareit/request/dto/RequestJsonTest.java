package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ShortItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestJsonTest {

    @Autowired JacksonTester<RequestFullResponseDto> json;

    @Test
    void serialize_requestFullResponseDto() throws Exception {
        var dto = new RequestFullResponseDto(
                5L,
                "Нужна дрель",
                42L, // requesterId
                LocalDateTime.of(2030,1,1,10,0),
                List.of(new ShortItemDto(1L, "Дрель", 2L))
        );

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужна дрель");
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(42);
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId").isEqualTo(2);
    }
}
