package de.hiqs.daybird.api.shared.domains.error.logger;

import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ErrorCodeLogDto {

    /**
     * ErrorId from ErrorCodeEnum
     */
    private final int errorId;
    /**
     * Random generated uuid, each ErrorCodeDto has a new UUID
     */
    private final String uuid;
    /**
     * errorCode from ErrorCodeEnum
     */
    private final String errorCode;
    /**
     * Error Message
     */
    private final String errorMessage;
    /**
     * Detail error message
     */
    private final String detailMessage;
    /**
     * Timestamp of the created ErrorCodeDto instance
     */
    private final String timestamp;

    private final String exceptionType;

    /**
     * StackTraceElement list to log
     */
    private final List<StackTraceElement> stackTraceElements;

    public ErrorCodeLogDto(ErrorCodeDto errorCodeDto, List<StackTraceElement> stackTraceElements, String exceptionType) {
        this.errorId = errorCodeDto.getErrorId();
        this.uuid = errorCodeDto.getUuid();
        this.errorCode = errorCodeDto.getErrorCode();
        this.errorMessage = errorCodeDto.getErrorMessage();
        this.detailMessage = errorCodeDto.getDetailMessage();
        this.timestamp = errorCodeDto.getTimestamp();
        this.exceptionType = exceptionType;
        this.stackTraceElements = stackTraceElements;
    }
}
