package ru.practicum.shareit.item.dto;

public record ItemPatchDto(
    String name,
    String description,
    Boolean available,
    Long ownerId
) {}
