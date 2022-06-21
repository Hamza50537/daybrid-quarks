package de.hiqs.daybird.api.modules.time_entry;

import de.hiqs.daybird.api.modules.employee.EmployeeFactory;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryDto;
import de.hiqs.daybird.api.modules.time_entry_type.TimeEntryTypeFactory;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestDto;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypeDto;
import de.hiqs.daybird.api.modules.work_package.WorkPackageFactory;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeEntryFactory {

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
                    if (recordedRequest.getPath().contains("time-entry?")) {
                        return callOk ? getListResponse() : get4xxErrorResponse(errorCode, errorMessage);
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
        TimeEntryDto timeEntryDto1 = getTimeEntryDto();
        timeEntryDto1.setName("Test1");
        TimeEntryDto timeEntryDto2 = getTimeEntryDto();
        timeEntryDto2.setName("Test2");

        List<TimeEntryDto> timeEntryDtos = new ArrayList<>();
        timeEntryDtos.add(timeEntryDto1);
        timeEntryDtos.add(timeEntryDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        TimeEntryRequestPostDto timeEntryRequestPostDto = getTimeEntryRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        TimeEntryRequestDto timeEntryRequestDto = getTimeEntryRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto = getTimeEntryUpdateRequestDto();
        timeEntryRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(timeEntryRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static TimeEntryDto getTimeEntryDto() {
        EmployeeDto employeeDto =
                EmployeeFactory.getEmployeeDto();

        WorkPackageDto workPackageDto =
                WorkPackageFactory.getWorkPackageDto();

        TimeEntryTypeDto timeEntryTypeDto =
                TimeEntryTypeFactory.getTimeEntryTypeDto();

        TimeEntryDto timeEntryDto =
                new TimeEntryDto();
        timeEntryDto.setName("TestTimeEntry");
        timeEntryDto.setStart(LocalTime.now());
        timeEntryDto.setEnd(LocalTime.now().plus(3, ChronoUnit.HOURS));
        timeEntryDto.setDate(LocalDate.now());
        timeEntryDto.setDescription("TimeEntryDescription");
        timeEntryDto.setWorkPackageUuid(workPackageDto.getUuid());
        timeEntryDto.setTimeEntryTypeUuid(timeEntryTypeDto.getUuid());
        timeEntryDto.setEmployeeUuid(employeeDto.getUuid());

        return timeEntryDto;
    }

    @NotNull
    public static TimeEntryRequestPostDto getTimeEntryRequestPostDto() {
        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        WorkPackageRequestDto workPackageRequestDto =
                WorkPackageFactory.getWorkPackageRequestDto();

        TimeEntryTypeRequestDto timeEntryTypeRequestDto =
                TimeEntryTypeFactory.getTimeEntryTypeRequestDto();

        TimeEntryRequestPostDto timeEntryRequestPostDto =
                new TimeEntryRequestPostDto();
        timeEntryRequestPostDto.setName("TestTimeEntry");
        timeEntryRequestPostDto.setStart(LocalTime.now());
        timeEntryRequestPostDto.setEnd(LocalTime.now().plus(3, ChronoUnit.HOURS));
        timeEntryRequestPostDto.setDate(LocalDate.now());
        timeEntryRequestPostDto.setDescription("TimeEntryDescription");
        timeEntryRequestPostDto.setWorkPackageUuid(workPackageRequestDto.getUuid());
        timeEntryRequestPostDto.setTimeEntryTypeUuid(timeEntryTypeRequestDto.getUuid());
        timeEntryRequestPostDto.setEmployeeUuid(employeeRequestDto.getUuid());

        return timeEntryRequestPostDto;
    }

    @NotNull
    public static TimeEntryRequestDto getTimeEntryRequestDto() {
        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        WorkPackageRequestDto workPackageRequestDto =
                WorkPackageFactory.getWorkPackageRequestDto();

        TimeEntryTypeRequestDto timeEntryTypeRequestDto =
                TimeEntryTypeFactory.getTimeEntryTypeRequestDto();

        TimeEntryRequestDto timeEntryRequestDto =
                new TimeEntryRequestDto();
        timeEntryRequestDto.setName("TestTimeEntry");
        timeEntryRequestDto.setStart(LocalTime.now());
        timeEntryRequestDto.setEnd(LocalTime.now().plus(3, ChronoUnit.HOURS));
        timeEntryRequestDto.setDate(LocalDate.now());
        timeEntryRequestDto.setDescription("TimeEntryDescription");
        timeEntryRequestDto.setWorkPackageUuid(workPackageRequestDto.getUuid());
        timeEntryRequestDto.setTimeEntryTypeUuid(timeEntryTypeRequestDto.getUuid());
        timeEntryRequestDto.setEmployeeUuid(employeeRequestDto.getUuid());

        return timeEntryRequestDto;
    }

    @NotNull
    public static TimeEntryRequestUpdateDto getTimeEntryUpdateRequestDto() {
        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto =
                new TimeEntryRequestUpdateDto();
        timeEntryRequestUpdateDto.setName("UpdatedTimeEntry");
        timeEntryRequestUpdateDto.setDescription("UpdatedDescription");
        timeEntryRequestUpdateDto.setArchived(true);
        return timeEntryRequestUpdateDto;
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
