package de.hiqs.daybird.api.modules.customer;

import de.hiqs.daybird.api.modules.address.AddressFactory;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestPostDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestUpdateDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerFactory {

    private static final JsonUtils jsonMapper = new JsonUtils();

    public static Dispatcher getFullDispatcher(boolean accessOk, boolean callOk, int errorCode, String errorMessage) {

        return new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                if (Objects.requireNonNull(recordedRequest.getPath()).contains("auth/realms")) {
                    return accessOk ? getAccessTokenMockResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("POST")) {
                    return callOk ? getPostResponse() : get4xxErrorResponse(errorCode, errorMessage);
                } else if (recordedRequest.getMethod().equalsIgnoreCase("GET")) {
                    if (recordedRequest.getPath().contains("customers?")) {
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
        CustomerDto customerDto1 = getCustomerDto();
        customerDto1.setName("Test1");
        CustomerDto customerDto2 = getCustomerDto();
        customerDto2.setName("Test2");

        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(customerDto1);
        customerDtos.add(customerDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(customerDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        CustomerRequestPostDto customerRequestPostDto = getCustomerRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(customerRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        CustomerRequestDto customerRequestDto = getCustomerRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(customerRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        CustomerRequestUpdateDto customerRequestUpdateDto = getCustomerUpdateRequestDto();
        customerRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(customerRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static CustomerDto getCustomerDto() {
        AddressDto addressDto =
                AddressFactory.getAddressDto();

        CustomerDto customerDto =
                new CustomerDto();
        customerDto.setName("TestName");
        customerDto.setSign("TestSign");
        customerDto.setEmail("test@testmail.de");
        customerDto.setPhoneNumber("12341234");
        customerDto.setAddressUuid(addressDto.getUuid());
        return customerDto;
    }

    @NotNull
    public static CustomerRequestPostDto getCustomerRequestPostDto() {
        AddressRequestDto addressRequestDto =
                AddressFactory.getAddressRequestDto();

        CustomerRequestPostDto customerRequestPostDto =
                new CustomerRequestPostDto();
        customerRequestPostDto.setName("TestName");
        customerRequestPostDto.setSign("TestSign");
        customerRequestPostDto.setEmail("test@testmail.de");
        customerRequestPostDto.setPhoneNumber("12341234");
        customerRequestPostDto.setAddressUuid(addressRequestDto.getUuid());
        return customerRequestPostDto;
    }

    @NotNull
    public static CustomerRequestDto getCustomerRequestDto() {
        AddressRequestDto addressRequestDto =
                AddressFactory.getAddressRequestDto();

        CustomerRequestDto customerRequestDto =
                new CustomerRequestDto();
        customerRequestDto.setName("TestName");
        customerRequestDto.setSign("TestSign");
        customerRequestDto.setEmail("test@testmail.de");
        customerRequestDto.setPhoneNumber("12341234");
        customerRequestDto.setAddressUuid(addressRequestDto.getUuid());
        return customerRequestDto;
    }

    @NotNull
    public static CustomerRequestUpdateDto getCustomerUpdateRequestDto() {
        CustomerRequestUpdateDto customerRequestUpdateDto =
                new CustomerRequestUpdateDto();
        customerRequestUpdateDto.setName("UpdatedName");
        customerRequestUpdateDto.setSign("UpdatedSign");
        customerRequestUpdateDto.setEmail("updatedtest@testmail.de");
        customerRequestUpdateDto.setPhoneNumber("123412345");
        return customerRequestUpdateDto;
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
