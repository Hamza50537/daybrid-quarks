package de.hiqs.daybird.api.shared.domains.error.exception;

import de.hiqs.daybird.api.shared.domains.error.ErrorCodeEnum;

public class ErrorCodeDtoMapper {

    private ErrorCodeDtoMapper() {
    }

    public static ErrorCodeDto mapFrom(Exception exception) {
        var message = getI18nErrorMessage(ErrorCodeEnum.EXCEPTION);

        return new ErrorCodeDto(ErrorCodeEnum.EXCEPTION.getErrorId(),
                ErrorCodeEnum.EXCEPTION.getErrorCode(),
                message, exception.getMessage());
    }

    public static ErrorCodeDto mapFrom(RuntimeException exception) {
        var message = getI18nErrorMessage(ErrorCodeEnum.EXCEPTION);

        return new ErrorCodeDto(ErrorCodeEnum.RUNTIME_EXCEPTION.getErrorId(),
                ErrorCodeEnum.RUNTIME_EXCEPTION.getErrorCode(),
                message, exception.getMessage());
    }


    public static ErrorCodeDto mapFrom(DaybirdApiException spException) {
        var message = getI18nErrorMessage(spException.getErrorCodeEnum());

        return new ErrorCodeDto(spException.getErrorCodeEnum().getErrorId(),
                spException.getErrorCodeEnum().getErrorCode(),
                message, spException.getMessage());
    }

    private static String getI18nErrorMessage(ErrorCodeEnum errorCodeEnum) {
        // possible to translate ErrorCode to localized message with local from user
        // i18nService.getMessageForKeyAndLocal(errorCodeEnum. getErrorCode(), locale)
        return errorCodeEnum.getErrorDescription();
    }
}
