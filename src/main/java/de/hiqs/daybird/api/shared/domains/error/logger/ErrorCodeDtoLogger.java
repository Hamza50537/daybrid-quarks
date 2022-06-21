package de.hiqs.daybird.api.shared.domains.error.logger;

import de.hiqs.daybird.api.shared.domains.error.exception.DaybirdApiException;
import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorCodeDtoLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodeDtoLogger.class);
    private static final int ERROR_LOG_LINE_COUNT = 10;

    private ErrorCodeDtoLogger() {
    }

    public static void error(ErrorCodeDto errorCodeDto, Exception exception) {
        List<StackTraceElement> stacktraceElements = Arrays.asList(exception.getStackTrace());
        String exceptionType = determineExceptionType(exception);

        var errorCodeLogDto = new ErrorCodeLogDto(errorCodeDto,
                filterStackTraceElements(stacktraceElements), exceptionType);

        String errorMessage = new JsonUtils().toJson(errorCodeLogDto);
        LOGGER.error(errorMessage);
    }

    private static String determineExceptionType(Exception exception) {
        if (exception instanceof DaybirdApiException daybirdApiException) {
            return daybirdApiException.getBaseException().toString();
        }
        return exception.toString();
    }

    private static List<StackTraceElement> filterStackTraceElements(List<StackTraceElement> stackTraceElements) {
        List<StackTraceElement> filteredStackTraceElements = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stackTraceElements)) {
            var logLineCount = 0;
            for (StackTraceElement element : stackTraceElements) {
                if (element.getClassName().contains("de.hiqs.daybird")
                        && element.getLineNumber() > 0) {
                    filteredStackTraceElements.add(element);
                    logLineCount += 1;
                    if (ERROR_LOG_LINE_COUNT == logLineCount) {
                        break;
                    }
                }
            }
        }
        return filteredStackTraceElements;
    }
}
