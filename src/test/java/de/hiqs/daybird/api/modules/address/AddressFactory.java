package de.hiqs.daybird.api.modules.address;

import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestPostDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddressFactory {
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
                    if (recordedRequest.getPath().contains("addresses?")) {
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
        AddressDto addressDto1 = getAddressDto();
        addressDto1.setCountry("Test1");
        AddressDto addressDto2 = getAddressDto();
        addressDto2.setCountry("Test2");

        List<AddressDto> addressDtos = new ArrayList<>();

        addressDtos.add(addressDto1);
        addressDtos.add(addressDto2);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(addressDtos))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getPostResponse() {
        AddressRequestPostDto addressRequestPostDto = getAddressRequestPostDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(addressRequestPostDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getRequestResponse() {
        AddressRequestDto addressRequestDto = getAddressRequestDto();

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(addressRequestDto))
                .addHeader("Content-Type", "application/json");
    }

    public static MockResponse getUpdateResponse(boolean archive) {
        AddressRequestUpdateDto addressRequestUpdateDto = getAddressUpdateRequestDto();
        addressRequestUpdateDto.setArchived(!archive);

        return new MockResponse()
                .setResponseCode(200)
                .setBody(jsonMapper.toJson(addressRequestUpdateDto))
                .addHeader("Content-Type", "application/json");
    }

    @NotNull
    public static AddressDto getAddressDto() {
        AddressDto addressDto =
                new AddressDto();
        addressDto.setCountry("TestCountry");
        addressDto.setCity("TestCity");
        addressDto.setZipCode("1234TestZipCode");
        addressDto.setStreet("TestStreet");
        addressDto.setHouseNumber("42TestH_N");
        return addressDto;
    }

    @NotNull
    public static AddressRequestPostDto getAddressRequestPostDto() {
        AddressRequestPostDto addressRequestPostDto =
                new AddressRequestPostDto();
        addressRequestPostDto.setCountry("TestCountry");
        addressRequestPostDto.setCity("TestCity");
        addressRequestPostDto.setZipCode("1234TestZipCode");
        addressRequestPostDto.setStreet("TestStreet");
        addressRequestPostDto.setHouseNumber("42TestH_N");
        return addressRequestPostDto;
    }

    @NotNull
    public static AddressRequestDto getAddressRequestDto() {
        AddressRequestDto addressRequestDto =
                new AddressRequestDto();
        addressRequestDto.setCountry("TestCountry");
        addressRequestDto.setCity("TestCity");
        addressRequestDto.setZipCode("1234TestZipCode");
        addressRequestDto.setStreet("TestStreet");
        addressRequestDto.setHouseNumber("42TestH_N");
        return addressRequestDto;
    }

    @NotNull
    public static AddressRequestUpdateDto getAddressUpdateRequestDto() {
        AddressRequestUpdateDto addressRequestUpdateDto =
                new AddressRequestUpdateDto();
        addressRequestUpdateDto.setCountry("UpdatedCountry");
        addressRequestUpdateDto.setCity("UpdatedCity");
        addressRequestUpdateDto.setZipCode("1234UpdatedZipCode");
        addressRequestUpdateDto.setStreet("UpdatedStreet");
        addressRequestUpdateDto.setHouseNumber("42UpdatedH_N");
        addressRequestUpdateDto.setArchived(true);
        return addressRequestUpdateDto;
    }

    public static MockResponse get4xxErrorResponse(int errorCode, String errorMessage) {
        System.out.println(errorCode);
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
