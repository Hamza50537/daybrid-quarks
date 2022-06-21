package de.hiqs.daybird.api.shared.domains.error;

import javax.ws.rs.core.Response;

public enum ErrorCodeEnum {

    /* ************************************************************************************************************ */
    /* Global Error Codes */
    /* ************************************************************************************************************ */
    RUNTIME_EXCEPTION(50, "ERROR.RUNTIME_EXCEPTION", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    EXCEPTION(60, "ERROR.EXCEPTION", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    MALFORMED_URL_EXCEPTION(65, "ERROR.EXCEPTION", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    IO_EXCEPTION(70, "ERROR.IO_EXCEPTION", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    JSON_SERIALIZATION_ERROR(75, "ERROR.JSON_SERIALIZATION_ERROR", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    JSON_DESERIALIZATION_ERROR(77, "ERROR.JSON_DESERIALIZATION_ERROR", ErrorCodeEnum.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),

    /* ************************************************************************************************************ */
    /* Custom Error Codes */
    /* ************************************************************************************************************ */
    SERVICE_NOT_AVAILABLE(100, "ERROR.SERVICE_NOT_AVAILABLE", "Service not available", Response.Status.SERVICE_UNAVAILABLE),
    NOT_FOUND(120, "ERROR.NOT_FOUND", "Not found", Response.Status.NOT_FOUND),

    INVALID_ARGUMENT(140, "ERROR.INVALID_ARGUMENT", "Invalid argument", Response.Status.BAD_REQUEST),
    MISSING_ARGUMENT(180, "ERROR.MISSING_ARGUMENT", "Missing argument", Response.Status.BAD_REQUEST),

    SECURITY_UNAUTHENTICATED(200, "ERROR.SECURITY_UNAUTHENTICATED", "User is unauthenticated. Please login.", Response.Status.UNAUTHORIZED),
    SECURITY_UNAUTHORIZED(220, "ERROR.SECURITY_UNAUTHORIZED", "User is unauthorized and has not the correct rights.", Response.Status.UNAUTHORIZED),

    /* ************************************************************************************************************ */
    /* Custom App Error Codes */
    /* ************************************************************************************************************ */

    OBJECT_WITH_UUID_NOT_FOUND(300, "ERROR.OBJECT_WITH_UUID_NOT_FOUND", "Object with uuid not found", Response.Status.BAD_REQUEST),
    OBJECT_FOR_UPDATE_IS_NULL(305, "ERROR.OBJECT_FOR_UPDATE_IS_NULL", "Object for update is null", Response.Status.BAD_REQUEST),
    DUPLICATE_UUID(307, "ERROR.DUPLICATE_UUID", "Duplicate UUID found", Response.Status.BAD_REQUEST),
    NOT_ALLOWED(310, "ERROR.NOT_ALLOWED", "This method is not allowed to use!", Response.Status.BAD_REQUEST);

    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    private final int errorId;
    private final String errorCode;
    private final String errorDescription;
    private final Response.Status httpStatus;

    ErrorCodeEnum(int errorId, String errorCode, String errorDescription, Response.Status httpStatus) {
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.httpStatus = httpStatus;
    }

    public int getErrorId() {
        return errorId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }
}
