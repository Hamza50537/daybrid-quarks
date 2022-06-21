package de.hiqs.daybird.api.shared.domains.error;

import de.hiqs.daybird.api.shared.domains.error.exception.DaybirdApiException;
import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDto;
import de.hiqs.daybird.api.shared.domains.error.exception.ErrorCodeDtoMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@QuarkusTest
class ErrorCodeEnumTest {

    Logger LOGGER = LoggerFactory.getLogger(ErrorCodeEnumTest.class);

    @Test
    void uniqueErrorIdTest() {
        Map<Integer, ErrorCodeEnum> enumIds = new HashMap<>();
        Arrays.stream(ErrorCodeEnum.values()).toList().forEach(errorCodeEnum -> {
            if (enumIds.containsKey(errorCodeEnum.getErrorId())) {
                Assertions.fail("ErrorId was a duplicate: " + errorCodeEnum.getErrorId());
            } else {
                enumIds.put(errorCodeEnum.getErrorId(), errorCodeEnum);
                LOGGER.info("Enum ErrorID: " + errorCodeEnum.getErrorId());
            }
        });
        Assertions.assertEquals(ErrorCodeEnum.values().length, enumIds.size());
    }

    @Test
    void errorMapperTest() {
        ErrorCodeDto errorCodeDto = new ErrorCodeDto(300, "ERROR.OBJECT_WITH_UUID_NOT_FOUND", "Object with uuid not found", "Test Uuid not found");
        ErrorCodeDto errorTest = ErrorCodeDtoMapper.mapFrom(new DaybirdApiException(ErrorCodeEnum.OBJECT_WITH_UUID_NOT_FOUND, "Test Uuid not found"));

        LOGGER.info("Dto: " + errorCodeDto.getErrorId() + " / " + "Test: " + errorTest.getErrorId());
        LOGGER.info("Dto: " + errorCodeDto.getErrorCode() + " / " + "Test: " + errorTest.getErrorCode());
        LOGGER.info("Dto: " + errorCodeDto.getErrorMessage() + " / " + "Test: " + errorTest.getErrorMessage());
        LOGGER.info("Dto: " + errorCodeDto.getDetailMessage() + " / " + "Test: " + errorTest.getDetailMessage());

        Assertions.assertEquals(errorCodeDto.getErrorId(), errorTest.getErrorId());
        Assertions.assertEquals(errorCodeDto.getErrorCode(), errorTest.getErrorCode());
        Assertions.assertEquals(errorCodeDto.getErrorMessage(), errorTest.getErrorMessage());
        Assertions.assertEquals(errorCodeDto.getDetailMessage(), errorTest.getDetailMessage());
    }
}