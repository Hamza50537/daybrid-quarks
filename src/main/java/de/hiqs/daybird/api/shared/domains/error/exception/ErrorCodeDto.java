package de.hiqs.daybird.api.shared.domains.error.exception;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ToString
@Getter
public class ErrorCodeDto {
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

    public ErrorCodeDto(int errorId, String errorCode, String errorMessage, String detailMessage) {
        this.uuid = UUID.randomUUID().toString();
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detailMessage = detailMessage;
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}
