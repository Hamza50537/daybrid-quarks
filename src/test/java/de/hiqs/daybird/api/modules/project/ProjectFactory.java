package de.hiqs.daybird.api.modules.project;

import de.hiqs.daybird.api.modules.customer.CustomerFactory;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestPostDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestUpdateDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectDto;
import de.hiqs.daybird.api.modules.work_package.WorkPackageFactory;
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
import java.util.UUID;

public class ProjectFactory {

    private static final JsonUtils jsonMapper = new JsonUtils();

    public static Dispatcher getFullDispatcher(boolean accessOk, boolean callOk, int errorCode, String errorMessage) {

        return new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                System.out.println(recordedRequest.getMethod() + " : " + recordedRequest.getPath());

                if (Objects.requireNonNull(recordedRequest.getPath()).contains("auth/realms")) {
                    return accessOk ? getAccessTokenMockResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("POST")) {
                    return callOk ? getRequestResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("GET")) {
                    if (recordedRequest.getPath().contains("projects?")) {
                        return callOk ? getListResponse() : get4xxErrorResponse(errorCode, errorMessage);
                    }
                    if (recordedRequest.getPath().contains("work-packages?")) {
                        return callOk ? WorkPackageFactory.getListResponse() : get4xxErrorResponse(errorCode, errorMessage);
                    }
                    if (recordedRequest.getPath().contains("customer")) {
                        return callOk ? CustomerFactory.getRequestResponse() : get4xxErrorResponse(errorCode, errorMessage);
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
        ProjectDto projectDto1 = getProjectDto();
        projectDto1.setName("Test1");
        projectDto1.setUuid("4b084064-f2dc-4aa3-8135-6ebab6d163b0");
        ProjectDto projectDto2 = getProjectDto();
        projectDto2.setName("Test2");
        projectDto2.setUuid(UUID.randomUUID().toString());

        List<ProjectDto> projectDtos = new ArrayList<>();
        projectDtos.add(projectDto1);
        projectDtos.add(projectDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(projectDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        ProjectRequestPostDto projectRequestPostDto = getProjectRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(projectRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        ProjectRequestDto projectRequestDto = getProjectRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(projectRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        ProjectRequestUpdateDto projectRequestUpdateDto = getProjectUpdateRequestDto();
        projectRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(projectRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static ProjectDto getProjectDto() {

        CustomerDto customerDto =
                CustomerFactory.getCustomerDto();

        ProjectDto projectDto =
                new ProjectDto();
        projectDto.setName("TestProject");
        projectDto.setSign("TestProjectSign");
        projectDto.setStartDate(LocalDate.now());
        projectDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        projectDto.setCustomerUuid(customerDto.getUuid());
        return projectDto;
    }

    @NotNull
    public static ProjectRequestPostDto getProjectRequestPostDto() {

        CustomerRequestDto customerRequestDto =
                CustomerFactory.getCustomerRequestDto();

        ProjectRequestPostDto projectRequestPostDto =
                new ProjectRequestPostDto();
        projectRequestPostDto.setName("TestProject");
        projectRequestPostDto.setSign("TestProjectSign");
        projectRequestPostDto.setStartDate(LocalDate.now());
        projectRequestPostDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        projectRequestPostDto.setCustomerUuid(customerRequestDto.getUuid());
        return projectRequestPostDto;
    }

    @NotNull
    public static ProjectRequestDto getProjectRequestDto() {

        CustomerRequestDto customerRequestDto =
                CustomerFactory.getCustomerRequestDto();

        ProjectRequestDto projectRequestDto =
                new ProjectRequestDto();
        projectRequestDto.setName("TestProject");
        projectRequestDto.setSign("TestProjectSign");
        projectRequestDto.setStartDate(LocalDate.now());
        projectRequestDto.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        projectRequestDto.setCustomerUuid(customerRequestDto.getUuid());
        return projectRequestDto;
    }

    @NotNull
    public static ProjectRequestUpdateDto getProjectUpdateRequestDto() {
        ProjectRequestUpdateDto projectRequestUpdateDto =
                new ProjectRequestUpdateDto();
        projectRequestUpdateDto.setName("UpdatedProject");
        projectRequestUpdateDto.setSign("UpdatedProjectSign");
        projectRequestUpdateDto.setArchived(true);
        return projectRequestUpdateDto;
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
