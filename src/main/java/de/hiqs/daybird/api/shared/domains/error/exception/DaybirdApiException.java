package de.hiqs.daybird.api.shared.domains.error.exception;

import de.hiqs.daybird.api.shared.domains.error.ErrorCodeEnum;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DaybirdApiException extends WebApplicationException {

    private final ErrorCodeEnum errorCodeEnum;
    private final String message;
    private final Response.Status httpStatus;
    private final Throwable baseException;

    public DaybirdApiException(ErrorCodeEnum errorCodeEnum, String message, Response.Status httpStatus, Throwable baseException) {
        this.errorCodeEnum = errorCodeEnum;
        this.message = message;
        this.httpStatus = httpStatus;
        this.baseException = (baseException != null ? baseException: this);
    }

    public DaybirdApiException(ErrorCodeEnum errorCodeEnum) {
        this(errorCodeEnum, errorCodeEnum.getErrorDescription(), errorCodeEnum.getHttpStatus(), null);
    }

    public DaybirdApiException(ErrorCodeEnum errorCodeEnum, String message) {
        this(errorCodeEnum, message, errorCodeEnum.getHttpStatus(), null);
    }

    public DaybirdApiException(ErrorCodeEnum errorCodeEnum, String message, Exception exception) {
        this(errorCodeEnum, message, errorCodeEnum.getHttpStatus(), exception);
    }

    public DaybirdApiException(ErrorCodeEnum errorCodeEnum, Exception exception) {
        this(errorCodeEnum, exception.getMessage(), errorCodeEnum.getHttpStatus(), exception);
    }

    public ErrorCodeEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public Throwable getBaseException() {
        return baseException;
    }
}
