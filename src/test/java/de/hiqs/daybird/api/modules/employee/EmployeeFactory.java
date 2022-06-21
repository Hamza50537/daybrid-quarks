package de.hiqs.daybird.api.modules.employee;

import de.hiqs.daybird.api.modules.address.AddressFactory;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestPostDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestUpdateDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeFactory {

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
                    if (recordedRequest.getPath().contains("employees?")) {
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
        EmployeeDto employeeDto1 = getEmployeeDto();
        employeeDto1.setFirstName("Test1");
        EmployeeDto employeeDto2 = getEmployeeDto();
        employeeDto2.setFirstName("Test2");

        List<EmployeeDto> employeeDtos = new ArrayList<>();

        employeeDtos.add(employeeDto1);
        employeeDtos.add(employeeDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(employeeDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        EmployeeRequestPostDto employeeRequestPostDto = getEmployeeRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(employeeRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        EmployeeRequestDto employeeRequestDto = getEmployeeRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(employeeRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        EmployeeRequestUpdateDto employeeRequestUpdateDto = getEmployeeUpdateRequestDto();
        employeeRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(employeeRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static EmployeeDto getEmployeeDto() {
        AddressDto addressDto =
                AddressFactory.getAddressDto();

        EmployeeDto employeeDto =
                new EmployeeDto();
        employeeDto.setFirstName("TestEmployee");
        employeeDto.setLastName("TestLastName");
        employeeDto.setAddressUuid(addressDto.getUuid());
        employeeDto.setJobTitle("TestJob");
        employeeDto.setEmail("TestMail");
        employeeDto.setTargetHours("TestHours");

        return employeeDto;
    }

    @NotNull
    public static EmployeeRequestPostDto getEmployeeRequestPostDto() {
        AddressRequestDto addressRequestDto =
                AddressFactory.getAddressRequestDto();

        EmployeeRequestPostDto employeeRequestPostDto =
                new EmployeeRequestPostDto();
        employeeRequestPostDto.setFirstName("TestEmployee");
        employeeRequestPostDto.setLastName("TestLastName");
        employeeRequestPostDto.setAddressUuid(addressRequestDto.getUuid());
        employeeRequestPostDto.setJobTitle("TestJob");
        employeeRequestPostDto.setEmail("TestMail");
        employeeRequestPostDto.setTargetHours("TestHours");

        return employeeRequestPostDto;
    }

    @NotNull
    public static EmployeeRequestDto getEmployeeRequestDto() {
        AddressRequestDto addressRequestDto =
                AddressFactory.getAddressRequestDto();

        EmployeeRequestDto employeeRequestDto =
                new EmployeeRequestDto();
        employeeRequestDto.setFirstName("TestEmployee");
        employeeRequestDto.setLastName("TestLastName");
        employeeRequestDto.setAddressUuid(addressRequestDto.getUuid());
        employeeRequestDto.setJobTitle("TestJob");
        employeeRequestDto.setEmail("TestMail");
        employeeRequestDto.setTargetHours("TestHours");

        return employeeRequestDto;
    }

    @NotNull
    public static EmployeeRequestUpdateDto getEmployeeUpdateRequestDto() {
        EmployeeRequestUpdateDto employeeRequestUpdateDto =
                new EmployeeRequestUpdateDto();
        employeeRequestUpdateDto.setFirstName("UpdatedEmployee");
        employeeRequestUpdateDto.setLastName("UpdatedLastName");
        employeeRequestUpdateDto.setJobTitle("UpdatedJob");
        employeeRequestUpdateDto.setEmail("UpdatedMail");
        employeeRequestUpdateDto.setTargetHours("UpdatedHours");
        employeeRequestUpdateDto.setArchived(true);

        return employeeRequestUpdateDto;
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
