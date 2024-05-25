package com.aburakkontas.manga_cdn.domain.primitives;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Result<T> {
    private final T value;
    private final boolean success;
    private final String errorMessage;
    private final int errorCode;

    private Result(T value, boolean success, String errorMessage, int errorCode) {
        this.value = value;
        this.success = success;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, true, null, 200);
    }

    public static <T> Result<T> failure(String errorMessage, int errorCode) {
        return new Result<>(null, false, errorMessage, errorCode);
    }

    public static <T> Result<T> successWithoutValue() {
        return success(null);
    }
}
