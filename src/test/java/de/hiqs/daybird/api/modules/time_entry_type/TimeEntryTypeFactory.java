package de.hiqs.daybird.api.modules.time_entry_type;

import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypeDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeEntryTypeFactory {

    private static final JsonUtils jsonMapper = new JsonUtils();

    public static Dispatcher getFullDispatcher(boolean accessOk, boolean callOk, int errorCode, String errorMessage) {

        return new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                if (Objects.requireNonNull(recordedRequest.getPath()).contains("auth/realms")) {
                    return accessOk ? getAccessTokenMockResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("POST")) {
                    // TODO : check auth if authentication is active
                    /*
                    if ("Bearer l51PldIdEgkrZ1TOxZ38GzNGiiBW".equals(recordedRequest.getHeaders().get("Authorization"))) {
                        return callOk ? getPostResponse() : get4xxErrorResponse(errorCode, errorMessage);
                    }
                    return get4xxErrorResponse(401, "Missing Authorization Bearer Token");
                     */
                    return callOk ? getPostResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("GET")) {
                    if (recordedRequest.getPath().contains("time-entry-type?")) {
                        return callOk ? getListResponse(): get4xxErrorResponse(errorCode, errorMessage);
                    }
                    return callOk ? getRequestResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("DELETE")) {
                    return callOk ? getRequestResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("PUT")) {
                    return callOk ? getUpdateResponse(recordedRequest.getPath().contains("/archive/")) : get4xxErrorResponse(errorCode, errorMessage);
                } else {
                    return new MockResponse().setResponseCode(400);
                }
            }
        };
    }

    public static MockResponse getListResponse() {
        TimeEntryTypeDto timeEntryTypeDto1 = getTimeEntryTypeDto();
        timeEntryTypeDto1.setTypeName("Test1");
        TimeEntryTypeDto timeEntryTypeDto2 = getTimeEntryTypeDto();
        timeEntryTypeDto2.setTypeName("Test2");

        List<TimeEntryTypeDto> timeEntryTypeDtos = new ArrayList<>();
        timeEntryTypeDtos.add(timeEntryTypeDto1);
        timeEntryTypeDtos.add(timeEntryTypeDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryTypeDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        TimeEntryTypeRequestPostDto timeEntryTypeRequestPostDto = getTimeEntryTypeRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryTypeRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        TimeEntryTypeRequestDto timeEntryTypeRequestDto = getTimeEntryTypeRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryTypeRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        TimeEntryTypeRequestUpdateDto timeEntryTypeRequestUpdateDto = getTimeEntryTypeUpdateRequestDto();
        timeEntryTypeRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryTypeRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static TimeEntryTypeDto getTimeEntryTypeDto() {
        TimeEntryTypeDto timeEntryTypeRequestDto =
                new TimeEntryTypeDto();
        timeEntryTypeRequestDto.setTypeName("TestTimeEntryType");
        timeEntryTypeRequestDto.setName(false);
        timeEntryTypeRequestDto.setStartTime(false);
        timeEntryTypeRequestDto.setEndTime(false);
        timeEntryTypeRequestDto.setDate(false);
        timeEntryTypeRequestDto.setDescription(false);
        timeEntryTypeRequestDto.setWorkPackageUuid(false);
        timeEntryTypeRequestDto.setEmployeeUuid(false);

        return timeEntryTypeRequestDto;
    }

    @NotNull
    public static TimeEntryTypeRequestPostDto getTimeEntryTypeRequestPostDto() {
        TimeEntryTypeRequestPostDto timeEntryTypeRequestPostDto =
                new TimeEntryTypeRequestPostDto();
        timeEntryTypeRequestPostDto.setTypeName("TestTimeEntryType");
        timeEntryTypeRequestPostDto.setName(false);
        timeEntryTypeRequestPostDto.setStartTime(false);
        timeEntryTypeRequestPostDto.setEndTime(false);
        timeEntryTypeRequestPostDto.setDate(false);
        timeEntryTypeRequestPostDto.setDescription(false);
        timeEntryTypeRequestPostDto.setWorkPackageUuid(false);
        timeEntryTypeRequestPostDto.setEmployeeUuid(false);

        return timeEntryTypeRequestPostDto;
    }

    @NotNull
    public static TimeEntryTypeRequestDto getTimeEntryTypeRequestDto() {
        TimeEntryTypeRequestDto timeEntryTypeRequestDto =
                new TimeEntryTypeRequestDto();
        timeEntryTypeRequestDto.setTypeName("TestTimeEntryType");
        timeEntryTypeRequestDto.setName(false);
        timeEntryTypeRequestDto.setStartTime(false);
        timeEntryTypeRequestDto.setEndTime(false);
        timeEntryTypeRequestDto.setDate(false);
        timeEntryTypeRequestDto.setDescription(false);
        timeEntryTypeRequestDto.setWorkPackageUuid(false);
        timeEntryTypeRequestDto.setEmployeeUuid(false);

        return timeEntryTypeRequestDto;
    }

    @NotNull
    public static TimeEntryTypeRequestUpdateDto getTimeEntryTypeUpdateRequestDto() {
        TimeEntryTypeRequestUpdateDto timeEntryTypeRequestUpdateDto =
                new TimeEntryTypeRequestUpdateDto();
        timeEntryTypeRequestUpdateDto.setTypeName("UpdatedTimeEntryType");
        timeEntryTypeRequestUpdateDto.setName(true);
        timeEntryTypeRequestUpdateDto.setStartTime(true);
        timeEntryTypeRequestUpdateDto.setEndTime(true);
        timeEntryTypeRequestUpdateDto.setDate(true);
        timeEntryTypeRequestUpdateDto.setDescription(true);
        timeEntryTypeRequestUpdateDto.setWorkPackageUuid(true);
        timeEntryTypeRequestUpdateDto.setEmployeeUuid(true);

        return timeEntryTypeRequestUpdateDto;
    }

    public static MockResponse get4xxErrorResponse(int errorCode, String errorMessage) {
        return new MockResponse()
                .setResponseCode(errorCode)
                .setBody(errorMessage);
    }

    public static MockResponse getAccessTokenMockResponse() {
        // TODO: check Auth Token format
        var json = """
                {
                    "token_type": "Bearer",
                    "access_token": "l51PldIdEgkrZ1TOxZ38GzNGiiBW",
                    "expires_in": 3599,
                    "scope": ""
                }""";

        return new MockResponse()
                .setResponseCode(200)
                .setBody(json)
                .setHeader("Content-Type", MediaType.APPLICATION_JSON);
    }
}
