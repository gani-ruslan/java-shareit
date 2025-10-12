package ru.practicum.shareit.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.practicum.shareit.common.advice.dto.ErrorResponse;
import ru.practicum.shareit.common.exceptions.AppException;
import ru.practicum.shareit.common.exceptions.NotFoundException;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleApp(AppException ex,
                                                   HttpServletRequest req) {

        log.warn("Business error [{}] at {}: {}", ex.getCode(), req.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(ex.getStatus())
                .body(error(ex.getCode(), ex.getMessage(), req.getRequestURI())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex,
                                                        HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        var root = org.springframework.core.NestedExceptionUtils.getMostSpecificCause(ex);
        var msg = (root.getMessage() != null && root.getMessage().toLowerCase().contains("uq_users_email"))
                ? "Email уже используется"
                : "Нарушение ограничений целостности данных";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("CONFLICT", msg, req.getRequestURI()));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTx(TransactionSystemException ex, HttpServletRequest req) {
        var root = NestedExceptionUtils.getMostSpecificCause(ex);
        if (root instanceof ConstraintViolationException cve) {
            String details = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.badRequest().body(error("VALIDATION_FAILED", details, req.getRequestURI()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_ERROR", "Внутренняя ошибка при сохранении данных", req.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(Exception ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(error("BAD_REQUEST", "Некорректное тело запроса", req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest req) {

        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(error("VALIDATION_FAILED", details, req.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest req) {

        String details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(error("VALIDATION_FAILED", details, req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {

        String incident = UUID.randomUUID().toString();
        log.error("Unexpected [{}] at {}:", incident, req.getRequestURI(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_ERROR", "Внутренняя ошибка. Код: " + incident, req.getRequestURI()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex,
                                                             HttpServletRequest req) {
        log.warn("Missing header: {}", ex.getHeaderName());
        String msg = "Отсутствует обязательный заголовок '%s'".formatted(ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error("MISSING_HEADER", msg, req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "корректный тип";
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] allowed = ex.getRequiredType().getEnumConstants();
            expected += " (" + java.util.Arrays.stream(allowed).map(Object::toString).collect(Collectors.joining(", ")) + ")";
        }
        String msg = "Неверное значение для параметра '%s': ожидается %s".formatted(ex.getName(), expected);
        return ResponseEntity.badRequest().body(error("TYPE_MISMATCH", msg, req.getRequestURI()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(Exception ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(error("MISSING_PARAM", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandler(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", "Ресурс не найден", req.getRequestURI()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(error("METHOD_NOT_ALLOWED", "Метод не поддержан", req.getRequestURI()));
    }

    private ErrorResponse error(String code, String message, String path) {
        return new ErrorResponse(code, message, path, Instant.now());
    }
}
