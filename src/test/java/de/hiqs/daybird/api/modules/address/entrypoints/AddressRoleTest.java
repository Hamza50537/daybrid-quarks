package de.hiqs.daybird.api.modules.address.entrypoints;

import de.hiqs.daybird.api.modules.address.AddressFactory;
import de.hiqs.daybird.api.modules.address.dataproviders.AddressRestClient;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestPostDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressPostDto;
import de.hiqs.daybird.api.shared.AbstractMockWebServerTest;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AddressRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addAddressNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        AddressRequestPostDto addressRequestPostDto = AddressFactory.getAddressRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(addressRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void addAddressTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        AddressRequestPostDto addressRequestPostDto = AddressFactory.getAddressRequestPostDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        AddressPostDto addressDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(addressRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(addressRequestPostDto.getCity(), addressDtoResponse.getCity());
            Assertions.assertEquals(addressRequestPostDto.getCountry(), addressDtoResponse.getCountry());
            Assertions.assertEquals(addressRequestPostDto.getZipCode(), addressDtoResponse.getZipCode());
            Assertions.assertEquals(addressRequestPostDto.getStreet(), addressDtoResponse.getStreet());
            Assertions.assertEquals(addressRequestPostDto.getHouseNumber(), addressDtoResponse.getHouseNumber());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 200, 200"})
    void getAddressTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        AddressDto addressDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + addressRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(addressRequestDto.getUuid(), addressDtoResponse.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getAddressQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        AddressRequestDto addressRequestDto1 = AddressFactory.getAddressRequestDto();
        addressRequestDto1.setCountry("Test1");

        AddressRequestDto addressRequestDto2 = AddressFactory.getAddressRequestDto();
        addressRequestDto2.setCountry("Test2");

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String addressQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(AddressRestClient.PARAM_COUNTRY, checkAndGetValueOrNull(addressRequestDto1.getCountry()))
                .queryParam(AddressRestClient.PARAM_CITY, checkAndGetValueOrNull(addressRequestDto1.getCity()))
                .queryParam(AddressRestClient.PARAM_STREET, checkAndGetValueOrNull(addressRequestDto1.getStreet()))
                .queryParam(AddressRestClient.PARAM_HOUSE_NUMBER, checkAndGetValueOrNull(addressRequestDto1.getHouseNumber()))
                .queryParam(AddressRestClient.PARAM_ZIP_CODE, checkAndGetValueOrNull(addressRequestDto1.getZipCode()))
                .queryParam(AddressRestClient.PARAM_ARCHIVED, addressRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(addressQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 204, 204", "hr, 123, 204, 204", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteAddressTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + addressRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void updateAddressTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        AddressRequestUpdateDto addressRequestUpdateDto = AddressFactory.getAddressUpdateRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        AddressDto updatedAddress = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(addressRequestUpdateDto)
                .when().put(getBasePath() + "/" + addressRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(addressRequestUpdateDto.getCity(), updatedAddress.getCity());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveAddressTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        AddressRequestUpdateDto addressRequestUpdateDto = AddressFactory.getAddressUpdateRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        AddressDto addressDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(AddressRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.ADDRESS)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(addressDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return AddressResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
