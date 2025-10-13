package ru.practicum.shareit.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.common.advice.dto.ErrorResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {
    // 400: невалидный JSON / пустое тело
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(Exception ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(error("BAD_REQUEST", "Некорректное тело запроса", req.getRequestURI()));
    }

    // 400: @Valid на @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(error("VALIDATION_FAILED", details, req.getRequestURI()));
    }

    // 400: @Validated на @PathVariable/@RequestParam и др.
    @ExceptionHandler({ConstraintViolationException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolation(Exception ex, HttpServletRequest req) {
        String details;
        if (ex instanceof ConstraintViolationException cve) {
            details = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
        } else if (ex instanceof BindException be) {
            details = be.getAllErrors().stream()
                    .map(e -> e.getObjectName() + ": " + (e.getDefaultMessage() == null ? "invalid" : e.getDefaultMessage()))
                    .collect(Collectors.joining("; "));
        } else {
            details = "Нарушение ограничений валидации";
        }
        return ResponseEntity.badRequest()
                .body(error("VALIDATION_FAILED", details, req.getRequestURI()));
    }

    // 400: отсутствует обязательный заголовок
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex,
                                                             HttpServletRequest req) {
        String msg = "Отсутствует обязательный заголовок '%s'".formatted(ex.getHeaderName());
        return ResponseEntity.badRequest()
                .body(error("MISSING_HEADER", msg, req.getRequestURI()));
    }

    // 400: неправильный тип параметра / enum и т.п.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest req) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "корректный тип";
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] allowed = ex.getRequiredType().getEnumConstants();
            expected += " (" + Arrays.stream(allowed).map(Object::toString).collect(Collectors.joining(", ")) + ")";
        }
        String msg = "Неверное значение для параметра '%s': ожидается %s".formatted(ex.getName(), expected);
        return ResponseEntity.badRequest().body(error("TYPE_MISMATCH", msg, req.getRequestURI()));
    }

    // 400: отсутствует обязательный query-параметр
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(Exception ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(error("MISSING_PARAM", ex.getMessage(), req.getRequestURI()));
    }

    // 400: твои собственные IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(error("INVALID_ARGUMENT", ex.getMessage(), req.getRequestURI()));
    }

    // 404: нет обработчика/ресурса
    @ExceptionHandler({
            org.springframework.web.servlet.NoHandlerFoundException.class,
            org.springframework.web.servlet.resource.NoResourceFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNoHandler(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", "Ресурс не найден", req.getRequestURI()));
    }

    // 405: метод не поддержан
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(error("METHOD_NOT_ALLOWED", "Метод не поддержан", req.getRequestURI()));
    }

    // 500: всё прочее (только для ошибок внутри gateway)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected at {}:", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_ERROR", "Внутренняя ошибка шлюза", req.getRequestURI()));
    }

    private ErrorResponse error(String code, String message, String path) {
        return new ErrorResponse(code, message, path, Instant.now());
    }
}
