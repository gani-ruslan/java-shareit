package ru.practicum.shareit.request.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    Long id;
    String description;
    User requestor;
    LocalDate created;
}
