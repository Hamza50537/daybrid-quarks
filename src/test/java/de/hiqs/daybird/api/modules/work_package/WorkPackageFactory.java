package de.hiqs.daybird.api.modules.work_package;

import de.hiqs.daybird.api.modules.employee.EmployeeFactory;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.modules.project.ProjectFactory;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestPostDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestUpdateDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkPackageFactory {

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
                    if (recordedRequest.getPath().contains("work-packages?")) {
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
        WorkPackageDto workPackageDto1 = getWorkPackageDto();
        workPackageDto1.setName("Test1");
        workPackageDto1.setProjectUuid("4b084064-f2dc-4aa3-8135-6ebab6d163b0");
        WorkPackageDto workPackageDto2 = getWorkPackageDto();
        workPackageDto2.setName("Test2");
        workPackageDto2.setProjectUuid("4b084064-f2dc-4aa3-8135-6ebab6d163b0");

        List<WorkPackageDto> workPackageDtos = new ArrayList<>();
        workPackageDtos.add(workPackageDto1);
        workPackageDtos.add(workPackageDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(workPackageDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        WorkPackageRequestPostDto workPackageRequestPostDto = getWorkPackageRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(workPackageRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }


    public static MockResponse getRequestResponse() {
        WorkPackageRequestDto workPackageRequestDto = getWorkPackageRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(workPackageRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        WorkPackageRequestUpdateDto workPackageRequestUpdateDto = getWorkPackageUpdateRequestDto();
        workPackageRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(workPackageRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static WorkPackageDto getWorkPackageDto() {

        EmployeeDto employeeDto =
                EmployeeFactory.getEmployeeDto();

        ProjectDto projectDto =
                ProjectFactory.getProjectDto();

        WorkPackageDto workPackageDto =
                new WorkPackageDto();
        workPackageDto.setName("TestWorkPackage");
        workPackageDto.setSign("TestWPSign");
        workPackageDto.setStartDate(LocalDate.now());
        workPackageDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        workPackageDto.setProjectUuid(projectDto.getUuid());
        workPackageDto.setEmployeeUuid(employeeDto.getUuid());

        return workPackageDto;
    }

    @NotNull
    public static WorkPackageRequestPostDto getWorkPackageRequestPostDto() {

        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        ProjectRequestDto projectRequestDto =
                ProjectFactory.getProjectRequestDto();

        WorkPackageRequestPostDto workPackageRequestPostDto =
                new WorkPackageRequestPostDto();
        workPackageRequestPostDto.setName("TestWorkPackage");
        workPackageRequestPostDto.setSign("TestWPSign");
        workPackageRequestPostDto.setStartDate(LocalDate.now());
        workPackageRequestPostDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        workPackageRequestPostDto.setProjectUuid(projectRequestDto.getUuid());
        workPackageRequestPostDto.setEmployeeUuid(employeeRequestDto.getUuid());

        return workPackageRequestPostDto;
    }

    @NotNull
    public static WorkPackageRequestDto getWorkPackageRequestDto() {

        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        ProjectRequestDto projectRequestDto =
                ProjectFactory.getProjectRequestDto();

        WorkPackageRequestDto workPackageRequestDto =
                new WorkPackageRequestDto();
        workPackageRequestDto.setName("TestWorkPackage");
        workPackageRequestDto.setSign("TestWPSign");
        workPackageRequestDto.setStartDate(LocalDate.now());
        workPackageRequestDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        workPackageRequestDto.setProjectUuid(projectRequestDto.getUuid());
        workPackageRequestDto.setEmployeeUuid(employeeRequestDto.getUuid());

        return workPackageRequestDto;
    }

    @NotNull
    public static WorkPackageRequestUpdateDto getWorkPackageUpdateRequestDto() {
        WorkPackageRequestUpdateDto workPackageRequestUpdateDto =
                new WorkPackageRequestUpdateDto();
        workPackageRequestUpdateDto.setName("UpdatedWorkPackage");
        workPackageRequestUpdateDto.setSign("UpdatedWPSign");
        workPackageRequestUpdateDto.setArchived(true);
        return workPackageRequestUpdateDto;
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
