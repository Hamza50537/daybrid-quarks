package de.hiqs.daybird.api.shared.domains.error.handler;

import de.hiqs.daybird.api.shared.domains.error.exception.DaybirdApiException;
import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDto;
import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDtoMapper;
import de.hiqs.daybird.api.shared.domains.error.logger.ErrorCodeDtoLogger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DaybirdApiExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ErrorCodeDto> mapException(DaybirdApiException exception) {
        var errorCodeDto = ErrorCodeDtoMapper.mapFrom(exception);
        ErrorCodeDtoLogger.error(errorCodeDto, exception);
        return RestResponse.status(exception.getHttpStatus(), errorCodeDto);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorCodeDto> mapException(WebApplicationException exception) {
        var errorCodeDto = ErrorCodeDtoMapper.mapFrom(exception);
        ErrorCodeDtoLogger.error(errorCodeDto, exception);
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, errorCodeDto);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorCodeDto> mapException(RuntimeException exception) {
        var errorCodeDto = ErrorCodeDtoMapper.mapFrom(exception);
        ErrorCodeDtoLogger.error(errorCodeDto, exception);
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, errorCodeDto);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorCodeDto> mapException(Exception exception) {
        var errorCodeDto = ErrorCodeDtoMapper.mapFrom(exception);
        ErrorCodeDtoLogger.error(errorCodeDto, exception);
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, errorCodeDto);
    }
}
