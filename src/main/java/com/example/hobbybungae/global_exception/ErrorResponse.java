package com.example.hobbybungae.global_exception;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private ErrorCodeFormat error;
    private List<ErrorDetail> errorDetails;

    public ErrorResponse(ErrorCodeFormat error, List<ErrorDetail> errorDetails) {
        this.error = error;
        this.errorDetails = errorDetails;
    }

    public static ErrorResponse of(final GlobalErrorCode globalErrorCode, final BindingResult bindingResult) {
        return new ErrorResponse(globalErrorCode.getErrorCodeFormat(), ErrorDetail.of(bindingResult));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ErrorDetail {
        private String field;
        private String value;
        private String reason;

        public ErrorDetail(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<ErrorDetail> of(BindingResult bindingResult) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(fieldError ->
                            new ErrorDetail(fieldError.getField(),
                                    Objects.requireNonNull(fieldError.getRejectedValue()).toString(),
                                    fieldError.getDefaultMessage()))
                    .toList();
        }
    }
}
